package CFGPackage;

import java.util.LinkedList;

import org.eclipse.jdt.core.dom.CatchClause;

import GraphElements.*;
import JavaParserPackage.JavaClassParser;

public class NormalCFG extends CFGGraph{
	
	


	public NormalCFG(){
		allNodes = new LinkedList<Node>();
		allEdges = new LinkedList<Edge>();
		linesofCodeInsideRecursion = 0;
		joinBeforeElseFound = false;
		totalNumberOfNodesAfterEachGraph = 0;
		breaksFound = false;
		breakIdentifiers = new LinkedList<Integer>();
		methodSkipEdges = new LinkedList<Integer>();
	}



	public void generateGraph(String fileData) throws Exception{
		LinkedList<String> methods = new LinkedList<String>();
		JavaClassParser javaParseInstance = new JavaClassParser();
		methods = javaParseInstance.methodStatementParser(fileData);
		if(methods.size() > 0){

			JavaClassParser methodParser = new JavaClassParser();
			int initialIdentifier = 1;
			int initialNodeNumbering = 1;
			int previousNodeIdentifier = 1;
			for(int methodCounter = 0;methodCounter < methods.size(); methodCounter++){
				MethodTree currentMethodTree = new MethodTree();
				//WE ARE CHEATING HERE, ASTPARSER NEEDS THE CLASS 
				//INICIALIZATION TO PARSER ANYTHING INSIDE THE METHODS(I KNOW IT'S WEIRD)
				String methodToParse = "public class thisClass{ \n" + methods.get(methodCounter) + "\n }";
				LinkedList<String> currentMethodStatements = methodParser.ifStatementParser(methodToParse);
				currentMethodTree.setIfStatements(currentMethodStatements);
				currentMethodStatements = methodParser.forStatementParser(methodToParse);
				currentMethodTree.setForStatements(currentMethodStatements);
				currentMethodStatements = methodParser.whileStatementParser(methodToParse);
				currentMethodTree.setWhileStatements(currentMethodStatements);
				currentMethodStatements = methodParser.doWhileStatementParser(methodToParse);
				currentMethodTree.setDoWhileStatements(currentMethodStatements);
				currentMethodStatements = methodParser.switchStatementParser(methodToParse);
				currentMethodTree.setSwitchStatements(currentMethodStatements);
				currentMethodStatements = methodParser.tryStatementParser(methodToParse);
				currentMethodTree.setTryStatements(currentMethodStatements);

				this.methodSkipEdges.add(initialIdentifier);
				initialNodeNumbering = this.methodParseRecursion(methods.get(methodCounter), currentMethodTree,initialIdentifier,initialNodeNumbering,previousNodeIdentifier);
				previousNodeIdentifier = initialNodeNumbering;
				if(this.islastNodeJoin() == true){
					this.removeLastJoin();
				}
				initialIdentifier = this.getAllNodes().getLast().getIdentifier() + 1;
				this.setTotalNumberOfNodesAfterEachGraph(this.allNodes.size());

			}


		}
	}

