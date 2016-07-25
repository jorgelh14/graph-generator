package IOPackage;

import java.awt.Desktop;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import CFGPackage.CFGGraph;
import GraphElements.Edge;
import GraphElements.Node;

public class NodeDetailsGenerator extends IOManager {

	public NodeDetailsGenerator(){
		//SIMPLE CONSTRUCTOR
	}

	public NodeDetailsGenerator(CFGGraph graph,String fileName){
		try{
			File file = new File(fileName);
			if(file.exists())
				file.delete();
			else
				file.createNewFile();	
			BufferedWriter writer = new BufferedWriter(new FileWriter(file));
			printNodeList(graph.getAllNodes(),writer);
			printEdgeList(graph.getAllEdges(),writer);
			writer.close();
			openCFGDetailsFile(fileName);
		}catch(Exception e){
			e.printStackTrace();

		}

	}

	private void printNodeList(LinkedList<Node> nodes,BufferedWriter writer){

		try{

			writer.write("\n");
			//System.out.println("");
			writer.write("PRINTING ALL NODES\n");
			//System.out.println("PRINTING ALL NODES");
			writer.write("\n");
			System.out.println("");
			for(int i = 0;i<nodes.size();i++){
				writer.write("--------------------------------------\n");
				//System.out.println("--------------------------------------");
				writer.write("Node: " + nodes.get(i).getThisNodeText()+ "\n");
				//System.out.println("Node: " + nodes.get(i).getThisNodeText());
				writer.write("Identifier: " + nodes.get(i).getIdentifier()+"\n");
				//System.out.println("Identifier: " + nodes.get(i).getIdentifier());
				writer.write("Codelines: \n");
				//System.out.println("Codelines: ");
				for(int x = 0; x< nodes.get(i).getLinesOfCode().size();x++){
					LinkedList<String> linesOfCode = nodes.get(i).getLinesOfCode();
					writer.write("\t"+linesOfCode.get(x) + "\n");
					//System.out.println("\t"+linesOfCode.get(x));

				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private void printEdgeList(LinkedList<Edge> edges,BufferedWriter writer){
		try{

			writer.write("\n");
			//System.out.println("");
			writer.write("PRINTING ALL EDGES\n");
			//System.out.println("PRINTING ALL EDGES");
			writer.write("\n");
			//System.out.println("");
			for(int i = 0;i<edges.size();i++){
				writer.write("--------------------------------------\n");
				//System.out.println("--------------------------------------");
				writer.write("Edge: \n");
				//System.out.println("Edge: ");
				writer.write("Identifier Source: " + edges.get(i).getSource() + "\n");
				//System.out.println("Identifier Source: " + edges.get(i).getSource());
				writer.write("Identifier Target: " + edges.get(i).getTarget() + "\n");
				//System.out.println("Identifier Target: " + edges.get(i).getTarget());
			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	private void openCFGDetailsFile(String fileName){
		try {
			File file = new File(fileName);
			if(file.exists())
				Desktop.getDesktop().open(file);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
