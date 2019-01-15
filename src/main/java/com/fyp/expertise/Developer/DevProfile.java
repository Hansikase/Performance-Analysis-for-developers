package com.fyp.expertise.Developer;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.json.JSONException;

import com.fyp.expertise.Analyzer.Frequency.Probability;
import com.fyp.expertise.Dictionary.Dictionary;
import com.fyp.filter.ClassFilter.OClass;
import com.fyp.filter.ClassFilter.OMethod;

public class DevProfile {

	private DevProfile(int id){
		this.id = id;
	}

	public static DevProfile getDeveloper(int id){
		if(DeveloperMap.getInstance().checkExistence(id)){
			return DeveloperMap.getInstance().getDeveloper(id);
		}
		else {
			DevProfile profile = new DevProfile(id);
			DeveloperMap.getInstance().setDevelopers(id, profile);
			return profile;
		}
	}

	private String name;

	private int id;

	private List<Integer> developerLines = new ArrayList<Integer>();

	//	expertise, count
	private HashMap<String,Integer> expertiseMap = new HashMap<String,Integer>();

	private HashMap<String,Double> probabilityMap = new HashMap<String,Double>();

	private HashMap<String,Double> packageProbabilityMap = new HashMap<String,Double>();

	private String devFilepath = null;

	private ArrayList<String> codeFragments = new ArrayList<String>();

	//method name, no of times used
	private HashMap<String, Integer> methodUsageMap = new HashMap<String, Integer>();

	//expertise, no of times used
	private HashMap<String, Integer> methodExpertiseMap = new HashMap<String, Integer>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public List<Integer> getDeveloperLines() {
		return developerLines;
	}

	public void setDeveloperLines(int developerLine) {
		this.developerLines.add(developerLine);
	}

	public HashMap<String,Integer> getExpertiseMap() {
		return expertiseMap;
	}

	public int getExpertiseCount(String expertise) {
		return this.expertiseMap.get(expertise);
	}

	public void setExpertise(String expertise) {
		if(this.expertiseMap.containsKey(expertise)){
			int count = this.getExpertiseCount(expertise);
			this.getExpertiseMap().replace(expertise, ++count);
		}
		else {
			this.expertiseMap.put(expertise,1);
		}
	}

	public HashMap<String, Double> getProbabilityMap() {
		return probabilityMap;
	}

	public double getProbability(String expertise) {
		if(this.probabilityMap.keySet().contains(expertise)){
			return this.probabilityMap.get(expertise);
		}
		else {
			return 0;
		}
	}

	public void setProbabilityMap(String expertise, double probability) {
		this.probabilityMap.put(expertise, probability);
	}

	public HashMap<String, Double> getPackageProbabilityMap() {
		return packageProbabilityMap;
	}

	public void setPackageProbabilityMap(String opackage, Double probability) {
		this.packageProbabilityMap.put(opackage, probability);
	}

	public String getDevFilepath() {
		return devFilepath;
	}

	public void setDevFilepath(String devFilepath) {
		this.devFilepath = devFilepath;
	}

	public ArrayList<String> getCodeFragments() {
		return codeFragments;
	}

	public void setCodeFragments(String codeFragments) {
		this.codeFragments.add(codeFragments);
	}

	public HashMap<String, Integer> getUsageMap() {
		return this.methodUsageMap;
	}

	public void setUsageMap(String method) {
		HashMap<String, Integer> map = this.getUsageMap();		
		if(map.containsKey(method)) {
			int count = map.get(method);
			this.methodUsageMap.replace(method, ++count);
		}
		else {
			this.methodUsageMap.put(method, 1);
		}
	}	

	public HashMap<String, Integer> getMethodExpertiseMap() {
		return methodExpertiseMap;
	}

	public int getMethodExpertise(String name) {
		if(this.methodExpertiseMap.containsKey(name)){
			return this.methodExpertiseMap.get(name);
		}
		return 0;
	}

	public void setMethodExpertiseMap(String name) {
		if(this.methodExpertiseMap.containsKey(name)){
			int count = this.methodExpertiseMap.get(name);
			this.methodExpertiseMap.replace(name, ++count);
		}
		else {
			this.methodExpertiseMap.put(name, 1);
		}
	}

	public void mapExpertise(OClass oclass, List<Integer> devLines){
		HashMap<String,OMethod> methodmap = oclass.getMethodmap();
		Set<String> variableMap = new HashSet<String>();
		for(String omethod : methodmap.keySet()){
			HashMap<String, List<Integer>> invokedLineMap = methodmap.get(omethod).getInvokedLineMap();
			HashMap<String, List<Integer>> variableUsageMap = methodmap.get(omethod).getVariableUsageMap();
			HashMap<String,String> localVariableMap = methodmap.get(omethod).getLocalVariableMap();

			for(int line : devLines){
				for(String method : invokedLineMap.keySet()){
					List<Integer> list = invokedLineMap.get(method);
					for(int num : list){
						if(line == num){
							this.setUsageMap(method);
							List<String> expertise = methodmap.get(omethod).getMethodExpertise();
							for(String term : expertise) {
								this.setMethodExpertiseMap(term);
							}
						}
					}
				}

				for(String var : variableUsageMap.keySet()){
					String expertise;
					List<Integer> list = variableUsageMap.get(var);
					for(int num : list){
						if(line == num){
							expertise = localVariableMap.get(var);
							this.setExpertise(expertise);
						}
					}
				}

				//add to dictionary
				for(String term : localVariableMap.keySet()){
					variableMap.add(localVariableMap.get(term));
				}
			}
		}
		Dictionary.getInstance().updateDictionary(variableMap);
	}
}