	private int methodParseRecursion(String currentSectionOfCode,MethodTree currentMethodTree, int identifier,int nodeNumbering,int previousNodeIdentifier) throws Exception{
		LinkedList<Node> currentNodes = this.getAllNodes();
		LinkedList<Edge> currentEdges = this.getAllEdges();
		Node newNode = null;
		Edge newEdge = null;
		LinkedList<String> linesOfCode = new LinkedList<String>();
		LinkedList<Integer> tryIdentifiers = null;
		LinkedList<Integer> catchIdentifiers = null;
		boolean ifFound = false;
		int lastIfStatementFoundIdentifier = 0;

		CodeBlockFeatures currentBlock = null;

		String[] javaContentData = currentSectionOfCode.split("\n");
		if(javaContentData != null)
			this.setLinesofCodeInsideRecursion(javaContentData.length);
		else
			this.setLinesofCodeInsideRecursion(0);

		if(identifier == previousNodeIdentifier && identifier != 1 && previousNodeIdentifier != 1){
			identifier++;
		}
		for(int i = 0; i < javaContentData.length;i++){
			//System.out.println(javaContentData[i]);
			if(isMethodCall(javaContentData[i]) == true){
				//DO NOTHING
			}
			//CHECKING IF CURRENT LINE IS AN IF STATEMENT
			else if(isIfStatement(javaContentData[i]) == true && i != 0){//FOUND AN IF, AND i CURRENT LINE IS NOT THE SAME IF WE'RE WORKING ON

				ifFound = true;
				lastIfStatementFoundIdentifier = previousNodeIdentifier;
				currentBlock = new CodeBlockFeatures(currentMethodTree, previousNodeIdentifier, newNode, newEdge, currentNodes, currentEdges, nodeNumbering, i, linesOfCode, javaContentData, identifier);
				currentBlock = this.runBlockStatement(currentBlock,"if");

			}
			//CHECKING IF CURRENT LINE IS A FOR STATEMENT
			else if(isForStatement(javaContentData[i]) == true && i != 0){

				currentBlock = new CodeBlockFeatures(currentMethodTree, previousNodeIdentifier, newNode, newEdge, currentNodes, currentEdges, nodeNumbering, i, linesOfCode, javaContentData, identifier);
				currentBlock = this.runBlockStatement(currentBlock,"for");
			}
			//CHECKING IF CURRENT LINE IS A WHILE STATEMENT
			else if(isWhileStatement(javaContentData[i]) == true && i != 0){
				currentBlock = new CodeBlockFeatures(currentMethodTree, previousNodeIdentifier, newNode, newEdge, currentNodes, currentEdges, nodeNumbering, i, linesOfCode, javaContentData, identifier);
				currentBlock = this.runBlockStatement(currentBlock,"while");
			}
			//CHECKING IF CURRENT LINE IS A DO WHILE STATEMENT
			else if(isDoWhileStatement(javaContentData[i]) == true && i != 0){
				currentBlock = new CodeBlockFeatures(currentMethodTree, previousNodeIdentifier, newNode, newEdge, currentNodes, currentEdges, nodeNumbering, i, linesOfCode, javaContentData, identifier);
				currentBlock = this.runBlockStatement(currentBlock,"do");
			}
			//CHECKING IF CURRENT LINE IS A SWITCH STATEMENT
			else if(isSwitchStatement(javaContentData[i]) == true && i != 0){
				currentBlock = new CodeBlockFeatures(currentMethodTree, previousNodeIdentifier, newNode, newEdge, currentNodes, currentEdges, nodeNumbering, i, linesOfCode, javaContentData, identifier);
				currentBlock = this.runBlockStatement(currentBlock,"switch");
			}
			//CHECKING IF CURRENT LINE IS A SWITCH STATEMENT
			else if(isTryStatement(javaContentData[i]) == true && i != 0){
				currentBlock = new CodeBlockFeatures(currentMethodTree, previousNodeIdentifier, newNode, newEdge, currentNodes, currentEdges, nodeNumbering, i, linesOfCode, javaContentData, identifier);
				currentBlock = this.runBlockStatement(currentBlock,"try");
			}

			else if(isSwitchCaseFound(javaContentData[i]) == true || isSwitchDefaultFound(javaContentData[i]) == true){

				if(newNode != null){
					newNode = this.closeNode(newNode, linesOfCode);
					currentNodes.add(newNode);
					this.setAllNodes(currentNodes);

					//SINCE WE SAVED THE DATA FROM PREVIOUS NODE, NOW WE NEED TO EMPTY THE NODE TO CREATE A NEW ONE
					newNode = null;
					linesOfCode = new LinkedList<String>();
				}
				//TIME TO CREATE A NEW NODE
				newNode = new Node(nodeNumbering+",",identifier);
				linesOfCode.add(javaContentData[i].trim());
				nodeNumbering++;
				identifier++;//NEW NODE IDENTIFIER

			}else if(isCatchStatement(javaContentData[i]) == true){
				if(catchIdentifiers == null)
					catchIdentifiers = new LinkedList<Integer>();
				if(newNode != null){
					//IF THE PREVIOUS NODE IS A 'JOIN', IT'LL CREATE AN EDGE BETWEEN THE CURRENT NODE AND THE 'JOIN'
					this.createJoinEdge(currentNodes,newNode);

					//SINCE WE FOUND AN IF, WE NEED TO SAVE THE PREVIOUS NODE DATA
					newNode = this.closeNode(newNode, linesOfCode);
					//WE NEED TO SAVE THE LAST NODE IDENTIFIER HAD THE LAST NODE
					previousNodeIdentifier = newNode.getIdentifier();
					currentNodes.add(newNode);
					//SAVING ALL NODES TO THE GLOBAL LIST OF NODES
					this.setAllNodes(currentNodes);

					//SINCE WE SAVED THE DATA FROM PREVIOUS NODE, NOW WE NEED TO EMPTY THE NODE TO CREATE A NEW ONE
					newNode = null;
					linesOfCode = new LinkedList<String>();

					newNode = new Node(nodeNumbering+",",identifier);
					linesOfCode.add(javaContentData[i].trim());
					nodeNumbering++;
					identifier++;//NEW NODE IDENTIFIER


					catchIdentifiers.add(newNode.getIdentifier());
				}
			}
			else if(isSwitchStatement(javaContentData[i]) == true && i == 0){
				//DO NOTHING
			}
			else if(isDoWhileStatement(javaContentData[i]) == true && i == 0){
				//DO NOTHING
			}
			else if(isForStatement(javaContentData[i]) == true && i == 0){
				//DO NOTHING
			}
			else if(isWhileStatement(javaContentData[i]) == true && i == 0){
				//DO NOTHING
			}
			else if(isIfStatement(javaContentData[i]) == true && i == 0){
				ifFound = true;
				lastIfStatementFoundIdentifier = previousNodeIdentifier;
			}
			else if(isTryStatement(javaContentData[i]) == true && i == 0){
				tryIdentifiers = new LinkedList<Integer>();

			}
			else  if(javaContentData[i].trim().equals("{") || javaContentData[i].trim().equals("}")){
				//DO NOTHING
			}
			else if(javaContentData[i].trim().substring(0, 2).equals("//")){//THIS MEANS THIS IS A COMMENT LINE, WE NEED TO SKIP THESE
				//DO NOTHING
			}
			else{
				//IF newNode IS NULL, THIS MEANS WE NEED TO CREATE A NEW NODE
				if(newNode == null && isElseStatement(javaContentData[i].trim()) == false){
					newNode = new Node(nodeNumbering+",",identifier);
					linesOfCode.add(javaContentData[i].trim());
					nodeNumbering++;
					identifier++;//NEW NODE IDENTIFIER

					//THIS SECTION OF CODE WILL ADD AN EDGE IF THE PREVIOUS LINE WAS AN ELSE IF STATEMENT
					if(i > 0 && javaContentData != null){
						String previousLineOfCode = javaContentData[i-1].trim();
						if(isElseIfStatement(previousLineOfCode) == true){
							int previousLineIdentifier = getIdentifierFromText(currentNodes, previousLineOfCode);
							newEdge = this.createEdge(previousLineIdentifier,newNode.getIdentifier());
							currentEdges.add(newEdge);
							this.setAllEdges(currentEdges);

						}

					}

					if(tryIdentifiers != null && catchIdentifiers == null){
						tryIdentifiers.add(newNode.getIdentifier());
					}
				}
				else{

					if(isElseStatement(javaContentData[i].trim()) == true){
						if(newNode != null){//DOUBLE MAKING SURE THAT IF THE NODE IS NULL OR NOT
							//IF THE PREVIOUS NODE IS A 'JOIN', IT'LL CREATE AN EDGE BETWEEN THE CURRENT NODE AND THE 'JOIN'
							if(ifFound == true){//THIS WILL PREVENT FROM CHILD NODES TO CONNECT TO PARENT NODES 
								//this.createJoinEdge(currentNodes,newNode);//THIS WILL PREVENT FROM CHILD NODES TO CONNECT TO PARENT NODES 		
								newEdge = this.createEdge(lastIfStatementFoundIdentifier,identifier);
								currentEdges.add(newEdge);
							}else{
								this.setJoinBeforeElseFound(false);
							}
							newNode = this.closeNode(newNode, linesOfCode);
							currentNodes.add(newNode);

							this.setAllNodes(currentNodes);
							this.setAllEdges(currentEdges);
							//SINCE WE SAVED THE DATA FROM PREVIOUS NODE, NOW WE NEED TO EMPTY THE NODE TO CREATE A NEW ONE
							newNode = null;
							linesOfCode = new LinkedList<String>();
						}

						if(isElseIfStatement(javaContentData[i].trim()) == true){
							newNode = new Node(nodeNumbering+",",identifier);
							linesOfCode.add(javaContentData[i].trim());
							nodeNumbering++;
							identifier++;//NEW NODE IDENTIFIER

							newNode = this.closeNode(newNode, linesOfCode);
							currentNodes.add(newNode);

							this.setAllNodes(currentNodes);

							
							
							if(ifFound == true){
								newEdge = this.createEdge(lastIfStatementFoundIdentifier,newNode.getIdentifier());
								currentEdges.add(newEdge);
								this.setAllEdges(currentEdges);
							}
							
							//SINCE WE SAVED THE DATA FROM PREVIOUS NODE, NOW WE NEED TO EMPTY THE NODE TO CREATE A NEW ONE
							newNode = null;
							linesOfCode = new LinkedList<String>();
						}
						if(findJoinBeforeElseStatement(this.getAllNodes().get(this.getAllNodes().size()-1).getThisNodeText()) == true){
							this.setJoinBeforeElseFound(true);
						}

					}else{
						//IF THIS IS JUST CODE, THEN WE KEEP ON ADDING IT TO THE SAME NODE
						newNode.setThisNodeText(newNode.getThisNodeText() + nodeNumbering + ",");
						linesOfCode.add(javaContentData[i].trim());
						nodeNumbering++;
					}
				}
			}

			if(isBreakFound(javaContentData[i]) == true){
				this.setBreaksFound(true);
				LinkedList<Integer> currentBreaksFound = this.getBreakIdentifiers();
				currentBreaksFound.add(newNode.getIdentifier());
				this.setBreakIdentifiers(currentBreaksFound);
			}

			if(currentBlock != null){

				//CLOSE PREVIOUS NODE BEFORE WORKING ON IFSTATEMENTS
				previousNodeIdentifier = currentBlock.getPreviousNodeIdentifier();

				nodeNumbering = currentBlock.getNodeNumbering();
				i = currentBlock.getBlockPointer();

				//ONCE DONE WITH THE INNER DATA FROM THIS IF, NOW WE NEED TO CREATE A JOIN NODE
				identifier = currentBlock.getIdentifier();



				//UPDATING OUR LOCAL VARIABLES AFTER RECURSION
				currentNodes = currentBlock.getCurrentNodes();
				currentEdges = currentBlock.getCurrentEdges();

				while(findIdentifierInNodes(currentNodes, identifier) == true){
					identifier++;
				}


				//SINCE WE SAVED THE DATA FROM PREVIOUS NODE, NOW WE NEED TO EMPTY THE NODE TO CREATE A NEW ONE
				newNode = null;
				linesOfCode = new LinkedList<String>();


				if(tryIdentifiers != null){
					//TIME TO ADD INNER NODES TO THE 
					for(int z = previousNodeIdentifier;z < currentNodes.getLast().getIdentifier();z++){
						if(!currentNodes.get(z).getThisNodeText().toLowerCase().equals("join"))
							tryIdentifiers.add(currentNodes.get(z).getIdentifier());
					}




				}

				currentBlock = null;
			}

		}
		//IF WE GOT THIS FAR, THIS MEANS THAT THIS IS THE LAST LINE IN NODE FOR THIS PIECE OF CODE
		if(newNode != null){

			//IF THE PREVIOUS NODE IS A 'JOIN', IT'LL CREATE AN EDGE BETWEEN THE CURRENT NODE AND THE 'JOIN'
			//if(this.isJoinBeforeElseFound() == false)//THIS WILL PREVENT FROM CHILD NODES TO CONNECT TO PARENT NODES 
			//this.createJoinEdge(currentNodes,newNode);

			/*
			 * THIS IF STATEMENT WILL GENERATE THE LAST EDGE FOR EACH METHOD, HOWEVER, WE ENDED UP USING A GLOBAL VARIABLE TO KEEP TRACK
			 * OF HOW MANY NODES GET ENTERED AFTER EACH METHOD IN THE JAVA FILE. 
			 * THIS WILL GENERATE A EDGE ONLY IF THERE'S AT LEAST ONE NODE ALREADY CREATED BEFORE THIS LAST NODE SO THAT THERE CAN BE AN EDGE
			 */
			if((isThisLastNode(javaContentData) == true && (currentNodes.size() - this.getTotalNumberOfNodesAfterEachGraph()) > 0) || islastNodeJoin() == true){
				newEdge = this.createEdge(currentNodes.getLast().getIdentifier(),newNode.getIdentifier());
				currentEdges.add(newEdge);
				this.setAllEdges(currentEdges);
			}

			newNode = this.closeNode(newNode, linesOfCode);
			currentNodes.add(newNode);
			this.setAllNodes(currentNodes);
		}
		//THIS SHOULD CREATE THE 'TRY' JOIN THAT WILL CONNECT TO ALL THE NODES INSIDE THE TRY
		if(tryIdentifiers != null){
			identifier = this.createJoinNode(tryIdentifiers, identifier);
			currentEdges = this.getAllEdges();

			for(int x = 0; catchIdentifiers != null && x < catchIdentifiers.size(); x++){
				newEdge = this.createEdge(currentNodes.getLast().getIdentifier(),catchIdentifiers.get(x) );
				currentEdges.add(newEdge);
				this.setAllEdges(currentEdges);
			}
		}

		return nodeNumbering;

	}







