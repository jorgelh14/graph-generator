package GraphElements;

import java.util.LinkedList;

public class CodeBlockFeatures {

	private MethodTree currentMethodTree;
	private int previousNodeIdentifier;
	private Node currentNode;
	private Edge currentEdge;
	private LinkedList<Node> currentNodes;
	private LinkedList<Edge> currentEdges;
	private int nodeNumbering;
	private int blockPointer;
	private LinkedList<String> linesOfCode;
	private String[] javaContentData;
	private int identifier;
	
	
	public CodeBlockFeatures() {
		super();
	}


	/**
	 * @param currentMethodTree
	 * @param previousNodeIdentifier
	 * @param currentNode
	 * @param currentEdge
	 * @param currentNodes
	 * @param currentEdges
	 * @param nodeNumbering
	 * @param blockPointer
	 * @param linesOfCode
	 * @param javaContentData
	 * @param identifier
	 */
	public CodeBlockFeatures(MethodTree currentMethodTree,
			int previousNodeIdentifier, Node currentNode, Edge currentEdge,
			LinkedList<Node> currentNodes, LinkedList<Edge> currentEdges,
			int nodeNumbering, int blockPointer,
			LinkedList<String> linesOfCode, String[] javaContentData,
			int identifier) {
		super();
		this.currentMethodTree = currentMethodTree;
		this.previousNodeIdentifier = previousNodeIdentifier;
		this.currentNode = currentNode;
		this.currentEdge = currentEdge;
		this.currentNodes = currentNodes;
		this.currentEdges = currentEdges;
		this.nodeNumbering = nodeNumbering;
		this.blockPointer = blockPointer;
		this.linesOfCode = linesOfCode;
		this.javaContentData = javaContentData;
		this.identifier = identifier;
	}


	/**
	 * @return the currentMethodTree
	 */
	public MethodTree getCurrentMethodTree() {
		return currentMethodTree;
	}


	/**
	 * @param currentMethodTree the currentMethodTree to set
	 */
	public void setCurrentMethodTree(MethodTree currentMethodTree) {
		this.currentMethodTree = currentMethodTree;
	}


	/**
	 * @return the previousNodeIdentifier
	 */
	public int getPreviousNodeIdentifier() {
		return previousNodeIdentifier;
	}


	/**
	 * @param previousNodeIdentifier the previousNodeIdentifier to set
	 */
	public void setPreviousNodeIdentifier(int previousNodeIdentifier) {
		this.previousNodeIdentifier = previousNodeIdentifier;
	}


	/**
	 * @return the currentNode
	 */
	public Node getCurrentNode() {
		return currentNode;
	}


	/**
	 * @param currentNode the currentNode to set
	 */
	public void setCurrentNode(Node currentNode) {
		this.currentNode = currentNode;
	}


	/**
	 * @return the currentEdge
	 */
	public Edge getCurrentEdge() {
		return currentEdge;
	}


	/**
	 * @param currentEdge the currentEdge to set
	 */
	public void setCurrentEdge(Edge currentEdge) {
		this.currentEdge = currentEdge;
	}


	/**
	 * @return the currentNodes
	 */
	public LinkedList<Node> getCurrentNodes() {
		return currentNodes;
	}


	/**
	 * @param currentNodes the currentNodes to set
	 */
	public void setCurrentNodes(LinkedList<Node> currentNodes) {
		this.currentNodes = currentNodes;
	}


	/**
	 * @return the nodeNumbering
	 */
	public int getNodeNumbering() {
		return nodeNumbering;
	}


	/**
	 * @param nodeNumbering the nodeNumbering to set
	 */
	public void setNodeNumbering(int nodeNumbering) {
		this.nodeNumbering = nodeNumbering;
	}


	/**
	 * @return the blockPointer
	 */
	public int getBlockPointer() {
		return blockPointer;
	}


	/**
	 * @param blockPointer the blockPointer to set
	 */
	public void setBlockPointer(int blockPointer) {
		this.blockPointer = blockPointer;
	}


	/**
	 * @return the linesOfCode
	 */
	public LinkedList<String> getLinesOfCode() {
		return linesOfCode;
	}


	/**
	 * @param linesOfCode the linesOfCode to set
	 */
	public void setLinesOfCode(LinkedList<String> linesOfCode) {
		this.linesOfCode = linesOfCode;
	}


	/**
	 * @return the javaContentData
	 */
	public String[] getJavaContentData() {
		return javaContentData;
	}


	/**
	 * @param javaContentData the javaContentData to set
	 */
	public void setJavaContentData(String[] javaContentData) {
		this.javaContentData = javaContentData;
	}


	/**
	 * @return the identifier
	 */
	public int getIdentifier() {
		return identifier;
	}


	/**
	 * @param identifier the identifier to set
	 */
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}


	/**
	 * @return the currentEdges
	 */
	public LinkedList<Edge> getCurrentEdges() {
		return currentEdges;
	}


	/**
	 * @param currentEdges the currentEdges to set
	 */
	public void setCurrentEdges(LinkedList<Edge> currentEdges) {
		this.currentEdges = currentEdges;
	}
	
	
}
