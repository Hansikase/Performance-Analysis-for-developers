package com.fyp.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DbConnector {

	private static DbConnector instance;

	private Connection connection;

	private DbConnector(){

	}

	public static DbConnector getInstance(){
		if(instance == null){
			instance = new DbConnector();
			instance.connect();
		}
		return instance;
	}

	public void connect(){
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager
					.getConnection("jdbc:mysql://localhost:3306/expertise","root", "");

			if (conn != null) {		
				this.connection = conn;
//				System.out.println("connection made");

			} else {
				System.out.println("Failed to make connection!");
			}
		} catch (ClassNotFoundException e) {
		} catch (SQLException e){
		}
	}

	public void updateQuery(String query){
		Statement stmt = null;
		try {
			stmt = getInstance().getConnection().createStatement();
			stmt.executeUpdate(query);
		} catch (SQLException e) {			
		}
	}
	
	public ResultSet executeQuery(String query){
		Statement stmt = null;
		ResultSet rs = null;
		try {
			stmt = getInstance().getConnection().createStatement();
			rs = stmt.executeQuery(query);
		} catch (SQLException e) {
		}
		return rs;
	}

	public Connection getConnection() {
		return getInstance().connection;
	}
	
	
	//	for testing purposes only
	public static void main(String[] args){
		String query = "INSERT into Developer(DevName , email)"
				+ "VALUES ('Vince Mi','vincemi@gmail.com')";
		DbConnector.getInstance().updateQuery(query);
	}
}