	private int preJoinNodeProcess(Node newNode,LinkedList<Node> currentNodes,Edge newEdge, 
			LinkedList<Edge> currentEdges,int nodeNumbering,LinkedList<String> linesOfCode, String lineOfCode, 
			int previousNodeIdentifier, int identifier){

		if(newNode != null){
			//WE WANT TO PREVENT ADDING THE DO WHILE STATEMENT TO TE NODE
			if(isDoWhileStatement(lineOfCode) == false && isTryStatement(lineOfCode) == false){
				//ADDING IFSTATEMENT TO LAST NODE, BEFORE CREATING A NEW NODE
				newNode.setThisNodeText(newNode.getThisNodeText() + nodeNumbering + ",");
				linesOfCode.add(lineOfCode);
			}
		}else{
			//WE WANT TO PREVENT ADDING THE DO WHILE STATEMENT TO TE NODE
			if(isDoWhileStatement(lineOfCode) == false && isTryStatement(lineOfCode) == false){
				newNode = new Node(nodeNumbering+",",identifier);
				linesOfCode.add(lineOfCode);
				nodeNumbering++;
				identifier++;//NEW NODE IDENTIFIER
			}
		}
		if(newNode != null){
			//IF THE PREVIOUS NODE IS A 'JOIN', IT'LL CREATE AN EDGE BETWEEN THE CURRENT NODE AND THE 'JOIN'
			this.createJoinEdge(currentNodes,newNode);

			//SINCE WE FOUND AN IF, WE NEED TO SAVE THE PREVIOUS NODE DATA
			newNode = this.closeNode(newNode, linesOfCode);
			//WE NEED TO SAVE THE LAST NODE IDENTIFIER HAD THE LAST NODE
			previousNodeIdentifier = newNode.getIdentifier();
			newEdge = this.createEdge(previousNodeIdentifier,identifier);
			currentNodes.add(newNode);
			currentEdges.add(newEdge);
			//SAVING ALL NODES TO THE GLOBAL LIST OF NODES
			this.setAllNodes(currentNodes);
			this.setAllEdges(currentEdges);
		}
		return previousNodeIdentifier;
	}

