package com.fyp.expertise.Dictionary;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fyp.database.DbHandler;
import com.fyp.filter.ClassFilter.ClassMap;

public class Dictionary {

	private final static String FILEPATH = "E:/4th year/FYP/FinalReport/expertiseDic.txt";

	//class wise
	Set<String> expertise = new HashSet<String>();

	//all
	List<String> dictionary = new ArrayList<String>();

	private static Dictionary dict = null;	

	//expertise, package
	private HashMap<String,String> importMap = new HashMap<String, String>();

	private Dictionary(){

	}

	public static Dictionary getInstance(){
		if(dict == null){
			dict = new Dictionary();			
		}
		return dict;
	}

	public void updateDictionary(Set<String> expertise){
		if(!ClassMap.getInstance().getProjectClasses().contains(expertise)){
			getInstance().expertise.addAll(expertise);
			DbHandler handler = new DbHandler();
			for(String exp : getInstance().expertise){
				handler.addToDictionary(exp);
			}
		}
	}

	public Set<String> readDictionaryFromDb(){
		DbHandler handler = new DbHandler();
		return handler.readDictionary();
	}
	
	public List<String> readDictionary(){
		FileReader reader = null;
		BufferedReader buffer = null;
		try{
			reader = new FileReader(FILEPATH);
			buffer = new BufferedReader(reader);

			String sCurrentLine;
			//buffer = new BufferedReader(new FileReader(FILEPATH));

			while ((sCurrentLine = buffer.readLine()) != null) {
				sCurrentLine = sCurrentLine.trim();
				getInstance().dictionary.add(sCurrentLine); 
			}
			return getInstance().dictionary;
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
		return null;
	}

	public void updateDictionaryToFile(){
		BufferedWriter bw = null;
		FileWriter fw = null;
		String newLine = System.getProperty("line.separator");		

		try {

			fw = new FileWriter(FILEPATH,false);
			bw = new BufferedWriter(fw);

			for(String term : getInstance().expertise){
				bw.write(term);
				bw.write(newLine);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();

				if (fw != null)
					fw.close();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	public Set<String> getDictionary() {
		return getInstance().expertise;
	}	

	public HashMap<String, String> getImportMap() {
		return getInstance().importMap;
	}

	public String getImport(String expertise) {
		return getInstance().importMap.get(expertise);
	}

	public void setImportMap(String expertise, String oimport) {
		getInstance().importMap.put(expertise, oimport);
	}

	//for testing purposes only
	public static void main(String[] args){
		Dictionary obj = Dictionary.getInstance();
		for(String term : obj.expertise){
			System.out.println(term + "\n");
		}
	}
}
