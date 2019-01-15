package com.fyp.expertise.Developer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DeveloperLines {
	
	public static String CLASSPATH = "E:/4th year/FYP/test/Janitha/TestProjects (2)/apptik-jus/CorrectedContribution";
	
	public ArrayList<String> FILENAME = new ArrayList<String>();
	
//	public ArrayList<String> developerNames = new ArrayList<String>();
//	
//	public ArrayList<ArrayList<Integer>> developerLines = new ArrayList<ArrayList<Integer>>();
	
	public HashMap<String,HashMap<String,ArrayList<Integer>>> devLineMap = new HashMap<String, HashMap<String,ArrayList<Integer>>>();
	
	public static ArrayList<String> get_unique_developers(JSONArray j_linecontributors) throws JSONException{
		Set<String> unique_developers =new HashSet<String>();
		for(int i=0; i<j_linecontributors.length();i++){
			unique_developers.add(j_linecontributors.getString(i));
		}
		
		ArrayList<String> devs = new ArrayList<String>();
		devs.addAll(unique_developers);
		return devs;
	}

	public HashMap<String,HashMap<String,ArrayList<Integer>>> findlines() throws IOException, JSONException, InterruptedException{
		ArrayList<String> contributorfiles = listFilesForFolder(new File(CLASSPATH));
		String filename;
		JSONObject j_filecontent;
		JSONArray j_contributors,j_linecontributors;
		String s_filecontent,developer;
		
		ArrayList<String> developers;
		ArrayList< ArrayList<Integer> > developerslines;
		ArrayList<Integer> developer_lines;
		ArrayList<String> unique_developers;
		for(int i=0;i<contributorfiles.size();i++){
			filename = contributorfiles.get(i);
//			this.setFILENAME(filename);
			
			if(filename.equals("summary.txt"))
				continue;
			 developers = new ArrayList<String>();
			 developerslines = new ArrayList<ArrayList<Integer>>();
			s_filecontent = readFolder(filename, CLASSPATH);
			j_filecontent = new JSONObject(s_filecontent);
			j_contributors = j_filecontent.getJSONArray("contributors");
			for(int j=0;j<j_contributors.length();j++){
				j_linecontributors = j_contributors.getJSONArray(j);
				unique_developers = get_unique_developers(j_linecontributors);
				for(int k=0;k<unique_developers.size();k++){
					developer = unique_developers.get(k);
					if(developers.size() ==0){
						developers.add(developer);	
						developer_lines = new ArrayList<Integer>();
						developer_lines.add(j+1);
						developerslines.add(developer_lines);
					}
					else{
						if(developers.contains(developer)){
							developer_lines = developerslines.get(developers.indexOf(developer));
							developer_lines.add(j+1);
							developerslines.set(developers.indexOf(developer), developer_lines);
						}
						else{
							developers.add(developer);	
							developer_lines = new ArrayList<Integer>();
							developer_lines.add(j+1);
							developerslines.add(developer_lines);
						}
					}
					
				}
			}
//			System.out.println("developer " +developers.toString());
//			System.out.println(developerslines.toString());
			int index = 0;
			for(String name : developers){
				this.setDevLineMap(j_filecontent.getString("filename"), name, developerslines.get(index));
				index++;
			}
//			this.setDeveloperNames(developers);
//			this.setDeveloperLines(developerslines);
			//Thread.sleep(2000);
		}
		return devLineMap;		
	}

	public static String readFolder(final String filename, final String foldername ) throws IOException{
		File file = new File(foldername,filename);
		FileInputStream fis = new FileInputStream(file);
		byte[] data = new byte[(int) file.length()];
		fis.read(data);
		fis.close();
		//System.out.println(filename);
		String str = new String(data, "UTF-8");
		return str;
	}

	public static ArrayList<String> listFilesForFolder(final File folder) throws IOException, JSONException {
		ArrayList<String> filepaths = new ArrayList<String>();
		for (final File fileEntry : folder.listFiles()) {
			if(!fileEntry.exists()){
				continue;
			}
			else if (fileEntry.isDirectory()) {
				listFilesForFolder(fileEntry);
			} else {
				filepaths.add(fileEntry.getName());
			}
		}
		return  filepaths;
	}
	
	public ArrayList<String> getFILENAME() {
		return FILENAME;
	}

	public void setFILENAME(String filename) {
		FILENAME.add(filename);
	}

	public HashMap<String, HashMap<String,ArrayList<Integer>>> getDevLineMap() {
		return devLineMap;
	}

	public void setDevLineMap(String name, String classname, ArrayList<Integer> lines) {
		HashMap<String,ArrayList<Integer>> temp = new HashMap<String,ArrayList<Integer>>();
		temp.put(classname, lines);
		this.devLineMap.put(name, temp);
	}

	//for testing purposes only
	public static void main(String [] args) throws IOException, JSONException, InterruptedException{
//		findlines();
	}
}
