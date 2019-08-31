package com.engine.hive;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ConnectionFactory {
	
	static private Connection connection;
	static{
		try {
			Class.forName("org.apache.hive.jdbc.HiveDriver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	public static Connection getConnection()  {
		try {
			connection=DriverManager.getConnection("jdbc:hive2://localhost:10000/roshan","roshan","hibuddy");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return connection;
	}
	public static void shutdownConnection(){
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
