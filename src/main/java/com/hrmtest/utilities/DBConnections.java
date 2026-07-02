package com.hrmtest.utilities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.Logger;

import com.hrmtest.base.BaseClass;

public class DBConnections {
	
	private static final String DB_url="jdbc:mysql://localhost:3306/orangehrm";
	private static final String DB_username="root";
	private static final String DB_password="";
	public static final Logger logger=BaseClass.logger;
	
	public static Connection getDBConnection() {
		
		try {
			logger.info("Starting DB Connection");
			Connection conn = DriverManager.getConnection(DB_url, DB_username, DB_password);
			logger.info(" DB Connection established");
			return conn;
		} catch (SQLException e) {
			logger.error("DB connection not established"+e.getMessage());
			return null;
		}	
	}
	
	
	public static Map<String,String> getEmployeeDetails(String employee_id){
		
		String query="SELECT emp_firstname,emp_middle_name,emp_lastname FROM `hs_hr_employee` WHERE employee_id=2 ";
		
		Map<String,String> employeeDetails =new HashMap<String,String>();
		
		try(Connection conn=getDBConnection();
				Statement stmt= conn.createStatement();
				ResultSet rs= stmt.executeQuery(query))
		{
			logger.info("Executing query: " + query);
			if(rs.next()) {
				String firstName= rs.getString("emp_firstname");
				String middleName=rs.getString("emp_middle_name");
				String lastName=rs.getString("emp_lastname");
				
				employeeDetails.put("empFirstName", firstName);
				employeeDetails.put("empMiddleName", middleName!=null?middleName:"");
				employeeDetails.put("empLastName", lastName);
				
				logger.info("Query Executed Successfully");
				logger.info("Employee Details found"+employeeDetails);
			}
			else {
				logger.error("Employee Details not found");
			}		
			
		} catch (SQLException e) {
			logger.error("Error while executing query");
			e.printStackTrace();
		}
				
		return employeeDetails;
	}

}
