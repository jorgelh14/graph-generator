package IOPackage;


import GraphElements.Edge;
import GraphElements.Node;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

public class XMLGenerator implements IOManager{
	
	public void CreateExportFile (LinkedList<Node> allNodes, LinkedList <Edge> allEdges){ 


		    try {
		    	File file = new File("XMLExportFile.xml");
			    file.createNewFile();	    
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>" + "\n<root>\n");
				for (int i=0; i<allEdges.size(); i++){
					String source= allEdges.get(i).getSource();
					String target= allEdges.get(i).getTarget();
					String sourceText="";
					String targetText="";
					
					for (int j=0; j<allNodes.size(); j++){
						if(Integer.parseInt(source) == allNodes.get(j).getIdentifier()){
							
							sourceText= allNodes.get(j).getThisNodeText();
							if(sourceText.startsWith("Join")){
								sourceText += allNodes.get(j).getIdentifier();
							}
						}
						if (Integer.parseInt(target) == allNodes.get(j).getIdentifier()){
							targetText= allNodes.get(j).getThisNodeText();
							if(targetText.startsWith("Join")){
								targetText += allNodes.get(j).getIdentifier();
							}
						}
					}
					if (sourceText != "" && targetText != ""){
						writer.write( "\t<links>" + "\n" + "\t\t<source>" + sourceText + "</source>\n" + "\t\t<target>" + targetText + "</target>" + "\n\t</links>\n" );
					}
				}
				writer.write("</root>");
		        writer.close();
		    } catch (Exception ex) {
		        ex.printStackTrace();
		    }   
		}

		public static void printXmlDocument(Document document) {
		    DOMImplementationLS domImplementationLS = 
		        (DOMImplementationLS) document.getImplementation();
		    LSSerializer lsSerializer = 
		        domImplementationLS.createLSSerializer();
		    String string = lsSerializer.writeToString(document);
		    System.out.println(string);
		}

		
		public String fileReader(String dirPath) {
			// TODO Auto-generated method stub
			return null;
		}
	}
