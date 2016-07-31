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
		LinkedList<Statement> methods = new LinkedList<Statement>();
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
				String methodToParse = "public class thisClass{ \n" + methods.get(methodCounter).getBlockStatement() + "\n }";
				currentMethodTree = getAllStamentsInsideMethod(methodParser, methodToParse);

				this.methodSkipEdges.add(initialIdentifier);
				initialNodeNumbering = this.methodParseRecursion(methods.get(methodCounter).getBlockStatement(), currentMethodTree,initialIdentifier,initialNodeNumbering,previousNodeIdentifier);
				previousNodeIdentifier = this.getAllNodes().getLast().getIdentifier()+1;
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
			System.out.println(javaContentData[i]);
			if(isSkipLine(javaContentData[i],i) == true){

				//EVEN THAT WE DO NOTHING FOR MOST OF THE STAMENTS, WE NEED TO DO THESE EXCEPTIONS IF THE LINE IS ONE OF THE FOLLOWING
				String statement = getBlockStatementType(javaContentData[i]);

				if(statement != null && statement.equals("if")){
					ifFound = true;
					lastIfStatementFoundIdentifier = previousNodeIdentifier;
				}
				if(statement != null && statement.equals("try")){
					tryIdentifiers = new LinkedList<Integer>();
				}
			}
			else if(isBlockStatement(javaContentData[i], i) == true){
				String statement = getBlockStatementType(javaContentData[i]);
				if(statement != null && statement.equals("if")){
					ifFound = true;
					lastIfStatementFoundIdentifier = previousNodeIdentifier;
				}
				currentBlock = new CodeBlockFeatures(currentMethodTree, previousNodeIdentifier, newNode, newEdge, currentNodes, currentEdges, nodeNumbering, i, linesOfCode, javaContentData, identifier);
				currentBlock = this.runBlockStatement(currentBlock,statement);


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
				newNode = this.createNode(nodeNumbering,identifier);
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

					newNode = this.createNode(nodeNumbering,identifier);
					linesOfCode.add(javaContentData[i].trim());
					nodeNumbering++;
					identifier++;//NEW NODE IDENTIFIER


					catchIdentifiers.add(newNode.getIdentifier());
				}
			}

			else{
				//IF newNode IS NULL, THIS MEANS WE NEED TO CREATE A NEW NODE
				if(newNode == null && isElseStatement(javaContentData[i].trim()) == false){
					newNode = this.createNode(nodeNumbering,identifier);
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
							newNode = this.createNode(nodeNumbering,identifier);
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
				if(currentBreaksFound == null)
					currentBreaksFound = new LinkedList<Integer>();
				currentBreaksFound.add(newNode.getIdentifier());
				this.setBreakIdentifiers(currentBreaksFound);
			}

			if(currentBlock != null){

				currentMethodTree = currentBlock.getCurrentMethodTree();

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


				if(tryIdentifiers != null && tryIdentifiers.size() > 0 && catchIdentifiers == null){


					int identifierDifference = currentNodes.getLast().getIdentifier() - currentNodes.size();

					//TIME TO ADD INNER NODES TO THE 
					for(int z = (previousNodeIdentifier - identifierDifference);z < (currentNodes.getLast().getIdentifier() - identifierDifference);z++){
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
		if(tryIdentifiers != null && catchIdentifiers != null ){
			int tryJoinNode = this.createJoinNode(tryIdentifiers, identifier);
			currentEdges = this.getAllEdges();

			for(int x = 0;x < catchIdentifiers.size(); x++){
				newEdge = this.createEdge( tryJoinNode ,catchIdentifiers.get(x) );
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
				newNode = this.createNode(nodeNumbering,identifier);
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
			if(identifiersForJoinNode != null && identifiersForJoinNode.size()>0)
				biggestIdentifier = identifiersForJoinNode.getLast();
			else
				biggestIdentifier = 0;
			identifiersForJoinNode = new LinkedList<Integer>();
		}



		if(statement.equals("try")){

			for(int z =(currentNodes.size()-1); z>0; z--){
				String firstLineOfNode = "";
				if(isJoinNode(currentNodes,currentNodes.get(z).getIdentifier()) == false){
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

		if(doesNodeExist(identifier, currentNodes) == true){
			identifier = this.getHighestIdentifier(identifier, currentNodes);
		}

		return identifier;


	}


	private CodeBlockFeatures runBlockStatement(CodeBlockFeatures currentBlock, String statement) throws Exception{


		LinkedList<Statement> allStatements = getTypeStatements(statement, currentBlock);

		for(int x = 0; allStatements != null && x < allStatements.size();x++){
			//WE NEED TO COMPARE THE FIRST LINE OF EACH STORED IF INSIDE THE CURRENT METHOD TO GO DEEP INSIDE THE TREE
			String [] currentStatement = allStatements.get(x).getBlockStatement().split("\n");
			if(allStatements.get(x).isTraversed() == false && currentStatement[0].contains(currentBlock.getJavaContentData()[currentBlock.getBlockPointer()].trim())){

				allStatements.get(x).setTraversed(true);

				currentBlock.setCurrentMethodTree(updateMethodTree(statement, currentBlock, allStatements));

				//CLOSE PREVIOUS NODE BEFORE WORKING ON IFSTATEMENTS
				currentBlock.setPreviousNodeIdentifier(preJoinNodeProcess(currentBlock.getCurrentNode(), currentBlock.getCurrentNodes(), currentBlock.getCurrentEdge(), currentBlock.getCurrentEdges(), 
						currentBlock.getNodeNumbering(), currentBlock.getLinesOfCode(), currentBlock.getJavaContentData()[currentBlock.getBlockPointer()].trim(), 
						currentBlock.getPreviousNodeIdentifier(), currentBlock.getIdentifier()));


				currentBlock.setNodeNumbering(this.methodParseRecursion(allStatements.get(x).getBlockStatement(), currentBlock.getCurrentMethodTree(),currentBlock.getIdentifier(),currentBlock.getNodeNumbering()+1,currentBlock.getPreviousNodeIdentifier()));
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
		if(sourceFound == false && doesNodeExist(identifier, this.getAllNodes()) == true){
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

	private boolean isBlockStatement(String codeLine, int pointer){
		//CHECKING IF CURRENT LINE IS AN IF STATEMENT
		if(isIfStatement(codeLine.trim()) == true && pointer != 0){//FOUND AN IF, AND i CURRENT LINE IS NOT THE SAME IF WE'RE WORKING ON
			return true;
		}
		//CHECKING IF CURRENT LINE IS A FOR STATEMENT
		if(isForStatement(codeLine.trim()) == true && pointer != 0){
			return true;
		}
		//CHECKING IF CURRENT LINE IS A WHILE STATEMENT
		if(isWhileStatement(codeLine.trim()) == true && pointer != 0){
			return true;
		}
		//CHECKING IF CURRENT LINE IS A DO WHILE STATEMENT
		if(isDoWhileStatement(codeLine.trim()) == true && pointer != 0){
			return true;
		}
		//CHECKING IF CURRENT LINE IS A SWITCH STATEMENT
		if(isSwitchStatement(codeLine.trim()) == true && pointer != 0){
			return true;
		}
		//CHECKING IF CURRENT LINE IS A SWITCH STATEMENT
		if(isTryStatement(codeLine) == true && pointer != 0){
			return true;
		}
		return false;
	}

	private String getBlockStatementType(String codeLine){
		//CHECKING IF CURRENT LINE IS AN IF STATEMENT
		if(isIfStatement(codeLine.trim()) == true ){
			return "if";
		}
		//CHECKING IF CURRENT LINE IS A FOR STATEMENT
		if(isForStatement(codeLine.trim()) == true ){
			return "for";
		}
		//CHECKING IF CURRENT LINE IS A WHILE STATEMENT
		if(isWhileStatement(codeLine.trim()) == true ){
			return "while";
		}
		//CHECKING IF CURRENT LINE IS A DO WHILE STATEMENT
		if(isDoWhileStatement(codeLine.trim()) == true){
			return "do";
		}
		//CHECKING IF CURRENT LINE IS A SWITCH STATEMENT
		if(isSwitchStatement(codeLine.trim()) == true){
			return "switch";
		}
		//CHECKING IF CURRENT LINE IS A SWITCH STATEMENT
		if(isTryStatement(codeLine) == true ){
			return "try";
		}
		return null;
	}

	private MethodTree getAllStamentsInsideMethod(JavaClassParser methodParser, String methodToParse){

		MethodTree currentMethodTree = new MethodTree();
		LinkedList<Statement> currentMethodStatements = methodParser.ifStatementParser(methodToParse);
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

		return currentMethodTree;
	}


	/**
	 * THIS METHOD WILL BE TRUE IF THE lastNodeText EQUALS TO 'join' 
	 * 
	 * @param lastNodeText text containing the the lastNodeText
	 * @return
	 */
	private boolean findJoinBeforeElseStatement(String lastNodeText){

		if(lastNodeText.toLowerCase().equals("join"))
			return true;

		return false;

	}





	private void createJoinEdge(LinkedList<Node> currentNodes, Node newNode){
		if(currentNodes.size() > 1 && currentNodes.getLast().getThisNodeText().toLowerCase().equals("join")){
			Edge newEdge = this.createEdge(currentNodes.get(currentNodes.size()-1).getIdentifier(),newNode.getIdentifier());
			this.allEdges.add(newEdge);
		}
	}

	/**
	 * THIS METHOD WILL FIND IF THE LAST NODE OF THE TREE IS A 'JOIN' NODE, WE DON'T NEED THESE KIND OF CASES
	 * @return boolean: true or false
	 */
	private boolean islastNodeJoin(){
		LinkedList<Node> allNodes = this.getAllNodes();

		if(allNodes.size()>0 && allNodes.get(allNodes.size()-1).getThisNodeText().toLowerCase().equals("join"))
			return true;
		return false;
	}

	private int createJoinNode(LinkedList<Integer> identifiersForJoinNode,int biggestIdentifier){
		//THIS PART IS VERY IMPORTANT, SINCE WE ARE BACK FROM RECURSION, WE NEED TO SKIP THE LINES WE ALREADY TRAVERSED, 
		//TO KEEP THE NUMBERING CORRECTLY
		//WE ALSO NEED TO CREATE A JOIN NODE

		int previousNodeIdentifier = 0;

		if(identifiersForJoinNode.size() > 0 ){
			for(int j = 0; j<identifiersForJoinNode.size();j++){
				previousNodeIdentifier = identifiersForJoinNode.get(j);
				if(previousNodeIdentifier > biggestIdentifier)
					biggestIdentifier = previousNodeIdentifier;
			}
			Node newNode = new Node("Join",(biggestIdentifier+1));
			this.allNodes.add(newNode);//ADDING 'JOIN' NODE TO THE GLOBAL List

			for(int i = 0; i<identifiersForJoinNode.size();i++){
				previousNodeIdentifier = identifiersForJoinNode.get(i);
				//if(isJoinNode(this.getAllNodes(), previousNodeIdentifier) == false){
				if(doesNodeExist(previousNodeIdentifier, this.getAllNodes()) == true){
					Edge newEdge = this.createEdge(previousNodeIdentifier, newNode.getIdentifier());
					this.allEdges.add(newEdge);
				}
				//}
			}
		}

		return (biggestIdentifier+1);
	}



	/**
	 * IF FOR SOME REASON A JOIN GETS CREATED AT THE END, THIS WILL REMOVE THE JOIN AND ANY EDGE THAT CONNECTS TO IT
	 */
	private void removeLastJoin(){
		LinkedList<Node> allNodes = this.getAllNodes();
		String joinIdentifier = allNodes.get(allNodes.size()-1).getIdentifier() + "";
		LinkedList<Edge> allEdges = this.getAllEdges();


		for(int i = 0; i < allEdges.size();i++){
			if(joinIdentifier.equals(allEdges.get(i).getTarget())){
				allEdges.remove(i);
				i = 0;
			}

		}
		this.setAllEdges(allEdges);

		allNodes.remove(allNodes.size()-1);
		this.setAllNodes(allNodes);

	}






	private void createMissingEdges(LinkedList<Node> allNodes,int previousNodeIdentifier,LinkedList<Edge> allEdges){

		int nodeIdentifier = 0;
		boolean foundTarget = false;
		Edge newEdge = null;
		for(int i = 1; i < allNodes.size();i++){
			nodeIdentifier = allNodes.get(i).getIdentifier();
			foundTarget = false;
			for(int j = 0; j < allEdges.size(); j++){
				if(Integer.parseInt(allEdges.get(j).getTarget()) == nodeIdentifier)
					foundTarget = true;
			}

			if(foundTarget == false && previousNodeIdentifier < nodeIdentifier && doesNodeExist(previousNodeIdentifier, allNodes) == true){
				newEdge = this.createEdge(previousNodeIdentifier, nodeIdentifier);
				allEdges.add(newEdge);
				this.setAllEdges(allEdges);
			}
		}

	}

	private boolean findIntegerInList(LinkedList<Integer> list, int number){

		for(int i=0;i<list.size();i++){
			if(number == list.get(i))
				return true;
		}
		return false;
	}

	private LinkedList<Integer> includeBreakIdentifierNodes(LinkedList<Integer> identifiersForJoinNode,LinkedList<Integer> breakIdentifiers){
		boolean identifierFound = false;
		for(int i = 0; i< breakIdentifiers.size();i++){
			identifierFound = false;
			for(int j = 0; j< identifiersForJoinNode.size(); j++){
				if(breakIdentifiers.get(i) == identifiersForJoinNode.get(j))
					identifierFound = true;
			}
			if(identifierFound == false)
				identifiersForJoinNode.add(breakIdentifiers.get(i));
		}

		return identifiersForJoinNode;
	}

	/**
	 * THIS METHOD WILL RETURN TRUE IF THE JAVA CONTENT SECTION EQUALS TO THE FULL METHOD PORTION
	 * @param javaContentData THE CURRENT JAVA CONTENT DATA, THIS CAN BE AN IF, FOR, BUT WE ARE LOOKING FOR THIS TO BE THE WHOLE METHOD SECTION
	 * @return TRUE OR FALSE
	 */
	private boolean isThisLastNode(String[] javaContentData){

		if(javaContentData != null && this.isMethodCall(javaContentData[0]) == true)
			return true;
		return false;
	}

	private boolean isFirstNodeOfMethod(int nodeIdentifier){
		LinkedList<Integer> allMethodSkipEdges = this.getMethodSkipEdges();

		for(int i = 0; i< allMethodSkipEdges.size();i++){
			if(nodeIdentifier == allMethodSkipEdges.get(i))
				return true;
		}
		return false;

	}
	private boolean findIdentifierInNodes(LinkedList<Node> allNodes, int identifier){
		for(int i=0;i< allNodes.size();i++){
			if(identifier == allNodes.get(i).getIdentifier()){
				return true;
			}
		}
		return false;
	}

	private String getTextFromNode(LinkedList<Node> allNodes,int identifier){
		for(int i=0;i< allNodes.size();i++){
			if(identifier == allNodes.get(i).getIdentifier()){
				return allNodes.get(i).getThisNodeText();
			}
		}
		return "";
	}
	private LinkedList<String> getLinesOfCodeFromNode(LinkedList<Node> allNodes,int identifier){
		for(int i=0;i< allNodes.size();i++){
			if(identifier == allNodes.get(i).getIdentifier()){
				return allNodes.get(i).getLinesOfCode();
			}
		}
		return null;
	}

	private boolean isJoinNode(LinkedList<Node> allNodes,int identifier){

		String nodeText = getTextFromNode(allNodes,identifier);
		if(nodeText.toLowerCase().equals("join"))
			return true;
		return false;
	}

	private int getIdentifierFromText(LinkedList<Node> allNodes,String codeLine){

		for(int i = (allNodes.size()-1); i > 0;i--){
			LinkedList<String> linesOfCodePerNode = allNodes.get(i).getLinesOfCode();
			for(int x = 0;linesOfCodePerNode != null && x< linesOfCodePerNode.size();x++){
				if(codeLine.equals(linesOfCodePerNode.get(x)))
					return allNodes.get(i).getIdentifier();
			}
		}
		return 0;
	}

	private LinkedList<Statement> getTypeStatements(String statement,CodeBlockFeatures currentBlock){

		LinkedList<Statement> allStatements = null;

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

		return allStatements;
	}

	private MethodTree updateMethodTree(String statement,CodeBlockFeatures currentBlock,LinkedList<Statement> newUpdatedStatements){

		MethodTree newMethodTree = currentBlock.getCurrentMethodTree();

		if(statement.equals("for"))
			newMethodTree.setForStatements(newUpdatedStatements);
		else if(statement.equals("while"))
			newMethodTree.setWhileStatements(newUpdatedStatements);
		else if(statement.equals("if"))
			newMethodTree.setIfStatements(newUpdatedStatements);
		else if(statement.equals("do"))
			newMethodTree.setDoWhileStatements(newUpdatedStatements);
		else if(statement.equals("switch"))
			newMethodTree.setSwitchStatements(newUpdatedStatements);
		else if(statement.equals("try"))
			newMethodTree.setTryStatements(newUpdatedStatements);


		return newMethodTree;



	}

	private boolean doesNodeExist(int identifier,LinkedList<Node> allNodes){

		for(int i = 0;i<allNodes.size();i++){
			if(identifier == allNodes.get(i).getIdentifier())
				return true;
		}

		return false;
	}

	private int getHighestIdentifier(int currentIdentifier,LinkedList<Node> allNodes){
		int highestIdentifier = currentIdentifier;

		for(int i=0;i< allNodes.size();i++){
			if(highestIdentifier < allNodes.get(i).getIdentifier())
				highestIdentifier = allNodes.get(i).getIdentifier();
		}

		return highestIdentifier;
	}


}
