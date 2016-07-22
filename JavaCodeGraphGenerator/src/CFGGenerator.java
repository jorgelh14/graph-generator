
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.LinkedList;

import org.apache.commons.lang.StringEscapeUtils;

import GraphElements.*;
import JavaParserPackage.*;
import IOPackage.*;
import CFGPackage.*;
public class CFGGenerator {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter File Path: ");
		LinkedList<String> listOfMethods = new LinkedList<String>();
		//try {
			//String dirPath = br.readLine(); //USE FOR LIVE CODE
			String dirPath = "C:\\Users\\disae\\workspace\\JavaCodeGraphGenerator\\src\\TestJava.java";
			IOManager fileReaderInstance = new JavaClassReader();
			System.out.println("");
			String fileData = fileReaderInstance.fileReader(dirPath);
			if(fileData != null){
				JavaClassParser javaParseInstance = new JavaClassParser();
				listOfMethods = javaParseInstance.methodStatementParser(fileData);
				if(listOfMethods.size() > 0){
					CFGGraph normalCFG = new NormalCFG();
					normalCFG.generateGraph(listOfMethods);
					XMLGenerator xmlGen = new XMLGenerator();
					xmlGen.CreateExportFile(normalCFG.getAllNodes(), normalCFG.getAllEdges());
					CFGVisualization viewGraph = new CFGVisualization();
					viewGraph.createGraphViz("XMLExportFile.xml");
				}
				
				/*Testing Purposes
				for(int i = 0; i<listOfMethods.size();i++){
					System.out.println("");
					System.out.println("--------------------------------------------------");
					System.out.println("Method " + (i+1));
					System.out.println("--------------------------------------------------");
					//System.out.println(StringEscapeUtils.escapeJava(listOfMethods.get(i)));
					String[] methodArray = listOfMethods.get(i).split("\n");
					System.out.println("");
					for(int x = 0;x<methodArray.length;x++){
					System.out.println("Line " + (x+1) + ": " + methodArray[x]);
					}
				}
				*/
			}else{
				System.out.println("UNABLE TO FIND FILE AT LOCATION: " + dirPath);
			}

		//} catch (IOException e) {
			// TODO Auto-generated catch block
			//CREATE BETTER EXCEPTION HERE LATER
			//e.printStackTrace();
		//}
	}

}
