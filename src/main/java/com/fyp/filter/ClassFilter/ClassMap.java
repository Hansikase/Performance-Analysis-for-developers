package com.fyp.filter.ClassFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;

public class ClassMap {

	private static final String PROJECT_CLASSPATH = "E:/4th year/FYP/FinalReport/classNames.txt";

	private static ClassMap instance = null;

	private Set<String> projectClasses = new HashSet<String>();
	
	private Set<String> imports = new LinkedHashSet<String>();
	
	private ClassMap(){

	}

	public static ClassMap getInstance(){
		if(instance == null){
			instance = new ClassMap();
		}
		return instance;
	}

	private HashMap<String, OClass> classMap = new HashMap<String, OClass>();

	public OClass getClass(String id) {
		//Check*******
		if(classMap.containsKey(id)){
			return getInstance().classMap.get(id);
		}
		else {
			getInstance().setClassMap(id, new OClass());
			return getInstance().getClass(id);
		}

	}

	public HashMap<String, OClass> getClassMap() {
		return classMap;
	}

	public void setClassMap(String id, OClass obj) {
		HashMap<String, OClass> temp = this.getClassMap();
		if(temp.containsKey(id)) {
			this.classMap.replace(id, obj);
		}
		else {
			this.classMap.put(id, obj);
		}
	}

	public List<String> codeFragemnets = new ArrayList<String>();

	public List<String> getCodeFragemnets() {
		return codeFragemnets;
	}

	public void setCodeFragemnets(String codeFragemnets) {
		this.codeFragemnets.add(codeFragemnets);
	}

	public void readProjectClasses(Set<String> files){
		for(String file : files){
			String[] tempS = file.split("/");
			String id = tempS[tempS.length-1];
			getInstance().projectClasses.add(id.split("\\.")[0]);
		}
	}

	public Set<String> getProjectClasses() {
		return getInstance().projectClasses;
	}

	public Set<String> getImports() {
		return getInstance().imports;
	}

	public void setImports(List<String> imports) {
		getInstance().imports.addAll(imports);
	}


}
