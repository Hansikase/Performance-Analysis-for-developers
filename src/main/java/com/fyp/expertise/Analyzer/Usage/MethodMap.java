package com.fyp.expertise.Analyzer.Usage;

import java.util.HashMap;

public class MethodMap {
	
//	private MethodMap(String id){
//		
//	}
//	
	public HashMap<String,Integer> usageMap = new HashMap<String, Integer>();
	
//	public static MethodMap getMethodMap(String id){
//		if(MethodMap.checkExistence(id)){
//			return DeveloperMap.getMethodMap(id);
//		}
//		else {
//			DevProfile profile = new MethodMap(id);
//			DeveloperMap.getInstance().setDevelopers(id, profile);
//			return profile;
//		}
//	}
 
	public HashMap<String, Integer> getUsageMap() {
		return usageMap;
	}

	public void setUsageMap(String methodName, int count) {
		this.usageMap.put(methodName, count);
	}

	public void checkMethodUsage(){
		
	}
}
