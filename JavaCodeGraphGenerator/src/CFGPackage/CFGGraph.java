package CFGPackage;

import java.util.LinkedList;

import GraphElements.Edge;
import GraphElements.Node;

public abstract class CFGGraph {

	protected LinkedList<Node> allNodes;
	protected LinkedList<Edge> allEdges;
	protected int linesofCodeInsideRecursion;
	protected boolean joinBeforeElseFound;
	protected int totalNumberOfNodesAfterEachGraph;
	protected boolean breaksFound;
	protected LinkedList<Integer> breakIdentifiers;
	protected LinkedList<Integer> methodSkipEdges;


	/**
	 * @return the allNodes
	 */
	public LinkedList<Node> getAllNodes() {
		return allNodes;
	}



	/**
	 * @param allNodes the allNodes to set
	 */
	protected void setAllNodes(LinkedList<Node> allNodes) {
		this.allNodes = allNodes;
	}



	/**
	 * @return the allEdges
	 */
	public LinkedList<Edge> getAllEdges() {
		return allEdges;
	}



	/**
	 * @param allEdges the allEdges to set
	 */
	protected void setAllEdges(LinkedList<Edge> allEdges) {
		this.allEdges = allEdges;
	}


	/**
	 * @return the linesofCodeInsideRecursion
	 */
	protected int getLinesofCodeInsideRecursion() {
		return linesofCodeInsideRecursion;
	}

	/**
	 * @param linesofCodeInsideRecursion the linesofCodeInsideRecursion to set
	 */
	protected void setLinesofCodeInsideRecursion(int linesofCodeInsideRecursion) {
		this.linesofCodeInsideRecursion = linesofCodeInsideRecursion;
	}

	/**
	 * @return the joinBeforeElseFound
	 */
	protected boolean isJoinBeforeElseFound() {
		return joinBeforeElseFound;
	}

	/**
	 * @param joinBeforeElseFound the joinBeforeElseFound to set
	 */
	protected void setJoinBeforeElseFound(boolean joinBeforeElseFound) {
		this.joinBeforeElseFound = joinBeforeElseFound;
	}


	/**
	 * @return the totalNumberOfNodesAfterEachGraph
	 */
	protected int getTotalNumberOfNodesAfterEachGraph() {
		return totalNumberOfNodesAfterEachGraph;
	}

	/**
	 * @param totalNumberOfNodesAfterEachGraph the totalNumberOfNodesAfterEachGraph to set
	 */
	protected void setTotalNumberOfNodesAfterEachGraph(
			int totalNumberOfNodesAfterEachGraph) {
		this.totalNumberOfNodesAfterEachGraph = totalNumberOfNodesAfterEachGraph;
	}

	/**
	 * @return the breaksFound
	 */
	protected boolean areBreaksFound() {
		return breaksFound;
	}

	/**
	 * @param breaksFound the breaksFound to set
	 */
	protected void setBreaksFound(boolean breaksFound) {
		this.breaksFound = breaksFound;
	}

	/**
	 * @return the breakIdentifiers
	 */
	protected LinkedList<Integer> getBreakIdentifiers() {
		return breakIdentifiers;
	}

	/**
	 * @param breakIdentifiers the breakIdentifiers to set
	 */
	protected void setBreakIdentifiers(LinkedList<Integer> breakIdentifiers) {
		this.breakIdentifiers = breakIdentifiers;
	}


	/**
	 * @return the methodSkipEdges
	 */
	protected LinkedList<Integer> getMethodSkipEdges() {
		return methodSkipEdges;
	}



	/**
	 * @param methodSkipEdges the methodSkipEdges to set
	 */
	protected void setMethodSkipEdges(LinkedList<Integer> methodSkipEdges) {
		this.methodSkipEdges = methodSkipEdges;
	}



	public void generateGraph(LinkedList<String> methods){

	}

	/**
	 * THIS METHOD WILL BE TRUE IF THE lastNodeText EQUALS TO 'join' 
	 * 
	 * @param lastNodeText text containing the the lastNodeText
	 * @return
	 */
	protected boolean findJoinBeforeElseStatement(String lastNodeText){

		if(lastNodeText.toLowerCase().equals("join"))
			return true;

		return false;

	}

