package com.fyp.expertise.Analyzer.Frequency;

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

import org.json.JSONException;

import com.fyp.database.DbHandler;
import com.fyp.expertise.Developer.DevProfile;
import com.fyp.expertise.Developer.DeveloperLines;
import com.fyp.expertise.Dictionary.Cluster;
import com.fyp.expertise.Dictionary.Dictionary;
import com.fyp.filter.AST.Generator;
import com.fyp.filter.ClassFilter.ClassMap;
import com.fyp.filter.ClassFilter.OClass;
import com.fyp.filter.ClassFilter.OMethod;

public class ExpertiseFrequency {

	public final static String FILENAME = "E:/4th year/FYP/FinalReport/Developers.txt";

	public final static String CLASSYFY_FILENAME = "E:/DevelopersClassify.txt";

	public static String CLASSPATH = "E:/4th year/FYP/test/Janitha/TestProjects (2)/New folder/jus-master/";

	public final static String DevLinePath = "";

	Dictionary dict = Dictionary.getInstance();

	public ArrayList<String> codeFragments = new ArrayList<String>();

	public void measureFrequency(String name,ArrayList<Integer> lines){

		DbHandler handler = new DbHandler();
		handler.addDeveloper(name);
		handler.countFiles();

		int id = handler.getDeveloper(name);

		DevProfile developer = DevProfile.getDeveloper(id);
		//		odevLines.getDeveloperLines()
		//		odevLines.getDevLineMap().get(name)
		for(int line : lines){
			developer.setDeveloperLines(line);
		}

		HashMap<String, OClass> classmap = ClassMap.getInstance().getClassMap();
		Set<String> keys = classmap.keySet();
		for(String key : keys){

			HashMap<String,OMethod> methods = classmap.get(key).getMethodmap();
			Set<String> methodkeys = methods.keySet();
			for(String mkey : methodkeys){
				Set<String> expertiseSet = new HashSet<String>();
				expertiseSet.addAll(methods.get(mkey).getLocalVariableMap().values());

				for(String expertise : expertiseSet){
					methods.get(mkey).setMethodExpertise(expertise);
				}

//								System.out.println("method : " + mkey );
//								printVariableMap(methods.get(mkey).getLocalVariableMap());
//								printUsageMap(methods.get(mkey).getVariableUsageMap());
//								printInvokedMap(methods.get(mkey).getInvoked());
//								printInvokedLineMap(methods.get(mkey).getInvokedLineMap());
			}

			//			developer.readDeveloperLines(devFilePath);
			developer.mapExpertise(classmap.get(key), developer.getDeveloperLines());

			HashMap<String, Integer> methodExpertise = developer.getMethodExpertiseMap();
			double totalCount = 0;
			for(String expertise : methodExpertise.keySet()){
				totalCount += methodExpertise.get(expertise);
			}

			for(String expertise : methodExpertise.keySet()){
				if(Dictionary.getInstance().getImportMap().keySet().contains(expertise)){
					double percentage = (methodExpertise.get(expertise)/totalCount)*100;
					//					System.out.println("\nmethod expertise for exp " + expertise + " = " + percentage + "\n");
					handler.addMethodExpertise(id, expertise, percentage);
				}
			}

			//update dictionary at the end of project execution 
			Dictionary.getInstance().updateDictionaryToFile();

			Probability obj = new Probability();
			obj.calculate(developer);

			HashMap<String,Double> probabilityMap = developer.getProbabilityMap();
			HashMap<String,Double> packagepProbabilityMap = developer.getPackageProbabilityMap();

			//			update db
			for(String expertise : probabilityMap.keySet()){
				handler.updateProbability(id, expertise, probabilityMap.get(expertise));
			}

			for(String opackage : packagepProbabilityMap.keySet()){
				handler.updatePackageProbability(id, opackage, packagepProbabilityMap.get(opackage));
			}
			ClassMap.getInstance().setImports(classmap.get(key).getImports());
		}		
		HashMap<String,Integer> percentageMap = handler.getDeveloperPercantage(id);
		this.writeInKMeansFormat(id, percentageMap, ClassMap.getInstance().getImports());
		Cluster ocluster = new Cluster();
		//				this.writeToFile(developer);
		//				this.writeInKMeansFormat(developer.getExpertiseMap());

	}

