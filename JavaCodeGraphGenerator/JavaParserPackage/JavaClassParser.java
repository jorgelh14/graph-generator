package JavaParserPackage;

import java.util.ArrayList;
import java.util.LinkedList;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.WhileStatement;

public class JavaClassParser {

	LinkedList<String> methodList = new LinkedList<String>();
	public JavaClassParser(){
		
	}
	
/**
	 * @return the methodList
	 */
	public LinkedList<String> getMethodList() {
		return methodList;
	}

	/**
	 * @param methodList the methodList to set
	 */
	public void setMethodList(LinkedList<String> methodList) {
		this.methodList = methodList;
	}

public LinkedList<String> MethodStatementParser(String fileData){

	ASTParser parser = ASTParser.newParser(AST.JLS3);
	parser.setSource(fileData.toCharArray());
	parser.setKind(ASTParser.K_COMPILATION_UNIT);
	

	final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

	cu.accept(new ASTVisitor() {

		

		public boolean visit(MethodDeclaration node) {
			 
			methodList.add(node.toString());
			return false;
		}
		
	});
	
	return this.methodList;


}
}