	protected boolean isMethodCall(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("public") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")"))
			return true;
		if(codeLine.toLowerCase().contains("protected") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")"))
			return true;
		if(codeLine.toLowerCase().contains("private") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")"))
			return true;

		return false;
	}

	protected boolean isIfStatement(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("if") && codeLine.toLowerCase().contains("(") 
				&& codeLine.toLowerCase().contains(")") 
				&& !codeLine.toLowerCase().contains("else")
				&& !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected boolean isElseStatement(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("else ") && codeLine.toLowerCase().contains("if") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		if(codeLine.toLowerCase().contains("else ") && codeLine.toLowerCase().contains("if") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		if(codeLine.toLowerCase().contains("else ") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected boolean isForStatement(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("for") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected boolean isWhileStatement(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("while") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected boolean isDoWhileStatement(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("do") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("while") && !codeLine.toLowerCase().contains("system.out") )
			return true;
		if(codeLine.toLowerCase().contains("do") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		if(codeLine.toLowerCase().trim().equals("do"))
			return true;

		return false;
	}

	protected boolean isSwitchStatement(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("switch") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		if(codeLine.toLowerCase().contains("switch") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}
	protected boolean isTryStatement(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("try") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		return false;
	}
	protected boolean isSwitchCaseFound(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("case") && codeLine.toLowerCase().contains(":") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}
	protected boolean isCatchStatement(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("catch") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")") && codeLine.toLowerCase().contains("{") && codeLine.toLowerCase().contains("exception") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		if(codeLine.toLowerCase().contains("catch") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected boolean isSwitchDefaultFound(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("default") && codeLine.toLowerCase().contains(":") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected boolean isBreakFound(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("break") && codeLine.toLowerCase().contains(";") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected void printNodeList(LinkedList<Node> nodes){
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

	protected void printEdgeList(LinkedList<Edge> edges){
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

	protected Node closeNode(Node thisNode,LinkedList<String> linesOfCode){
		thisNode.setLinesOfCode(linesOfCode);
		String fixedNodeText = thisNode.getThisNodeText();
		fixedNodeText = fixedNodeText.substring(0,fixedNodeText.length()-1);//WE HAVE AN EXTRA ',' THIS SHOLD REMOVE IT
		thisNode.setThisNodeText(fixedNodeText);

		return thisNode;
	}

	protected Edge createEdge(int source,int target){
		Edge newEdge = new Edge(source+"",target+"");
		return newEdge;

	}

	protected void createJoinEdge(LinkedList<Node> currentNodes, Node newNode){
		if(currentNodes.size() > 1 && currentNodes.getLast().getThisNodeText().toLowerCase().equals("join")){
			Edge newEdge = this.createEdge(currentNodes.get(currentNodes.size()-1).getIdentifier(),newNode.getIdentifier());
			this.allEdges.add(newEdge);
		}
	}

	/**
	 * THIS METHOD WILL FIND IF THE LAST NODE OF THE TREE IS A 'JOIN' NODE, WE DON'T NEED THESE KIND OF CASES
	 * @return boolean: true or false
	 */
	protected boolean islastNodeJoin(){
		LinkedList<Node> allNodes = this.getAllNodes();

		if(allNodes.get(allNodes.size()-1).getThisNodeText().toLowerCase().equals("join"))
			return true;
		return false;
	}

	protected int createJoinNode(LinkedList<Integer> identifiersForJoinNode,int biggestIdentifier){
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
				Edge newEdge = this.createEdge(previousNodeIdentifier, newNode.getIdentifier());
				this.allEdges.add(newEdge);
			}
		}

		return (biggestIdentifier+1);
	}



	/**
	 * IF FOR SOME REASON A JOIN GETS CREATED AT THE END, THIS WILL REMOVE THE JOIN AND ANY EDGE THAT CONNECTS TO IT
	 */
	protected void removeLastJoin(){
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
	 * THIS METHOD WILL RETURN A LIST OF NODES TO BE LINKED TO THE NEW CREATED JOIN NODE
	 * @param identifier INTEGER TO BE SEARCH IN ALLTHE EDGES ALREADY CREATED
	 * @param allEdges ALL THE EDGES THAT HAVE BEEN CREATED ALREADY
	 * @return LINKEDLIST CONTAINING ALL THE IDENTIFIERS TO BE LINKED TO THE NEW JOIN NODE
	 */
	protected LinkedList<Integer> findNodesToLinkToJoin(int identifier,LinkedList<Edge> allEdges){

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

	protected void createMissingEdges(LinkedList<Node> allNodes,int previousNodeIdentifier,LinkedList<Edge> allEdges){

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

			if(foundTarget == false && previousNodeIdentifier < nodeIdentifier ){
				newEdge = this.createEdge(previousNodeIdentifier, nodeIdentifier);
				allEdges.add(newEdge);
				this.setAllEdges(allEdges);
			}
		}

	}

	protected boolean findIntegerInList(LinkedList<Integer> list, int number){

		for(int i=0;i<list.size();i++){
			if(number == list.get(i))
				return true;
		}
		return false;
	}

	protected LinkedList<Integer> includeBreakIdentifierNodes(LinkedList<Integer> identifiersForJoinNode,LinkedList<Integer> breakIdentifiers){
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
	protected boolean isThisLastNode(String[] javaContentData){

		if(javaContentData != null && this.isMethodCall(javaContentData[0]) == true)
			return true;
		return false;
	}

	protected boolean isFirstNodeOfMethod(int nodeIdentifier){
		LinkedList<Integer> allMethodSkipEdges = this.getMethodSkipEdges();

		for(int i = 0; i< allMethodSkipEdges.size();i++){
			if(nodeIdentifier == allMethodSkipEdges.get(i))
				return true;
		}
		return false;

	}
	protected boolean findIdentifierInNodes(LinkedList<Node> allNodes, int identifier){
		for(int i=0;i< allNodes.size();i++){
			if(identifier == allNodes.get(i).getIdentifier()){
				return true;
			}
		}
		return false;
	}

	protected String getTextFromNode(LinkedList<Node> allNodes,int identifier){
		for(int i=0;i< allNodes.size();i++){
			if(identifier == allNodes.get(i).getIdentifier()){
				return allNodes.get(i).getThisNodeText();
			}
		}
		return "";
	}
	protected LinkedList<String> getLinesOfCodeFromNode(LinkedList<Node> allNodes,int identifier){
		for(int i=0;i< allNodes.size();i++){
			if(identifier == allNodes.get(i).getIdentifier()){
				return allNodes.get(i).getLinesOfCode();
			}
		}
		return null;
	}

	protected boolean isJoinNode(LinkedList<Node> allNodes,int identifier){

		if(allNodes.get(identifier).getThisNodeText().toLowerCase().equals("join"))
			return true;
		return false;
	}
}
