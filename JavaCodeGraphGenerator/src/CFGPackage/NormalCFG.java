package CFGPackage;

import java.util.LinkedList;

import GraphElements.*;
import JavaParserPackage.JavaClassParser;

public class NormalCFG implements CFGInterface{
	private LinkedList<Node> allNodes;
	private LinkedList<Edge> allEdges;
	private int linesofCodeInsideRecursion;
	private boolean joinBeforeElseFound;
	private int totalPreviousAddedNodes;

	public NormalCFG(){
		allNodes = new LinkedList<Node>();
		allEdges = new LinkedList<Edge>();
		linesofCodeInsideRecursion = 0;
		joinBeforeElseFound = false;
		totalPreviousAddedNodes = 0;
	}

	/**
	 * @return the allNodes
	 */
	private LinkedList<Node> getAllNodes() {
		return allNodes;
	}



	/**
	 * @param allNodes the allNodes to set
	 */
	private void setAllNodes(LinkedList<Node> allNodes) {
		this.allNodes = allNodes;
	}



	/**
	 * @return the allEdges
	 */
	private LinkedList<Edge> getAllEdges() {
		return allEdges;
	}



	/**
	 * @param allEdges the allEdges to set
	 */
	private void setAllEdges(LinkedList<Edge> allEdges) {
		this.allEdges = allEdges;
	}


	/**
	 * @return the linesofCodeInsideRecursion
	 */
	public int getLinesofCodeInsideRecursion() {
		return linesofCodeInsideRecursion;
	}

	/**
	 * @param linesofCodeInsideRecursion the linesofCodeInsideRecursion to set
	 */
	private void setLinesofCodeInsideRecursion(int linesofCodeInsideRecursion) {
		this.linesofCodeInsideRecursion = linesofCodeInsideRecursion;
	}

	/**
	 * @return the joinBeforeElseFound
	 */
	private boolean isJoinBeforeElseFound() {
		return joinBeforeElseFound;
	}

	/**
	 * @param joinBeforeElseFound the joinBeforeElseFound to set
	 */
	private void setJoinBeforeElseFound(boolean joinBeforeElseFound) {
		this.joinBeforeElseFound = joinBeforeElseFound;
	}

	/**
	 * @return the totalPreviousAddedNodes
	 */
	private int getTotalPreviousAddedNodes() {
		return totalPreviousAddedNodes;
	}

	/**
	 * @param totalPreviousAddedNodes the totalPreviousAddedNodes to set
	 */
	private void setTotalPreviousAddedNodes(int totalPreviousAddedNodes) {
		this.totalPreviousAddedNodes = totalPreviousAddedNodes;
	}

	public void generateGraph(LinkedList<String> methods) {

		JavaClassParser methodParser = new JavaClassParser();
		for(int methodCounter = 0;methodCounter < methods.size(); methodCounter++){
			MethodTree currentMethodTree = new MethodTree();
			//WE ARE CHEATING HERE, ASTPARSER NEEDS THE CLASS 
			//INICIALIZATION TO PARSER ANYTHING INSIDE THE METHODS(I KNOW IT'S WEIRD)
			String methodToParse = "public class thisClass{ \n" + methods.get(methodCounter) + "\n }";
			LinkedList<String> currentMethodIfStatements = methodParser.IfStatementParser(methodToParse);
			currentMethodTree.setIfStaments(currentMethodIfStatements);
			int initialIdentifier = 1;
			int initialNodeNumbering = 1;
			int previousNodeIdentifier = 1;
			this.methodParseRecursion(methods.get(methodCounter), currentMethodTree,initialIdentifier,initialNodeNumbering,previousNodeIdentifier);
			if(this.islastNodeJoin() == true){
				this.removeLastJoin();
			}

		}
		printNodeList(this.getAllNodes());
		printEdgeList(this.getAllEdges());

	}

