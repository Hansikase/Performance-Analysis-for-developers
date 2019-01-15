package com.fyp.filter.ClassFilter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class OMethod {

	public OMethod(String name, int line, List<String> parameters){
		this.setMethodName(name);
		this.setLineNumber(line);
		this.setParameters(parameters);
	}

	public OMethod(){

	}

	private String methodName;

	private int lineNumber;

	private List<String> parameters;

	private String returnType;

	//	name, type
	private HashMap<String,String> localVariableMap = new HashMap<String,String>();

	//	name, line nos
	private HashMap<String, List<Integer>> variableUsageMap = new HashMap<String, List<Integer>>();

	//	name, no of times
	private HashMap<String, Integer> invoked = new HashMap<String, Integer>();

	//name, line no
	private HashMap<String, List<Integer>> invokedLineMap = new HashMap<String, List<Integer>>();

	private List<String> methodExpertise = new ArrayList<String>();

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public int getLineNumber() {
		return lineNumber;
	}

	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	public List<String> getParameters() {
		return parameters;
	}

	public void setParameters(List<String> param) {
		this.parameters.addAll(param);
	}

	public String getReturnType() {
		return returnType;
	}

	public void setReturnType(String returnType) {
		this.returnType = returnType;
	}

	public HashMap<String, String> getLocalVariableMap() {
		return localVariableMap;
	}

	public void setLocalVariableMap(String name, String type) {
		this.localVariableMap.put(name,type);
	}

	public List<Integer> getVariableUsage(String key){
		return variableUsageMap.get(key);
	}

	public HashMap<String, List<Integer>> getVariableUsageMap() {
		return variableUsageMap;
	}

	public void setVariableUsageMap(String key, int line) {
		List<Integer> temp = this.getVariableUsage(key);
		if(temp == null) {
			temp = new ArrayList();
			temp.add(line);
			this.variableUsageMap.put(key, temp);
		}
		else {
			temp.add(line);
			this.variableUsageMap.replace(key, temp);
		}
	}

	//check**
	public int getInvokedMethod(String name){
		return invoked.get(name);
	}

	public HashMap<String, Integer> getInvoked() {
		return invoked;
	}

	public void setInvoked(String name) {
		HashMap<String,Integer> temp = this.getInvoked();
		if(temp.containsKey(name)){
			int count = temp.get(name);
			this.invoked.replace(name, ++count);
		}
		else {
			this.invoked.put(name, 1);
		}
	}

	public HashMap<String, List<Integer>> getInvokedLineMap() {
		return invokedLineMap;
	}

	public List<Integer> getInvokedLine(String key){
		return invokedLineMap.get(key);
	}

	public void setInvokedLine(String key, int line) {
		List<Integer> temp = this.getInvokedLine(key);
		if(temp == null) {
			temp = new ArrayList();
			temp.add(line);
			this.invokedLineMap.put(key, temp);
		}
		else {
			temp.add(line);
			this.invokedLineMap.replace(key, temp);
		}
	}

	public List<String> getMethodExpertise() {
		return methodExpertise;
	}

	public void setMethodExpertise(String expertise ) {
		this.methodExpertise.add(expertise);
	}
}
