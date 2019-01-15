package com.fyp.expertise.Analyzer.Frequency;

import java.util.HashMap;
import java.util.Set;

import com.fyp.expertise.Developer.DevProfile;
import com.fyp.expertise.Dictionary.Dictionary;

public class Probability {

	public void calculate(DevProfile developer){
		Set<String> Dict = Dictionary.getInstance().readDictionaryFromDb();
		HashMap<String,Integer> expertiseMap = developer.getExpertiseMap();
		double totalCount = 0;
		double totalPackageCount = 0;

		for(String term : Dict){
			if(expertiseMap.keySet().contains(term)){
				totalCount += expertiseMap.get(term);
				if(Dictionary.getInstance().getImportMap().containsKey(term)){
					totalPackageCount += expertiseMap.get(term);
				}
			}
		}
		for(String term : Dict){
			try{
			double percentage = (expertiseMap.get(term)/totalCount)*100;
			developer.setProbabilityMap(term, percentage);
//			System.out.println("expertise probability = " + term + "," + percentage);
			
			if(Dictionary.getInstance().getImportMap().containsKey(term)){
			double packagePercentage = (expertiseMap.get(term)/totalPackageCount)*100;			
//			System.out.println("\npackage probability = " + Dictionary.getInstance().getImportMap().get(term) + "," + packagePercentage + "\n");
//			String[] packageName = Dictionary.getInstance().getImportMap().get(term).split("//.");
//			String name = packageName[0] + "." + packageName[1];
			developer.setPackageProbabilityMap(Dictionary.getInstance().getImportMap().get(term), packagePercentage);
			}
			} catch(NullPointerException e){
				continue;
			}
		}
	}
}
