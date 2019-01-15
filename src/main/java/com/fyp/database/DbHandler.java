package com.fyp.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.fyp.expertise.Dictionary.Dictionary;
import com.fyp.filter.ClassFilter.ClassMap;

public class DbHandler {

	public void updateProbability(int devId, String expertise, Double probability){
		int expId = this.getExpertiseId(expertise);
		double eProb = this.getExpertiseProbability(devId, expId);
		if( eProb != 0) {
			//			int fileCount = this.getFilesCount();
			int fileCount = ClassMap.getInstance().getProjectClasses().size();
			double newProb = (((eProb/100)*(fileCount-1) + probability/100) / fileCount)*100;
			String Uquery = "UPDATE expertise SET probability = '" + newProb + "' WHERE DevId = '"
					+ devId + "' AND ExpId = '" + expId + "'";
			DbConnector.getInstance().updateQuery(Uquery);
		}
		else {
			String query = "INSERT into expertise(DevId, ExpId, Probability) VALUES "
					+ "('" + devId + "','" +  expId + "','" + probability + "')";
			DbConnector.getInstance().updateQuery(query);
		}
	}

	public void updatePackageProbability(int devId, String opackage, double percentage){
		if(opackage!=null){
			double eProb = this.getPackageProbabbility(devId, opackage);
			if( eProb != 0) {
				//				int fileCount = this.getFilesCount();
				int fileCount = ClassMap.getInstance().getProjectClasses().size();
				double newProb = (((eProb/100)*(fileCount-1) + (percentage/100)) / fileCount)*100;
				String Uquery = "UPDATE packageExpertise SET percentage = '" + newProb + "' WHERE DevId = '" + devId + "'";
				DbConnector.getInstance().updateQuery(Uquery);
			}
			else {
				String query = "INSERT into packageExpertise(DevId,package, percentage) VALUES "
						+ "('" + devId + "','" + opackage + "','" + percentage + "')";
				DbConnector.getInstance().updateQuery(query);
			}
		}
	}

	public int getExpertiseId(String expertise){
		try{
			int expertiseId = 0;
			String query = "SELECT ExpId FROM dictionary WHERE name='" + expertise + "'";
			ResultSet results = DbConnector.getInstance().executeQuery(query);
			while(results.next()){
				expertiseId = results.getInt("ExpId");
				return expertiseId;
			}
		} catch (NullPointerException e){
			return 0;
		} catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}

