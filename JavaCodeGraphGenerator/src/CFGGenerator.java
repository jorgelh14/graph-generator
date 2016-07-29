
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import IOPackage.*;
import CFGPackage.*;
public class CFGGenerator {

	public static void main(String[] args) {
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		//try {
			IOManager fileReaderInstance = new JavaClassReader();
			IOManager fileDetailsGenerator = new NodeDetailsGenerator();
			System.out.println("");
			//String fileData = fileReaderInstance.fileReader(dirPath);
			
			String fileData = fileReaderInstance.fileReader(fileReaderInstance.findJavaFile());
			if(fileData != null){

				//GENERATING THE NORMAL CFG GRAPH
				CFGGraph normalCFG = new NormalCFG();
				try {
					normalCFG.generateGraph(fileData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileDetailsGenerator = new NodeDetailsGenerator(normalCFG, "normalCFGDetails.txt");
				IOManager xmlGen = new XMLGenerator();
				xmlGen.CreateExportFile(normalCFG.getAllNodes(), normalCFG.getAllEdges(),"normalCFGExportFile.xml");
				IOManager viewGraph = new CFGVisualization("normalCFGExportFile.xml");
				

				//GENERATING THE CCONDITIONAL CFG GRAPH
				CFGGraph conditionalCFG = new ConditionalCFG();
				try {
					conditionalCFG.generateGraph(fileData);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				fileDetailsGenerator = new NodeDetailsGenerator(conditionalCFG, "conditionalCFGDetails.txt");
				xmlGen = new XMLGenerator();
				xmlGen.CreateExportFile(conditionalCFG.getAllNodes(), conditionalCFG.getAllEdges(),"conditionalCFGExportFile.xml");
				viewGraph = new CFGVisualization("conditionalCFGExportFile.xml");



			}else{
				System.out.println("NO FILE SELECTED, PLEASE RUN PROGRAM AGAIN!");
			}

		//} catch (IOException e) {
			//TODO Auto-generated catch block
			//CREATE BETTER EXCEPTION HERE LATER
			//e.printStackTrace();
		//}
	}

}
