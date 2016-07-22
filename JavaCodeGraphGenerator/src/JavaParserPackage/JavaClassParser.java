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

	public LinkedList<String> methodStatementParser(String fileData){

		javaCodeList = new LinkedList<String>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(fileData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(MethodDeclaration node) {

				javaCodeList.add(node.toString());
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<String> ifStatementParser(String MethodData){

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

	public LinkedList<String> forStatementParser(String MethodData){

		javaCodeList = new LinkedList<String>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(ForStatement node) {

				javaCodeList.add(node.toString());
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<String> whileStatementParser(String MethodData){

		javaCodeList = new LinkedList<String>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(WhileStatement node) {

				javaCodeList.add(node.toString());
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<String> doWhileStatementParser(String MethodData){

		javaCodeList = new LinkedList<String>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(DoStatement node) {

				javaCodeList.add(node.toString());
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<String> switchStatementParser(String MethodData){

		javaCodeList = new LinkedList<String>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(SwitchStatement node) {

				javaCodeList.add(node.toString());
				return true;
			}

		});

		return this.javaCodeList;


	}

	public LinkedList<String> tryStatementParser(String MethodData){

		javaCodeList = new LinkedList<String>();
		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(MethodData.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);


		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		cu.accept(new ASTVisitor() {



			public boolean visit(TryStatement node) {

				javaCodeList.add(node.toString());
				return true;
			}

		});

		return this.javaCodeList;


	}

}
