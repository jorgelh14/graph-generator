package GraphElements;

import java.util.LinkedList;

public class Node extends ElementManager {
	private String thisNode;
	private int identifier;
	private LinkedList<Node> childNodes;
	
	/**
	 * @param thisNode
	 * @param identifier
	 */
	public Node(String thisNode, int identifier) {
		super();
		this.thisNode = thisNode;
		this.identifier = identifier;
		this.childNodes = new LinkedList<Node>();
	}

	/**
	 * @return the thisNode
	 */
	public String getThisNode() {
		return thisNode;
	}

	/**
	 * @param thisNode the thisNode to set
	 */
	public void setThisNode(String thisNode) {
		this.thisNode = thisNode;
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
	 * @return the childNodes
	 */
	public LinkedList<Node> getChildNodes() {
		return childNodes;
	}

	/**
	 * @param childNodes the childNodes to set
	 */
	public void setChildNodes(LinkedList<Node> childNodes) {
		this.childNodes = childNodes;
	}
	

	

}