	public void addToDictionary(String expertise){
		try {
			String retrieveQuery = "SELECT name FROM dictionary";
			ResultSet results = DbConnector.getInstance().executeQuery(retrieveQuery);
			Set<String> temp = new HashSet<String>();
			while(results.next()){
				temp.add(results.getString("name"));
			}
			if(!temp.contains(expertise)){
				String query = "INSERT into dictionary(name) VALUES "
						+ "('" + expertise + "')";
				DbConnector.getInstance().updateQuery(query);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}



	public double getPackageProbabbility(int devId, String opackage){
		try {
			String query = "SELECT percentage FROM packageExpertise WHERE DevId = '" +  devId + "'" + "package = '" +  opackage + "'";
			ResultSet rs = DbConnector.getInstance().executeQuery(query);
			double prob = 0;
			while(rs.next()){
				prob = rs.getDouble("percentage");
				return prob;
			}
		} catch (NullPointerException e){
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public double getExpertiseProbability(int devId,int ExpId){
		try {
			String query = "SELECT probability FROM expertise WHERE DevId = '" +  devId + "' AND ExpId = '" +  ExpId + "'";
			ResultSet rs = DbConnector.getInstance().executeQuery(query);
			double prob = 0;
			while(rs.next()){
				prob = rs.getDouble("probability");
				return prob;
			}
		} catch (NullPointerException e){
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public int getDeveloper(String name){
		try {
			String query = "SELECT DevId From developer WHERE DevName = '" +  name + "'";
			ResultSet rs = DbConnector.getInstance().executeQuery(query);
			int devId = 0;
			while(rs.next()){
				devId = rs.getInt("DevId");
				return devId;
			}
		} catch (NullPointerException e){
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public boolean checkForDeveloper(String name){
		try {
			String Dname = "";
			String query = "SELECT * From developer WHERE DevName = '" +  name + "'";
			ResultSet rs = DbConnector.getInstance().executeQuery(query);
			while(rs.next()){
				Dname = rs.getString("DevName");
			}
			return Dname.equals(name);
		} catch (NullPointerException e) {
			return false;
		} catch (SQLException e){
			return false;
		}
	}

	public void addDeveloper(String name){
		if(!checkForDeveloper(name)){
			String query = "INSERT into developer(DevName) VALUES ('" + name + "')";
			DbConnector.getInstance().updateQuery(query);	
		}
	}

	public Set<String> readDictionary(){
		try {
			String query = "SELECT name FROM dictionary";
			Set<String> dictionary = new HashSet<String>();
			ResultSet results = DbConnector.getInstance().executeQuery(query);
			while(results.next()){
				dictionary.add(results.getString("name"));
			}
			return dictionary;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void addMethodExpertise(int devId, String expertise, double percentage ){
		int expId = this.getExpertiseId(expertise);
		String opackage = Dictionary.getInstance().getImport(expertise);
		int val = this.getMethodExpertise(devId, expId);
		if(val == 0){
			String query = "INSERT into methodexpertise(DevId,ExpId, package, percentage) VALUES "
					+ "('" + devId + "','" + expId + "','" + opackage + "','" + percentage + "')";
			DbConnector.getInstance().updateQuery(query);
		}
		else {
			String query = "UPDATE methodexpertise SET percentage = '" + percentage + "'"
					+ " WHERE DevId = '" + devId + "' AND ExpId = '" + expId + "'";
			DbConnector.getInstance().updateQuery(query);
		}
	}

	public int getMethodExpertise(int devId, int expId){
		try {
			String query = "SELECT percentage FROM methodexpertise WHERE DevId = '" + devId + "' AND ExpId = '" + expId + "'";
			ResultSet rs = DbConnector.getInstance().executeQuery(query);
			int percentage = 0;
			while(rs.next()){
				percentage = rs.getInt("percentage");
				return percentage;
			}
		} catch (NullPointerException e){
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void countFiles(){
		int count = this.getFilesCount();
		if(count == 0){
			String Iquery = "INSERT into files(Id,count) VALUES('1','1')";
			DbConnector.getInstance().updateQuery(Iquery);
		}
		else {
			String query = "UPDATE files SET count = '" + ++count + "' WHERE Id = '1'";
			DbConnector.getInstance().updateQuery(query);
		}
	}

	public int getFilesCount(){
		try {
			String queryS = "SELECT count FROM files WHERE Id = '1'";
			ResultSet rs = DbConnector.getInstance().executeQuery(queryS);
			int count = 0;
			while(rs.next()){
				count = rs.getInt("count");
				return count;
			}
		} catch (NullPointerException e){
			return 0;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return 0;
	}

	public void emptyFileTable(){
		String query = "UPDATE files SET count = '0' WHERE Id = '1'";
		DbConnector.getInstance().updateQuery(query);
	}

	public HashMap<String,Integer> getDeveloperPercantage(int DevId){
		try {
			String packageName;
			int percentage;
			HashMap<String,Integer> percentageMap = new HashMap<String, Integer>();
			String query = "SELECT * FROM packageexpertise WHERE DevId = '" + DevId + "'";
			ResultSet rs = DbConnector.getInstance().executeQuery(query);
			while(rs.next()){
				packageName = rs.getString("package");
				percentage = rs.getInt("percentage");
				percentageMap.put(packageName, percentage);
			}
			return percentageMap;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