	private int postJoinNodeProcess(int previousNodeIdentifier, String statement) throws Exception{

		LinkedList<Node> currentNodes;
		LinkedList<Edge> currentEdges;
		int identifier = 0;
		int biggestIdentifier = 0;
		//UPDATING OUR LOCAL VARIABLES AFTER RECURSION
		currentNodes = this.getAllNodes();
		currentEdges = this.getAllEdges();

		createMissingEdges(currentNodes,previousNodeIdentifier,currentEdges);


		LinkedList<Integer> identifiersForJoinNode = null;
		
		int recursionCounter = 0;

		//WE WANT TO CREATE A JOIN FROM THE PARENT NODE IF THE CURRENT STATEMENT IS A FOR OR A WHILE LOOP
		if(!statement.equals("for") && !statement.equals("while")){
			identifiersForJoinNode = this.findNodesToLinkToJoin(previousNodeIdentifier,currentEdges,recursionCounter,new LinkedList<Edge>() );
		}else{
			identifiersForJoinNode = this.findNodesToLinkToJoin(previousNodeIdentifier,currentEdges,recursionCounter,new LinkedList<Edge>());
			biggestIdentifier = identifiersForJoinNode.getLast();
			identifiersForJoinNode = new LinkedList<Integer>();
		}



		if(statement.equals("try")){

			for(int z =(currentNodes.size()-1); z>0; z--){
				String firstLineOfNode = "";
				if(isJoinNode(currentNodes, z) == false){
					firstLineOfNode = currentNodes.get(z).getLinesOfCode().get(0);
				}

				if(!firstLineOfNode.equals("") && isCatchStatement(firstLineOfNode) == false){
					identifiersForJoinNode.add(currentNodes.get(z).getIdentifier());
					break;
				}
			}
		}
		//THIS SHOULD ONLY HAPPEN IN THE NODES TO BE LINKED IS ONLY ONE, THIS MEANS THAT THE NEXT SECOND EDGE TO THE JOIN SHOULD COME FROM THE PARENT NODE
		if(identifiersForJoinNode.size() <= 1 )
			identifiersForJoinNode.add(previousNodeIdentifier);

		//THIS STATEMENT WILL CREATE AN EDGE FROM THE LAST NODE OF THE LOOP, TO THE LOOP STATEMENT IFSELF TO REPRESENT THE RECURSION INSIDE A LOOP
		if(statement.equals("for") || statement.equals("while")){
			Edge newEdge = this.createEdge(currentNodes.getLast().getIdentifier(),previousNodeIdentifier);
			currentEdges.add(newEdge);
			this.setAllEdges(currentEdges);
		}
		//THIS STATEMENT WILL CREATE AN EDGE TO ITSELF
		if(statement.equals("do")){
			Edge newEdge = this.createEdge(currentNodes.getLast().getIdentifier(),currentNodes.getLast().getIdentifier());
			currentEdges.add(newEdge);
			this.setAllEdges(currentEdges);
		}

		if(this.areBreaksFound() == true){
			identifiersForJoinNode = this.includeBreakIdentifierNodes(identifiersForJoinNode,this.getBreakIdentifiers());

			this.setBreaksFound(false);
			this.setBreakIdentifiers(null);
		}

		//THIS METHOD WILL CREATE A JOIN NODE AND ADD IT TO THE LIST OF NODES
		//AFTER WE CREATE THE JOIN, THE METHOD RETURN THE NEXT IDENTIFIER AFTER RECURSION
		identifier = createJoinNode(identifiersForJoinNode,biggestIdentifier);

		return identifier;


	}


