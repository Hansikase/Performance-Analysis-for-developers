package com.fyp.filter.IdentifierFilter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class IdentifierRules {

	public static final String methodFileter = "(\\b[a-z]\\b)";
	
	public static Set<String> identifiers = new HashSet<String>(Arrays.asList("int", "long", "float", "double",
			"String","boolean","byte","final","protected","public","private","println","main","/**", "T", "Object..", "Object"));
	
//	public static Set<String> methodIdentifiers = new HashSet<String>(Arrays.asList("println","main","/**"));
	
		
	//for testing purposes only
	public static void main(String [] args){
		String test = "aiden Aiden ";
		Pattern p = Pattern.compile(methodFileter);
	    Matcher m = p.matcher(test); 
	    while(m.find()){
	    	System.out.println(m.start());
	    }
	}
}
