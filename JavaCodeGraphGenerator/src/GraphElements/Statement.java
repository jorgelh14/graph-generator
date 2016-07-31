package GraphElements;

public class Statement {
	
	private String blockStatement;
	private boolean traversed;
	
	public Statement(String blockStatement,boolean traversed){
		this.setBlockStatement(blockStatement);
		this.setTraversed(traversed);
		
	}
	public Statement(){
		blockStatement = "";
		traversed = false;
	}

	/**
	 * @return the blockStatement
	 */
	public String getBlockStatement() {
		return blockStatement;
	}

	/**
	 * @param blockStatement the blockStatement to set
	 */
	public void setBlockStatement(String blockStatement) {
		this.blockStatement = blockStatement;
	}

	/**
	 * @return the traversed
	 */
	public boolean isTraversed() {
		return traversed;
	}

	/**
	 * @param traversed the traversed to set
	 */
	public void setTraversed(boolean traversed) {
		this.traversed = traversed;
	}
	
	

}
