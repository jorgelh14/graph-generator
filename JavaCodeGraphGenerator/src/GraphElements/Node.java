package GraphElements;

import java.util.LinkedList;

public class Node {
	private String thisNodeText;
	private int identifier;
	private LinkedList<String> linesOfCode;
	
	/**
	 * @param thisNodeText
	 * @param identifier
	 */
	public Node(String thisNodeText, int identifier) {
		super();
		this.thisNodeText = thisNodeText;
		this.identifier = identifier;
		this.linesOfCode = new LinkedList<String>();
	}
	
	public Node(){
		super();
	}

	/**
	 * @return the thisNodeText
	 */
	public String getThisNodeText() {
		return thisNodeText;
	}

	/**
	 * @param thisNodeText the thisNodeText to set
	 */
	public void setThisNodeText(String thisNodeText) {
		this.thisNodeText = thisNodeText;
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
	

	

}
