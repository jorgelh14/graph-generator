package JavaParserPackage;

import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

public class JavaClassParser {

	LinkedList<String> javaCodeList = new LinkedList<String>();
	public JavaClassParser(){

	}

	/**
	 * @return the javaCodeList
	 */
	public LinkedList<String> getJavaCodeList() {
		return javaCodeList;
	}

	/**
	 * @param javaCodeList the javaCodeList to set
	 */
	public void setJavaCodeList(LinkedList<String> javaCodeList) {
		this.javaCodeList = javaCodeList;
	}

	public LinkedList<String> MethodStatementParser(String fileData){

		javaCodeList = new LinkedList<String>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(fileData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(MethodDeclaration node) {

				javaCodeList.add(node.toString());
				//methodVisitor(node.toString());
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<String> IfStatementParser(String MethodData){

		javaCodeList = new LinkedList<String>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(IfStatement node) {

				javaCodeList.add(node.toString());
				return true;
			}

		});

		return this.javaCodeList;


	}




}