	private CodeBlockFeatures runBlockStatement(CodeBlockFeatures currentBlock, String statement) throws Exception{


		LinkedList<String> allStatements = null;

		if(statement.equals("for"))
			allStatements = currentBlock.getCurrentMethodTree().getForStatements();
		else if(statement.equals("while"))
			allStatements = currentBlock.getCurrentMethodTree().getWhileStatements();
		else if(statement.equals("if"))
			allStatements = currentBlock.getCurrentMethodTree().getIfStatements();
		else if(statement.equals("do"))
			allStatements = currentBlock.getCurrentMethodTree().getDoWhileStatements();
		else if(statement.equals("switch"))
			allStatements = currentBlock.getCurrentMethodTree().getSwitchStatements();
		else if(statement.equals("try"))
			allStatements = currentBlock.getCurrentMethodTree().getTryStatements();

		for(int x = 0; allStatements != null && x < allStatements.size();x++){
			//WE NEED TO COMPARE THE FIRST LINE OF EACH STORED IF INSIDE THE CURRENT METHOD TO GO DEEP INSIDE THE TREE
			String [] currentStatement = allStatements.get(x).split("\n");
			if(currentStatement[0].contains(currentBlock.getJavaContentData()[currentBlock.getBlockPointer()].trim())){

				//CLOSE PREVIOUS NODE BEFORE WORKING ON IFSTATEMENTS
				currentBlock.setPreviousNodeIdentifier(preJoinNodeProcess(currentBlock.getCurrentNode(), currentBlock.getCurrentNodes(), currentBlock.getCurrentEdge(), currentBlock.getCurrentEdges(), 
						currentBlock.getNodeNumbering(), currentBlock.getLinesOfCode(), currentBlock.getJavaContentData()[currentBlock.getBlockPointer()].trim(), 
						currentBlock.getPreviousNodeIdentifier(), currentBlock.getIdentifier()));
				

				currentBlock.setNodeNumbering(this.methodParseRecursion(allStatements.get(x), currentBlock.getCurrentMethodTree(),currentBlock.getIdentifier(),currentBlock.getNodeNumbering()+1,currentBlock.getPreviousNodeIdentifier()));
				currentBlock.setBlockPointer(currentBlock.getBlockPointer() + this.getLinesofCodeInsideRecursion());
				this.setLinesofCodeInsideRecursion(currentBlock.getJavaContentData().length);

				//ONCE DONE WITH THE INNER DATA FROM THIS IF, NOW WE NEED TO CREATE A JOIN NODE
				currentBlock.setIdentifier(postJoinNodeProcess(currentBlock.getPreviousNodeIdentifier(),statement));


				//UPDATING OUR LOCAL VARIABLES AFTER RECURSION
				currentBlock.setCurrentNodes(this.getAllNodes());
				currentBlock.setCurrentEdges(this.getAllEdges());




				currentBlock.setIdentifier(currentBlock.getIdentifier()+1);


				//SINCE WE SAVED THE DATA FROM PREVIOUS NODE, NOW WE NEED TO EMPTY THE NODE TO CREATE A NEW ONE
				currentBlock.setCurrentNode(null);
				currentBlock.setLinesOfCode(new LinkedList<String>());

				currentBlock.setBlockPointer(currentBlock.getBlockPointer()-1);
				break;
			}
		}

		return currentBlock;


	}
	/**
	 * THIS METHOD WILL RETURN A LIST OF NODES TO BE LINKED TO THE NEW CREATED JOIN NODE
	 * @param identifier INTEGER TO BE SEARCH IN ALLTHE EDGES ALREADY CREATED
	 * @param allEdges ALL THE EDGES THAT HAVE BEEN CREATED ALREADY
	 * @return LINKEDLIST CONTAINING ALL THE IDENTIFIERS TO BE LINKED TO THE NEW JOIN NODE
	 */
	private LinkedList<Integer> findNodesToLinkToJoin(int identifier,LinkedList<Edge> allEdges, int recursionCounter, LinkedList<Edge> previousTraverserEdges) throws Exception{
		LinkedList<Integer> nodesToBeLinked = new LinkedList<Integer>();
		LinkedList<Integer> nodesAddedInRecursion = new LinkedList<Integer>();

		if(recursionCounter == this.getStackOverFlowLimit())
			throw new Exception("WTF");
		else
			recursionCounter++;
		
		boolean sourceFound = false;
		for(int i = 0;i < allEdges.size();i++){
			if(Integer.parseInt(allEdges.get(i).getSource()) == identifier && isEdgeAlreadyVisited(previousTraverserEdges,allEdges.get(i).getSource(),allEdges.get(i).getTarget()) == false){
				sourceFound = true;
				previousTraverserEdges.add(allEdges.get(i));
				nodesAddedInRecursion = findNodesToLinkToJoin(Integer.parseInt(allEdges.get(i).getTarget()), allEdges,recursionCounter,previousTraverserEdges);
				for(int j = 0; j< nodesAddedInRecursion.size();j++){
					if(this.findIntegerInList(nodesToBeLinked,nodesAddedInRecursion.get(j)) == false)
						nodesToBeLinked.add(nodesAddedInRecursion.get(j));
				}
			}
		}
		//THIS MEANS THAN IF THE SOURCE IS NOT FOUND, THEN THIS IDENTIFIER HAS TO BE LINKED TO THE NEW JOIN TO BE CREATED
		if(sourceFound == false){
			nodesToBeLinked.add(identifier);
		}

		return nodesToBeLinked;

	}
	
	private boolean isEdgeAlreadyVisited(LinkedList<Edge> previousTraverserEdges,String source, String target){
		
		for(int i = 0;previousTraverserEdges != null && i <previousTraverserEdges.size();i++){
			if(source.equals(previousTraverserEdges.get(i).getSource()) && target.equals(previousTraverserEdges.get(i).getTarget()))
				return true;
		}
		return false;
	}


}
