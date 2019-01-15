package com.fyp.expertise.Analyzer.Usage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList; 
import com.fyp.expertise.Developer.DevProfile;
import com.fyp.expertise.Developer.DeveloperMap;

public class MethodFrequency {
	
	public final static String FILENAME = "E:/4th year/FYP/FinalReport/methods.txt";
	
	public ArrayList<String> codeFragments = new ArrayList<String>();
	public ArrayList<String> methods = new ArrayList<String>();
	
	private void readMethods(DevProfile profile){

		FileReader reader = null;
		BufferedReader buffer = null;

		try{
			reader = new FileReader(FILENAME);
			buffer = new BufferedReader(reader);

			String sCurrentLine;
			//buffer = new BufferedReader(new FileReader(filepath));

			while ((sCurrentLine = buffer.readLine()) != null) {
				sCurrentLine = sCurrentLine.trim();
				this.methods.add(sCurrentLine);  
			}

		}catch(IOException e){
			e.printStackTrace();

		} finally {

			try {

				if (buffer != null)
					buffer.close();

				if (reader != null)
					reader.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
	
	private void readCodeFragments(DevProfile profile){

		FileReader reader = null;
		BufferedReader buffer = null;

		try{
			reader = new FileReader(profile.getDevFilepath());
			buffer = new BufferedReader(reader);

			String sCurrentLine;
			//buffer = new BufferedReader(new FileReader(filepath));

			while ((sCurrentLine = buffer.readLine()) != null) {
				sCurrentLine = sCurrentLine.trim();
				profile.setCodeFragments(sCurrentLine);  
			}

		}catch(IOException e){
			e.printStackTrace();

		} finally {

			try {

				if (buffer != null)
					buffer.close();

				if (reader != null)
					reader.close();

			} catch (IOException ex) {

				ex.printStackTrace();

			}
		}
	}
	
	public void measureUsage(DevProfile developer){
		readMethods(developer);
		int count;
		MethodMap map = new MethodMap();
		this.readCodeFragments(developer);
		this.codeFragments = developer.getCodeFragments();
		for(int i=0; i<this.codeFragments.size(); i++){
			String line = this.codeFragments.get(i);
			for(String term : methods){
				if(line.contains(term)) {
					count = map.getUsageMap().get(term);
					map.setUsageMap(term, ++count);
					DeveloperMap.getInstance().setMethods(developer.getId(), map);
				}
			}
		}
	}
	
	//for testing purposes only
	public static void main(String[] args){
		
		int id = 0;
		
		DevProfile developer = DevProfile.getDeveloper(id);
		MethodFrequency obj = new MethodFrequency();
		obj.measureUsage(developer);
	}

}
