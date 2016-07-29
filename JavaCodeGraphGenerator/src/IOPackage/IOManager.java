package IOPackage;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import GraphElements.Edge;
import GraphElements.Node;

public abstract class IOManager {

	

	/**
	 * 
	 * @param dirPath File Location
	 * @return String with all the file data
	 */
	public String fileReader(String dirPath) {
		return null;
	}

	

	public void CreateExportFile(LinkedList<Node> allNodes,
			LinkedList<Edge> allEdges, String string) {
		// TODO Auto-generated method stub
		
	}
	
	public String findJavaFile(){
		return null;
	}

}
