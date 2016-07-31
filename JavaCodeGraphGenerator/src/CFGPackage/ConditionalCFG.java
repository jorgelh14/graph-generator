package CFGPackage;

import java.util.LinkedList;

import GraphElements.Edge;
import GraphElements.Node;

public class ConditionalCFG extends CFGGraph {

	public void generateGraph(String fileData) throws Exception{

		CFGGraph normalCFG = new NormalCFG();
		normalCFG.generateGraph(fileData);
		this.setAllNodes(normalCFG.getAllNodes());
		this.setAllEdges(normalCFG.getAllEdges());
		LinkedList<Node> currentNodes = this.getAllNodes();
		LinkedList<Edge> currentEdges = this.getAllEdges();
		for(int i = 0; i<currentNodes.size();i++){
			int totalNumberofCurrentNodes = currentNodes.size();
			LinkedList<String> linesOfCodePerNode = currentNodes.get(i).getLinesOfCode();
			for(int z = 0; z< linesOfCodePerNode.size();z++){
				if(isIfStatement(linesOfCodePerNode.get(z))){
					convertToConditional(currentNodes.get(i), currentEdges, currentNodes, "if", linesOfCodePerNode.get(z));
					if(totalNumberofCurrentNodes != currentNodes.size())
						i = -1;

				}
				else if(isElseIfStatement(linesOfCodePerNode.get(z))){
					convertToConditional(currentNodes.get(i), currentEdges, currentNodes, "elseif", linesOfCodePerNode.get(z));
					if(totalNumberofCurrentNodes != currentNodes.size())
						i = -1;
				}else if(isForStatement(linesOfCodePerNode.get(z))){
					convertToConditional(currentNodes.get(i), currentEdges, currentNodes, "for", linesOfCodePerNode.get(z));
					if(totalNumberofCurrentNodes != currentNodes.size())
						i = -1;
				}else if(isWhileStatement(linesOfCodePerNode.get(z))){
					convertToConditional(currentNodes.get(i), currentEdges, currentNodes, "while", linesOfCodePerNode.get(z));
					if(totalNumberofCurrentNodes != currentNodes.size())
						i = -1;
				}else if(isDoWhileStatement(linesOfCodePerNode.get(z))){
					System.out.println("");
					System.out.println("FOUND 'DO WHILE' STATEMENT: " + linesOfCodePerNode.get(z));
				}
			}

		}
	}

	private void convertToConditional(Node normalNode,LinkedList<Edge> allEdges,LinkedList<Node> allNodes,String typeOfStatement,String statementCodeLine){

		LinkedList<Integer> targetIdentifiers = findAllTargetEdgesForConditionalNode(normalNode.getIdentifier(),allEdges);
		LinkedList<Integer> sourceIdentifiers = findAllSourceEdgesForConditionalNode(normalNode.getIdentifier(),allEdges);
		LinkedList<Node> statementNodes = new LinkedList<Node>();
		statementNodes = breakNodeInMultipleNodes(targetIdentifiers,normalNode,statementCodeLine);
		if(statementNodes.size() > 1){
			allEdges = removeNormalStatementEdges(allEdges, targetIdentifiers, normalNode);
			allEdges = createConditionalEdges(allEdges, statementNodes, targetIdentifiers,sourceIdentifiers,typeOfStatement);
			allNodes = removeNormalStatementNodes(allNodes,normalNode);
			allNodes = createConditionalNodes(allNodes, statementNodes);
			this.setAllEdges(allEdges);
			this.setAllNodes(allNodes);

		}
	}

	private LinkedList<Integer> findAllTargetEdgesForConditionalNode(int identifier,LinkedList<Edge> allEdges){

		LinkedList<Integer> targetIdentifiers = new LinkedList<Integer>();

		for(int i = 0;i< allEdges.size();i++){
			if(identifier == Integer.parseInt(allEdges.get(i).getSource()))
				targetIdentifiers.add(Integer.parseInt(allEdges.get(i).getTarget()));
		}

		return targetIdentifiers;

	}

	private LinkedList<Integer> findAllSourceEdgesForConditionalNode(int identifier,LinkedList<Edge> allEdges){


		LinkedList<Integer> sourceIdentifiers = new LinkedList<Integer>();

		for(int i = 0;i< allEdges.size();i++){
			if(identifier == Integer.parseInt(allEdges.get(i).getTarget()))
				sourceIdentifiers.add(Integer.parseInt(allEdges.get(i).getSource()));
		}

		return sourceIdentifiers;


	}

