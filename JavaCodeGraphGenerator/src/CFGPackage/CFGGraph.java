package CFGPackage;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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

	protected final int stackOverFlowLimit = 1000;


	/**
	 * @return the stackOverFlowLimit
	 */
	protected int getStackOverFlowLimit() {
		return stackOverFlowLimit;
	}



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



	public void generateGraph(String fileData) throws Exception{

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
		if(codeLine.toLowerCase().contains("else ") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		if(codeLine.toLowerCase().contains("else ") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected boolean isElseIfStatement(String codeLine){

		if(codeLine == null)
			return false;

		if(codeLine.toLowerCase().contains("else ") && codeLine.toLowerCase().contains("if") && codeLine.toLowerCase().contains("{") && !codeLine.toLowerCase().contains("system.out"))
			return true;
		if(codeLine.toLowerCase().contains("else ") && codeLine.toLowerCase().contains("if") && !codeLine.toLowerCase().contains("system.out"))
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

		if(codeLine.toLowerCase().contains("while") && codeLine.toLowerCase().contains("(") && codeLine.toLowerCase().contains(")") && !codeLine.toLowerCase().contains(";") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected boolean isCommentLine(String codeLine){
		
		if(codeLine == null)
			return false;
		
		if(codeLine.substring(0, 2).trim().equals("//") || codeLine.substring(1, 3).trim().equals("//"))
			return true;
		if(codeLine.substring(0, 2).trim().equals("/*") || codeLine.substring(1, 3).trim().equals("/*"))
			return true;
		if(codeLine.substring(0, 1).trim().equals("*") || codeLine.substring(1, 2).trim().equals("*"))
			return true;
		if(codeLine.substring(0, 2).trim().equals("*/") || codeLine.substring(1, 3).trim().equals("*"))
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

		if(codeLine.toLowerCase().contains("break;") && !codeLine.toLowerCase().contains("system.out"))
			return true;

		return false;
	}

	protected boolean isSkipLine(String codeLine, int pointer){

		if(isMethodCall(codeLine) == true){
			return true;
		}
		if(isIfStatement(codeLine.trim()) == true && pointer == 0){
			return true;
		}
		if(isTryStatement(codeLine.trim()) == true && pointer == 0){
			return true;
		}
		if(isSwitchStatement(codeLine.trim()) == true && pointer == 0){
			return true;
		}
		if(isDoWhileStatement(codeLine.trim()) == true && pointer == 0){
			return true;
		}
		if(isForStatement(codeLine.trim()) == true && pointer == 0){
			return true;
		}
		if(isWhileStatement(codeLine.trim()) == true && pointer == 0){
			return true;
		}
		if(codeLine.trim().equals("{") || codeLine.trim().equals("}")){
			return true;
		}
		if(isCommentLine(codeLine) == true)
			return true;

		return false;
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

	protected Node createNode(int nodeNumbering, int identifier){
		Node newNode = new Node(nodeNumbering+",",identifier);
		return newNode;
	}

}
