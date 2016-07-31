package IOPackage;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class CFGVisualization extends IOManager {
	public CFGVisualization(String fileName){
		createGraphFile(fileName,"pdf");// Types can be gif,dot, fig, pdf, ps, svg, png, and plain.
	}

	private void createGraphFile(String filePath, String type){
		GraphViz graph = createGraphViz(filePath);
		String fileName = filePath.replaceAll(".xml","");
		String repesentationType= "dot";// can be changed to neato, fdp, sfdp, twopi, circo.
		File out = new File(fileName + "." + type);    // change export location here
		graph.writeGraphToFile( graph.getGraph(graph.getDotSource(), type, repesentationType), out );
		try {
			Desktop.getDesktop().open(out);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private GraphViz createGraphViz(String filePath){
		GraphViz graph = new GraphViz();
		graph.addln(graph.start_graph());
		NodeList sourceList = null;
		NodeList targetList = null;
		try{
			sourceList = (getXmlRoot(filePath)).getElementsByTagName("source");
			targetList = (getXmlRoot(filePath)).getElementsByTagName("target");
		}catch(NullPointerException e){
			graph.addln(graph.end_graph());
			graph.increaseDpi();
			return graph;
		}
		for(int i=0;i<sourceList.getLength();i++){
			NodeList sourceSub = sourceList.item(i).getChildNodes();
			String sourceName = sourceSub.item(0).getNodeValue();
			NodeList targetSub = targetList.item(i).getChildNodes();
			graph = addNodesToGraph(graph,sourceName,targetSub);
		}
		graph.addln(graph.end_graph());
		//System.out.println(graph.getDotSource());//testing purposes
		graph.increaseDpi();   // 106 dpi from java api
		return graph;
	}

	private GraphViz addNodesToGraph(GraphViz graph, String sourceName, NodeList targetSub){
		if(targetSub.getLength()>0){
			String targetName = targetSub.item(0).getNodeValue();
			graph.addln("\""+sourceName+"\""+"->"+"\""+targetName+"\"");
			if(targetName.startsWith("Join")){
				graph.addln("\""+targetName+"\""+"[label=\"Join\"]");
			}
			if(sourceName.startsWith("Join")){
				graph.addln("\""+sourceName+"\""+"[label=\"Join\"]");
			}
		}
		else{
			graph.addln("\""+sourceName+"\";");
			if(sourceName.startsWith("Join")){
				graph.addln("\""+sourceName+"\""+"[label=\"Join\"]");
			}
		}
		return graph;
	}
	public org.w3c.dom.Element getXmlRoot(String filePath){
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
			e.printStackTrace();
		}
		return document.getDocumentElement();
	}


}
