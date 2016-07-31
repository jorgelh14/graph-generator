package JavaParserPackage;

import java.util.LinkedList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.WhileStatement;

import GraphElements.Statement;

public class JavaClassParser {

	LinkedList<Statement> javaCodeList = new LinkedList<Statement>();
	public JavaClassParser(){

	}

	/**
	 * @return the javaCodeList
	 */
	public LinkedList<Statement> getJavaCodeList() {
		return javaCodeList;
	}

	/**
	 * @param javaCodeList the javaCodeList to set
	 */
	public void setJavaCodeList(LinkedList<Statement> javaCodeList) {
		this.javaCodeList = javaCodeList;
	}

	public LinkedList<Statement> methodStatementParser(String fileData){

		javaCodeList = new LinkedList<Statement>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(fileData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(MethodDeclaration node) {

				javaCodeList.add(new Statement(node.toString(),false));
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<Statement> ifStatementParser(String MethodData){

		javaCodeList = new LinkedList<Statement>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(IfStatement node) {

				javaCodeList.add(new Statement(node.toString(),false));
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<Statement> forStatementParser(String MethodData){

		javaCodeList = new LinkedList<Statement>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(ForStatement node) {

				javaCodeList.add(new Statement(node.toString(),false));
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<Statement> whileStatementParser(String MethodData){

		javaCodeList = new LinkedList<Statement>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(WhileStatement node) {

				javaCodeList.add(new Statement(node.toString(),false));
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<Statement> doWhileStatementParser(String MethodData){

		javaCodeList = new LinkedList<Statement>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(DoStatement node) {

				javaCodeList.add(new Statement(node.toString(),false));
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<Statement> switchStatementParser(String MethodData){

		javaCodeList = new LinkedList<Statement>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(SwitchStatement node) {

				javaCodeList.add(new Statement(node.toString(),false));
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<Statement> tryStatementParser(String MethodData){

		javaCodeList = new LinkedList<Statement>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(TryStatement node) {

				javaCodeList.add(new Statement(node.toString(),false));
				return true;
			}

		});

		return this.javaCodeList;


	}

}
