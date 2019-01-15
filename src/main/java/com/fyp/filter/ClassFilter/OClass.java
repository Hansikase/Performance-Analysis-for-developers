package com.fyp.filter.ClassFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OClass {
	
	private String ClassName;
	
//	method name,object
	private HashMap<String,OMethod> methodmap = new HashMap<String,OMethod>();
	
	private HashMap<String,Object> globalVariableMap = new HashMap<String, Object>();
	
	private List<String> imports = new ArrayList<String>();
	
	public String getClassName() {
		return ClassName;
	}

	public void setClassName(String className) {
		ClassName = className;
	}

	public OMethod getMethod(String name){
		return methodmap.get(name);
	}
	
	public HashMap<String, OMethod> getMethodmap() {
		return methodmap;
	}

	public void setMethodmap(String name, OMethod method) {
		this.methodmap.put(name, method);
	}
	
	public void replaceMethodmap(String name, OMethod method) {
		this.methodmap.replace(name, method);
	}

	public Object getVariable(String name){
		return globalVariableMap.get(name);
	}
	
	public HashMap<String, Object> getVariableMap() {
		return globalVariableMap;
	}

	public void setVariableMap(HashMap<String, Object> variableMap) {
		this.globalVariableMap = variableMap;
	}
	

	public void setMethodMap(OMethod omethod){
		HashMap<String,OMethod> methodmap = this.getMethodmap();
		String name = omethod.getMethodName();
		if(methodmap.containsKey(name)){
			this.replaceMethodmap(name, omethod);
		}
		else {
			this.setMethodmap(name, omethod);
		}
	}

	public List<String> getImports() {
		return imports;
	}

	public void setImports(String opackage) {
		this.imports.add(opackage);
	}
}
