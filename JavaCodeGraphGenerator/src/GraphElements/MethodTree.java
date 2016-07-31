package GraphElements;

import java.util.LinkedList;

public class MethodTree {

	private LinkedList<Statement> ifStatements;
	private LinkedList<Statement> forStatements;
	private LinkedList<Statement> whileStatements;
	private LinkedList<Statement> doWhileStatements;
	private LinkedList<Statement> switchStatements;
	private LinkedList<Statement> tryStatements;
	
	public MethodTree(){
		
	}

	/**
	 * @return the ifStatements
	 */
	public LinkedList<Statement> getIfStatements() {
		return ifStatements;
	}

	/**
	 * @param ifStatements the ifStatements to set
	 */
	public void setIfStatements(LinkedList<Statement> ifStatements) {
		this.ifStatements = ifStatements;
	}

	/**
	 * @return the forStatements
	 */
	public LinkedList<Statement> getForStatements() {
		return forStatements;
	}

	/**
	 * @param forStatements the forStatements to set
	 */
	public void setForStatements(LinkedList<Statement> forStatements) {
		this.forStatements = forStatements;
	}

	/**
	 * @return the whileStatements
	 */
	public LinkedList<Statement> getWhileStatements() {
		return whileStatements;
	}

	/**
	 * @param whileStatements the whileStatements to set
	 */
	public void setWhileStatements(LinkedList<Statement> whileStatements) {
		this.whileStatements = whileStatements;
	}

	/**
	 * @return the doWhileStatements
	 */
	public LinkedList<Statement> getDoWhileStatements() {
		return doWhileStatements;
	}

	/**
	 * @param doWhileStatements the doWhileStatements to set
	 */
	public void setDoWhileStatements(LinkedList<Statement> doWhileStatements) {
		this.doWhileStatements = doWhileStatements;
	}

	/**
	 * @return the switchStatements
	 */
	public LinkedList<Statement> getSwitchStatements() {
		return switchStatements;
	}

	/**
	 * @param switchStatements the switchStatements to set
	 */
	public void setSwitchStatements(LinkedList<Statement> switchStatements) {
		this.switchStatements = switchStatements;
	}

	/**
	 * @return the tryStatements
	 */
	public LinkedList<Statement> getTryStatements() {
		return tryStatements;
	}

	/**
	 * @param tryStatements the tryStatements to set
	 */
	public void setTryStatements(LinkedList<Statement> tryStatements) {
		this.tryStatements = tryStatements;
	}
	
}
