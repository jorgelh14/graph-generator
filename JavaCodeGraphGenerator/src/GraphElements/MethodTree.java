package GraphElements;

import java.util.LinkedList;

public class MethodTree {

	private LinkedList<String> ifStaments;
	
	public MethodTree(){
		
	}

	/**
	 * @return the ifStaments
	 */
	public LinkedList<String> getIfStaments() {
		return ifStaments;
	}

	/**
	 * @param ifStaments the ifStaments to set
	 */
	public void setIfStaments(LinkedList<String> ifStaments) {
		this.ifStaments = ifStaments;
	}
	
}
