package GraphElements;

import java.util.LinkedList;

public class MethodTree {

	private LinkedList<String> ifStatements;
	private LinkedList<String> forStatements;
	private LinkedList<String> whileStatements;
	private LinkedList<String> doWhileStatements;
	private LinkedList<String> switchStatements;
	private LinkedList<String> tryStatements;
	
	public MethodTree(){
		
	}

	/**
	 * @return the ifStatements
	 */
	public LinkedList<String> getIfStatements() {
		return ifStatements;
	}

	/**
	 * @param ifStatements the ifStatements to set
	 */
	public void setIfStatements(LinkedList<String> ifStatements) {
		this.ifStatements = ifStatements;
	}

	/**
	 * @return the forStatements
	 */
	public LinkedList<String> getForStatements() {
		return forStatements;
	}

	/**
	 * @param forStatements the forStatements to set
	 */
	public void setForStatements(LinkedList<String> forStatements) {
		this.forStatements = forStatements;
	}

	/**
	 * @return the whileStatements
	 */
	public LinkedList<String> getWhileStatements() {
		return whileStatements;
	}

	/**
	 * @param whileStatements the whileStatements to set
	 */
	public void setWhileStatements(LinkedList<String> whileStatements) {
		this.whileStatements = whileStatements;
	}

	/**
	 * @return the doWhileStatements
	 */
	public LinkedList<String> getDoWhileStatements() {
		return doWhileStatements;
	}

	/**
	 * @param doWhileStatements the doWhileStatements to set
	 */
	public void setDoWhileStatements(LinkedList<String> doWhileStatements) {
		this.doWhileStatements = doWhileStatements;
	}

	/**
	 * @return the switchStatements
	 */
	public LinkedList<String> getSwitchStatements() {
		return switchStatements;
	}

	/**
	 * @param switchStatements the switchStatements to set
	 */
	public void setSwitchStatements(LinkedList<String> switchStatements) {
		this.switchStatements = switchStatements;
	}

	/**
	 * @return the tryStatements
	 */
	public LinkedList<String> getTryStatements() {
		return tryStatements;
	}

	/**
	 * @param tryStatements the tryStatements to set
	 */
	public void setTryStatements(LinkedList<String> tryStatements) {
		this.tryStatements = tryStatements;
	}
	
}
