package com.fyp.expertise.Dictionary;

import java.util.ArrayList;
import java.util.List;

public class ExtractorRules {

	AccessModifiers modifier;
	List<String> modf = new ArrayList<String>();
	List<String> returnType = new ArrayList<String>();


	public boolean checkForPackage(String line){
		if(line.contains("package")) {
			return true;
		}
		else return false;
	}

	public boolean checkForImport(String line){
		if(line.contains("import")) {
			return true;
		}
		else return false;
	}

	public boolean checkForClass(String line){
		if(line.contains("class")) {
			return true;
		}
		else return false;
	}

	public boolean checkForInterface(String line){
		if(line.contains("interface")) {
			return true;
		}
		else return false;
	}

	public boolean checkForMethod(String line){
		boolean method = false;

		modf.add("private");
		modf.add("public");
		modf.add("protected");

		returnType.add("void");
		returnType.add("int");
		returnType.add("String");
		returnType.add("float");
		returnType.add("double");

		line = line.trim();
		String[] words = line.split("\\s+");
		
		for(String term : modf){
			if(line.contains(term)) {
				method = true;
				break;
			}			
		}
		if(method == true){
			for(String type : returnType){
				if(line.contains(type)) {
					break;
				}
				else method = false;
			}
		}
		if(method == false){
			
		}

		
		return true;

	}


}
