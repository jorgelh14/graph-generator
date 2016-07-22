package IOPackage;



import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.text.Document;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CFGVisualization {
	
	public void createGraphViz(String filePath){
		
			GraphViz graph = new GraphViz();
			graph.addln(graph.start_graph());
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;
			try {
				builder = factory.newDocumentBuilder();
			} catch (ParserConfigurationException e) {
				System.out.println("Couldnt start docment builder");
			}
			org.w3c.dom.Document document = null;
			try {
				document = builder.parse(new File(filePath));
			} catch (SAXException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			org.w3c.dom.Element rootElement = document.getDocumentElement();
			
			 NodeList sourceList = ( rootElement).getElementsByTagName("source");
			 NodeList targetList = ( rootElement).getElementsByTagName("target");
			 for(int i=0;i<sourceList.getLength();i++){
		         NodeList sourceSub = sourceList.item(i).getChildNodes();
		         String sourceName = sourceSub.item(0).getNodeValue();
		         NodeList targetSub = targetList.item(i).getChildNodes();
		         String targetName = targetSub.item(0).getNodeValue();
	        	 graph.addln("\""+sourceName+"\""+"->"+"\""+targetName+"\"");
		         if(targetName.startsWith("Join")){
		        	 graph.addln("\""+targetName+"\""+"[label=\"Join\"]");
		         }
		         if(sourceName.startsWith("Join")){
		        	 graph.addln("\""+sourceName+"\""+"[label=\"Join\"]");
		         }
			 }
		    graph.addln(graph.end_graph());
			//System.out.println(graph.getDotSource());//testing purposes
			graph.increaseDpi();   // 106 dpi from java api

			String type = "pdf";		//can be changed to dot, fig, pdf, ps, svg, png, and plain. Just replace gif
			String repesentationType= "dot";// can be changed to neato, fdp, sfdp, twopi, circo.
			File out = new File("graphRep." + type);    // change export location here
			graph.writeGraphToFile( graph.getGraph(graph.getDotSource(), type, repesentationType), out );
			try {
				Desktop.getDesktop().open(out);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	}
	
}
