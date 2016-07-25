
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import IOPackage.*;
import CFGPackage.*;
public class CFGGenerator {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter File Path: ");

		//try {
			//String dirPath = br.readLine(); //USE FOR LIVE CODE
			String dirPath = "C:\\Users\\disae\\workspace\\JavaCodeGraphGenerator\\src\\TestJava.java";
			IOManager fileReaderInstance = new JavaClassReader();
			IOManager fileDetailsGenerator = new NodeDetailsGenerator();
			System.out.println("");
			String fileData = fileReaderInstance.fileReader(dirPath);
			if(fileData != null){

				//GENERATING THE NORMAL CFG GRAPH
				CFGGraph normalCFG = new NormalCFG();
				normalCFG.generateGraph(fileData);
				fileDetailsGenerator = new NodeDetailsGenerator(normalCFG, "normalCFGDetails.txt");
				IOManager xmlGen = new XMLGenerator();
				xmlGen.CreateExportFile(normalCFG.getAllNodes(), normalCFG.getAllEdges(),"normalCFGExportFile.xml");
				IOManager viewGraph = new CFGVisualization("normalCFGExportFile.xml");
				

				//GENERATING THE CCONDITIONAL CFG GRAPH
				CFGGraph conditionalCFG = new ConditionalCFG();
				conditionalCFG.generateGraph(fileData);
				fileDetailsGenerator = new NodeDetailsGenerator(conditionalCFG, "conditionalCFGDetails.txt");
				xmlGen = new XMLGenerator();
				xmlGen.CreateExportFile(conditionalCFG.getAllNodes(), conditionalCFG.getAllEdges(),"conditionalCFGExportFile.xml");
				viewGraph = new CFGVisualization("conditionalCFGExportFile.xml");



			}else{
				System.out.println("UNABLE TO FIND FILE AT LOCATION: " + dirPath);
			}

		//} catch (IOException e) {
			//TODO Auto-generated catch block
			//CREATE BETTER EXCEPTION HERE LATER
			//e.printStackTrace();
		//}
	}

}
