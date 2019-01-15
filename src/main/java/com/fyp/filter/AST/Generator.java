package com.fyp.filter.AST;

import java.util.List; 
import java.util.Set;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Name;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;

import com.fyp.expertise.Developer.DevProfile;
import com.fyp.expertise.Dictionary.Dictionary;
import com.fyp.filter.ClassFilter.ClassMap;
import com.fyp.filter.ClassFilter.OClass;
import com.fyp.filter.ClassFilter.OMethod;
import com.fyp.filter.IdentifierFilter.IdentifierRules;

public class Generator {

	static OClass oclass;

	static Set<String> fileSet;

	public void generatorM(String filename){

		fileSet = ClassMap.getInstance().getProjectClasses();
		String[] tempS = filename.split("/");
		String id = tempS[tempS.length-1];
		id = id.split("\\.")[0];

		List<String> temp = ClassMap.getInstance().getCodeFragemnets();
		String code = "";
		for(String line : temp){
			code = code.concat(line + "\n");
		}

		ASTParser parser = ASTParser.newParser(AST.JLS3);
		parser.setSource(code.toCharArray());
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		//ASTNode node = parser.createAST(null);

		final CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		oclass = ClassMap.getInstance().getClass(id);
		try{
			cu.accept(new ASTVisitor() {

				OMethod omethod = null;
				public boolean visit(MethodDeclaration node) {
					if(omethod == null){
						omethod = new OMethod();
					}
					for(Object param : node.parameters()){
						String[] temp = param.toString().trim().split(" ");
						String parsed = temp[0].split("<")[0];
						parsed = parsed.split("\\[")[0];
						if(!IdentifierRules.identifiers.contains(parsed)){
							omethod.setLocalVariableMap(temp[1],parsed );
						}
						//set import map
						List<String> imports = oclass.getImports();
						for(String term : imports){
							if(term.contains(parsed)){
								Dictionary.getInstance().setImportMap(parsed, term);
							}
						}

					}
					//					omethod.setReturnType(node.getReturnType2().toString());					
					SimpleName name = node.getName();
					node.getBody();
					omethod.setMethodName(name.toString());
					omethod.setLineNumber(cu.getLineNumber(name.getStartPosition()));
					oclass.setMethodMap(omethod);
					return true; 
				} 

				public boolean visit(VariableDeclarationFragment node) {
					if(omethod == null){
						omethod = new OMethod();
					}
					SimpleName name = node.getName();
					String[] test = node.getParent().toString().trim().split(" ");
					String type = test[0].split("<")[0];
					type = type.split("\\[")[0];
					if(!IdentifierRules.identifiers.contains(type)){
						omethod.setLocalVariableMap(name.toString(), type);	
					}
					//set import map
					List<String> temp = oclass.getImports();
					for(String term : temp){
						if(term.contains(type)){
							Dictionary.getInstance().setImportMap(type, term);
						}
					}

					oclass.setMethodMap(omethod);
					return false; // do not continue to avoid usage info
				}

				public boolean visit(ImportDeclaration node) {
					String name = node.getName().toString();
					String[] temp = name.split("\\.");
					if(!fileSet.contains(temp[temp.length-1])){
						oclass.setImports(name.toString());
					}
					return true; 
				}

				public boolean visit(PackageDeclaration node) {
					Name name = node.getName();
					return true; 
				}

				public boolean visit(MethodInvocation node) {
					SimpleName name = node.getName();
					omethod.setInvoked(name.toString());
					omethod.setInvokedLine(name.toString(), cu.getLineNumber(name.getStartPosition()));
					oclass.setMethodMap(omethod);
					return true; 
				}

				public boolean visit(SimpleName node) {
					try{
						Set<String> temp = omethod.getLocalVariableMap().keySet();
						if (temp.contains(node.getIdentifier())) {
							omethod.setVariableUsageMap(node.getIdentifier(), cu.getLineNumber(node.getStartPosition()));
						}
						return true;
					}catch (NullPointerException e){
						return true;
					}
				}

			});
			cu.getProblems();
		} finally{
			ClassMap.getInstance().setClassMap(id, oclass);
		}
	}

	public static String testReader(int id){

		DevProfile profile = DevProfile.getDeveloper(id);
		String code = "";
		for(String line : profile.getCodeFragments()){
			code = code.concat(line + " \n ");
		}
		//		System.out.println(code);
		return code;

	}
}