package com.fyp.expertise.Developer;

import java.util.HashMap;

import com.fyp.expertise.Analyzer.Usage.MethodMap;

public class DeveloperMap {

	private DeveloperMap(){
		
	}
	
	private static DeveloperMap instance = null;
	
	public static DeveloperMap getInstance(){
		if(instance == null){
			instance = new DeveloperMap();
		}
		return instance;
	}
	
	private HashMap<Integer,DevProfile> developers = new HashMap<Integer,DevProfile>();
	private HashMap<Integer,MethodMap> methods = new HashMap<Integer,MethodMap>();

	public DevProfile getDeveloper(int id) {
		return developers.get(id);
	}

	public void setDevelopers(int id, DevProfile profile) {
		getInstance().developers.put(id, profile);
	}
	
	public boolean checkExistence(int id){
		if(getInstance().developers.containsKey(id)){
			return true;
		}
		else return false;
	}

	public MethodMap getMethods(int id) {
		return methods.get(id);
	}

	public void setMethods(int id, MethodMap obj) {
		this.methods.put(id, obj);
	}


}