	private void readCodeFragments(String classpath){
		FileReader reader = null;
		BufferedReader buffer = null;
		try{
			reader = new FileReader(classpath);
			buffer = new BufferedReader(reader);

			String sCurrentLine;
			//buffer = new BufferedReader(new FileReader(filepath));

			while ((sCurrentLine = buffer.readLine()) != null) {
				sCurrentLine = sCurrentLine.trim();
				ClassMap.getInstance().setCodeFragemnets(sCurrentLine);  
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

	public void writeToFile(DevProfile developer){
		BufferedWriter bw = null;
		FileWriter fw = null;
		String newLine = System.getProperty("line.separator");		

		try {

			fw = new FileWriter(FILENAME,true);
			bw = new BufferedWriter(fw);

			HashMap<String,Integer> expertiseMap = developer.getExpertiseMap();
			HashMap<String, Integer> UsageMap = developer.getUsageMap();

			for(String expertise : expertiseMap.keySet()){
				bw.write("developer : " + developer.getName() + ", expertise : " + expertise + ", count : "+ expertiseMap.get(expertise)+ " ");
				bw.write(newLine);
			}
			for(String method : UsageMap.keySet()){
				bw.write("\n Usage: \ndeveloper : " + developer.getName() + ", expertise : " + method + ", count : "+ UsageMap.get(method)+ " ");
				bw.write(newLine);
			}
			bw.write(newLine);
			bw.write(newLine);

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

	public void writeInKMeansFormat(int id, HashMap<String,Integer> percentageMap, Set<String> imports){
		BufferedWriter bw = null;
		FileWriter fw = null;
		try {
			String newLine = System.getProperty("line.separator");
			fw = new FileWriter(CLASSYFY_FILENAME,true);
			bw = new BufferedWriter(fw);

			bw.write(--id + " ");
			int i = 0;
			for(String oimport : ClassMap.getInstance().getImports()){
				if(percentageMap.containsKey(oimport)){
					bw.write(i++ + ":" + percentageMap.get(oimport) + " ");
				}
				else {
					bw.write(i++ + ":0 ");
				}
			}
			bw.write(newLine);
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

	public static void printVariableMap(HashMap<String,String> map){
		System.out.println("\n Variable map");
		Set<String> keys = map.keySet();
		for(String key : keys){
			System.out.println("key : " + key + " value : " + map.get(key));
		}
	}

	public static void printUsageMap(HashMap<String,List<Integer>> map){
		System.out.println("\n usage map");
		Set<String> keys = map.keySet();
		for(String key : keys){
			List<Integer> temp = map.get(key);
			for(int i : temp){
				System.out.println("key : " + key + " value : " + i);	
			}
		}
	}

	public static void printInvokedLineMap(HashMap<String,List<Integer>> map){
		System.out.println("\n InvokedLine map");
		Set<String> keys = map.keySet();
		for(String key : keys){
			List<Integer> temp = map.get(key);
			for(int i : temp){
				System.out.println("key : " + key + " value : " + i);	
			}
		}
	}

	public static void printInvokedMap(HashMap<String,Integer> map){
		System.out.println("\n Invoked map");
		Set<String> keys = map.keySet();
		for(String key : keys){
			System.out.println("key : " + key + " value : " + map.get(key));
		}
	}

	//for testing purposes only
	public static void main(String[] args){
		try {
			String[] tempS = ExpertiseFrequency.CLASSPATH.split("/");
			String classId = tempS[tempS.length-1];
			classId = classId.split("\\.")[0];

			DeveloperLines odevLines = new DeveloperLines();
			HashMap<String,HashMap<String,ArrayList<Integer>>> devLines = odevLines.findlines();

			//get class names in the project
			ClassMap.getInstance().readProjectClasses(devLines.keySet());

			int i= 0;
			for(String filename : devLines.keySet()){
				System.out.println("No: " + ++i + " filename: " + filename);

				//read File 
				ExpertiseFrequency obj = new ExpertiseFrequency();
				obj.readCodeFragments(CLASSPATH + filename);

				//				Extract Expertise
				Generator generator = new Generator();
				generator.generatorM(CLASSPATH + filename);

				//				if(filename.contains(classId)){
				HashMap<String,ArrayList<Integer>> temp = devLines.get(filename);
				for(String name : temp.keySet()){
					//		measure developer frequency
					obj.measureFrequency(name,temp.get(name));	
				}
			}
			//			DbHandler handler = new DbHandler();
			//			handler.emptyFileTable();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}