	private int methodParseRecursion(String currentSectionOfCode,MethodTree currentMethodTree, int identifier,int nodeNumbering,int previousNodeIdentifier){
		LinkedList<Node> currentNodes = this.getAllNodes();
		LinkedList<Edge> currentEdges = this.getAllEdges();
		Node newNode = null;
		Edge newEdge = null;
		LinkedList<String> linesOfCode = new LinkedList<String>();

		String[] javaContentData = currentSectionOfCode.split("\n");
		if(javaContentData != null)
			this.setLinesofCodeInsideRecursion(javaContentData.length);
		else
			this.setLinesofCodeInsideRecursion(0);
		for(int i = 0; i < javaContentData.length;i++){
			if(isMethodCall(javaContentData[i]) == true){
				//DO NOTHING
			}else if(isIfStatement(javaContentData[i]) == true && i != 0){//FOUND AN IF, AND i CURRENT LINE IS NOT THE SAME IF WE'RE WORKING ON
				LinkedList<String> allIfStatements = currentMethodTree.getIfStaments();

				for(int x = 0; x < allIfStatements.size();x++){
					//WE NEED TO COMPARE THE FIRST LINE OF EACH STORED IF INSIDE THE CURRENT METHOD TO GO DEEP INSIDE THE TREE
					String [] currentIfStatement = allIfStatements.get(x).split("\n");
					if(currentIfStatement[0].contains(javaContentData[i].trim()) ){
						//ADDING IFSTATEMENT TO LAST NODE, BEFORE CREATING A NEW NODE
						newNode.setThisNodeText(newNode.getThisNodeText() + nodeNumbering + ",");
						linesOfCode.add(javaContentData[i].trim());

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

						int totalNodesAdded = currentNodes.size();
						nodeNumbering = this.methodParseRecursion(allIfStatements.get(x), currentMethodTree,identifier,nodeNumbering+1,previousNodeIdentifier);
						i = i + this.getLinesofCodeInsideRecursion();
						this.setLinesofCodeInsideRecursion(javaContentData.length);

						createMissingEdges(currentNodes,previousNodeIdentifier,currentEdges);

						//UPDATING OUR LOCAL VARIABLES AFTER RECURSION
						currentNodes = this.getAllNodes();
						currentEdges = this.getAllEdges();

						LinkedList<Integer> identifiersForJoinNode = this.findNodesToLinkToJoin(previousNodeIdentifier,currentEdges);

						//THIS SHOULD ONLY HAPPEN IN THE NODES TO BE LINKED IS ONLY ONE, THIS MEANS THAT THE NEXT SECOND EDGE TO TTHE JOIN SHOULD COME FROM THE PARENT NODE
						if(identifiersForJoinNode.size() == 1)
							identifiersForJoinNode.add(previousNodeIdentifier);
						totalNodesAdded = currentNodes.size() - totalNodesAdded;

						//THIS VARIABLE WILL BE NEEDED AS THE TREE GETS BIGGER FOR BETTER DISTRIBUTION OF NODES
						//this.setTotalPreviousAddedNodes(totalNodesAdded);



						/*
						if(this.isJoinBeforeElseFound() == true){
							for(int j = totalNodesAdded;j<currentNodes.size();j++){
								newEdge = this.createEdge(previousNodeIdentifier,currentNodes.get(j).getIdentifier());
								currentEdges.add(newEdge);
								this.setAllEdges(currentEdges);

							}
							totalNodesAdded--;//WE WANT TO CREATE AN EDGE FOR THE EXTRA JOIN FOUND
							this.setJoinBeforeElseFound(false);
						}
						 */

						//THIS METHOD WILL CREATE A JOIN NODE AND ADD IT TO THE LIST OF NODES
						//AFTER WE CREATE THE JOIN, THE METHOD RETURN THE NEXT IDENTIFIER AFTER RECURSION
						identifier = createJoinNode(identifiersForJoinNode);




						identifier++;

						//SINCE WE SAVED THE DATA FROM PREVIOUS NODE, NOW WE NEED TO EMPTY THE NODE TO CREATE A NEW ONE
						newNode = null;
						linesOfCode = new LinkedList<String>();

						i--;
						break;
					}
				}

			}//THIS WILL ONLY HAPPEN WHEN WORKING ON N IF STATEMENT AND THE FIRST PORTION OF DATA HAS BEEN ALREADY TO THE PREVIOUS NODE
			else if(isIfStatement(javaContentData[i]) == true && i == 0){
				//DO NOTHING
			}
			else  if(javaContentData[i].trim().equals("{") || javaContentData[i].trim().equals("}")){
				//DO NOTHING
			}
			else{
				//IF newNode IS NULL, THIS MEANS WE NEED TO CREATE A NEW NODE
				if(newNode == null && isElseStatement(javaContentData[i].trim()) == false){
					newNode = new Node(nodeNumbering+",",identifier);
					linesOfCode.add(javaContentData[i].trim());
					nodeNumbering++;
					identifier++;//NEW NODE IDENTIFIER
				}
				else{

					if(isElseStatement(javaContentData[i].trim()) == true){
						if(newNode != null){//DOUBLE MAKING SURE THAT IF THE NODE IS NULL OR NOT
							//IF THE PREVIOUS NODE IS A 'JOIN', IT'LL CREATE AN EDGE BETWEEN THE CURRENT NODE AND THE 'JOIN'
							if(this.isJoinBeforeElseFound() == false){//THIS WILL PREVENT FROM CHILD NODES TO CONNECT TO PARENT NODES 
								//this.createJoinEdge(currentNodes,newNode);//THIS WILL PREVENT FROM CHILD NODES TO CONNECT TO PARENT NODES 		
								newEdge = this.createEdge(previousNodeIdentifier,identifier);
								currentEdges.add(newEdge);
							}
							newNode = this.closeNode(newNode, linesOfCode);
							currentNodes.add(newNode);

							this.setAllNodes(currentNodes);
							this.setAllEdges(currentEdges);
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

		}
		//IF WE GOT THIS FAR, THIS MEANS THAT THIS IS THE LAST LINE IN NODE FOR THIS PIECE OF CODE
		if(newNode != null){

			//IF THE PREVIOUS NODE IS A 'JOIN', IT'LL CREATE AN EDGE BETWEEN THE CURRENT NODE AND THE 'JOIN'
			//if(this.isJoinBeforeElseFound() == false)//THIS WILL PREVENT FROM CHILD NODES TO CONNECT TO PARENT NODES 
			//this.createJoinEdge(currentNodes,newNode);

			newNode = this.closeNode(newNode, linesOfCode);
			currentNodes.add(newNode);
			this.setAllNodes(currentNodes);
		}

		return nodeNumbering;

	}

	private boolean isMethodCall(String codeLine){

		if(codeLine.toLowerCase().contains("public") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")"))
			return true;
		if(codeLine.toLowerCase().contains("protected") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")"))
			return true;
		if(codeLine.toLowerCase().contains("private") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")"))
			return true;

		return false;
	}

	private boolean isIfStatement(String codeLine){

		if(codeLine.toLowerCase().contains("if") && codeLine.toLowerCase().contains("(") 
				&& codeLine.toLowerCase().contains(")") 
				&& !codeLine.toLowerCase().contains("else")
				&& !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	private boolean isElseStatement(String codeLine){

		if(codeLine.toLowerCase().contains("else ") && codeLine.toLowerCase().contains("if") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		if(codeLine.toLowerCase().contains("else ") && codeLine.toLowerCase().contains("if") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		if(codeLine.toLowerCase().contains("else ") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	private void printNodeList(LinkedList<Node> nodes){
		System.out.println("");
		System.out.println("PRINTING ALL NODES");
		System.out.println("");
		for(int i = 0;i<nodes.size();i++){
			System.out.println("--------------------------------------");
			System.out.println("Node: " + nodes.get(i).getThisNodeText());
			System.out.println("Identifier: " + nodes.get(i).getIdentifier());
			System.out.println("Codelines: ");
			for(int x = 0; x< nodes.get(i).getLinesOfCode().size();x++){
				LinkedList<String> linesOfCode = nodes.get(i).getLinesOfCode();
				System.out.println("\t"+linesOfCode.get(x));

			}
		}

	}

	private void printEdgeList(LinkedList<Edge> edges){
		System.out.println("");
		System.out.println("PRINTING ALL EDGES");
		System.out.println("");
		for(int i = 0;i<edges.size();i++){
			System.out.println("--------------------------------------");
			System.out.println("Edge: ");
			System.out.println("Identifier Source: " + edges.get(i).getSource());
			System.out.println("Identifier Target: " + edges.get(i).getTarget());
		}

	}

	private Node closeNode(Node thisNode,LinkedList<String> linesOfCode){
		thisNode.setLinesOfCode(linesOfCode);
		String fixedNodeText = thisNode.getThisNodeText();
		fixedNodeText = fixedNodeText.substring(0,fixedNodeText.length()-1);//WE HAVE AN EXTRA ',' THIS SHOLD REMOVE IT
		thisNode.setThisNodeText(fixedNodeText);

		return thisNode;
	}

	private Edge createEdge(int source,int target){
		Edge newEdge = new Edge(source+"",target+"");
		return newEdge;

	}

	private int createJoinNode(LinkedList<Integer> identifiersForJoinNode){
		//THIS PART IS VERY IMPORTANT, SINCE WE ARE BACK FROM RECURSION, WE NEED TO SKIP THE LINES WE ALREADY TRAVERSED, 
		//TO KEEP THE NUMBERING CORRECTLY
		//WE ALSO NEED TO CREATE A JOIN NODE

		int previousNodeIdentifier = 0;
		int biggestIdentifier = 0;
		LinkedList<Node> nodesAfterRecursion = this.getAllNodes();

		if(identifiersForJoinNode.size() > 0 ){
			for(int j = 0; j<identifiersForJoinNode.size();j++){
				previousNodeIdentifier = identifiersForJoinNode.get(j);
				Edge newEdge = this.createEdge(previousNodeIdentifier, nodesAfterRecursion.size()+1);
				this.allEdges.add(newEdge);
				if(previousNodeIdentifier > biggestIdentifier)
					biggestIdentifier = previousNodeIdentifier;
			}
			Node newNode = new Node("Join",(biggestIdentifier+1));
			this.allNodes.add(newNode);//ADDING 'JOIN' NODE TO THE GLOBAL List
		}

		return (biggestIdentifier+1);
	}

	private void createJoinEdge(LinkedList<Node> currentNodes, Node newNode){
		if(currentNodes.size() > 1 && currentNodes.get(currentNodes.size()-1).getThisNodeText().toLowerCase().equals("join")){
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

		if(allNodes.get(allNodes.size()-1).getThisNodeText().toLowerCase().equals("join"))
			return true;
		return false;
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

	/**
	 * THIS METHOD WILL RETURN A LIST OF NODES TO BE LINKED TO THE NEW CREATED JOIN NODE
	 * @param identifier INTEGER TO BE SEARCH IN ALLTHE EDGES ALREADY CREATED
	 * @param allEdges ALL THE EDGES THAT HAVE BEEN CREATED ALREADY
	 * @return LINKEDLIST CONTAINING ALL THE IDENTIFIERS TO BE LINKED TO THE NEW JOIN NODE
	 */
	private LinkedList<Integer> findNodesToLinkToJoin(int identifier,LinkedList<Edge> allEdges){

		LinkedList<Integer> nodesToBeLinked = new LinkedList<Integer>();
		LinkedList<Integer> nodesAddedInRecursion = new LinkedList<Integer>();

		boolean sourceFound = false;
		for(int i = 0;i < allEdges.size();i++){
			if(Integer.parseInt(allEdges.get(i).getSource()) == identifier){
				sourceFound = true;
				nodesAddedInRecursion = findNodesToLinkToJoin(Integer.parseInt(allEdges.get(i).getTarget()), allEdges);
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

			if(foundTarget == false){
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

}