	private LinkedList<Node> breakNodeInMultipleNodes(LinkedList<Integer> targetIdentifiers,Node normalNode,String statementCodeLine){
		String[] conditionalStatements = statementCodeLine.split("\\|\\|");
		Node newNode;
		LinkedList<Node> newCoditionalNodes = new LinkedList<Node>();
		if(conditionalStatements.length > 1){
			for(int i = 0; i < conditionalStatements.length;i++){
				LinkedList<String> conditionalStatementsList = new LinkedList<String>();
				conditionalStatementsList.add(conditionalStatements[i]);
				newNode = new Node(normalNode.getThisNodeText() + setLetterToNumbering(i),getHighestIdentifier() + i);
				if(i == 0){
					LinkedList<String> linesOfCode = normalNode.getLinesOfCode();
					for(int x = 0;x<linesOfCode.size();x++){
						if(linesOfCode.get(x).equals(statementCodeLine)){
							linesOfCode.remove(x);
						}
					}
					linesOfCode.add(conditionalStatements[i]);
					newNode.setLinesOfCode(linesOfCode);
				}else{
					newNode.setLinesOfCode(conditionalStatementsList);
				}
				newCoditionalNodes.add(newNode);
			}
		}else{
			newCoditionalNodes.add(normalNode);
		}

		return newCoditionalNodes;
	}

	private String setLetterToNumbering(int pointer){
		String letter = "";

		switch(pointer){
		case 0: letter = "A"; break;
		case 1: letter = "B"; break;
		case 2: letter = "C"; break;
		case 3: letter = "D"; break;
		case 4: letter = "E"; break;
		case 5: letter = "F"; break;
		case 6: letter = "G"; break;
		case 7: letter = "H"; break;
		case 8: letter = "I"; break;
		case 9: letter = "J"; break;
		case 10: letter = "K"; break;
		case 11: letter = "L"; break;
		case 12: letter = "M"; break;
		case 13: letter = "N"; break;
		case 14: letter = "O"; break;
		case 15: letter = "P"; break;
		case 16: letter = "Q"; break;
		case 17: letter = "R"; break;
		case 18: letter = "S"; break;
		case 19: letter = "T"; break;
		case 20: letter = "U"; break;
		case 21: letter = "V"; break;
		case 22: letter = "W"; break;
		case 23: letter = "X"; break;
		case 24: letter = "Y"; break;
		case 25: letter = "Z"; break;
		default: pointer = pointer - 25;
		letter = setLetterToNumbering(pointer) + pointer;
		break;


		}
		return letter;
	}

	private int getHighestIdentifier(){
		LinkedList<Node> allNodes = this.getAllNodes();
		int highestIdentifier = 0;
		for(int i = 0; i<allNodes.size();i++){
			if(allNodes.get(i).getIdentifier() > highestIdentifier)
				highestIdentifier = allNodes.get(i).getIdentifier();
		}
		highestIdentifier++;

		return highestIdentifier;
	}

	private LinkedList<Edge> removeNormalStatementEdges(LinkedList<Edge> allEdges,LinkedList<Integer> targetIdentifiers,Node normalNode){
		for(int j = 0;targetIdentifiers != null && targetIdentifiers.size() > 1 && j<targetIdentifiers.size();j++){
			String source = normalNode.getIdentifier() + "";
			String target = targetIdentifiers.get(j)+ "";
			for(int i=0;i<allEdges.size();i++){
				if(source.equals(allEdges.get(i).getSource()) && target.equals(allEdges.get(i).getTarget())){
					allEdges.remove(i);
				}
			}
		}

		return allEdges;
	}

	private LinkedList<Node> removeNormalStatementNodes(LinkedList<Node> allNodes, Node normalNode){
		for(int i = 0;i<allNodes.size();i++){
			if(allNodes.get(i).getIdentifier() == normalNode.getIdentifier())
				allNodes.remove(i);
		}

		return allNodes;
	}

	private LinkedList<Edge> createConditionalEdges(LinkedList<Edge> allEdges,LinkedList<Node> newNodes,LinkedList<Integer> targetIdentifiers,LinkedList<Integer> sourceIdentifiers,String typeOfStatement){
		Edge newEdge;
		for(int i = 0;i<newNodes.size();i++){
			if((i+1) < newNodes.size()){
				int source = newNodes.get(i).getIdentifier();
				int target = newNodes.get(i+1).getIdentifier();
				newEdge = this.createEdge(source,target);
				allEdges.add(newEdge);
			}
		}
		for(int j = 0;j<newNodes.size();j++){
			for(int z = 0;z<targetIdentifiers.size();z++){
				if(newNodes.size()>1 && (typeOfStatement.equals("while") || typeOfStatement.equals("for")) && z==(targetIdentifiers.size()-1) && j == 0){
					//DO NOTHING
				}else{
					int source = newNodes.get(j).getIdentifier();
					int target = targetIdentifiers.get(z);
					newEdge = this.createEdge(source,target);
					allEdges.add(newEdge);
				}
			}
		}
		if(newNodes.size() > 0){
			for(int x = 0;x<sourceIdentifiers.size();x++){
				int source = sourceIdentifiers.get(x);
				int target = newNodes.get(0).getIdentifier();
				newEdge = this.createEdge(source, target);
				allEdges.add(newEdge);
			}
		}

		return allEdges;

	}

	private LinkedList<Node> createConditionalNodes(LinkedList<Node> allNodes,LinkedList<Node> newNodes){
		for(int i = 0; i< newNodes.size();i++){
			allNodes.add(newNodes.get(i));
		}

		return allNodes;
	}
}
