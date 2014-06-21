/**
 * 
 */
package com.thedawson.util.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.dbutils.DbUtils;

import com.sun.rowset.CachedRowSetImpl;

/**
 * Manages the database connection objects and encapsulates database functions.
 * @author Victor Lau
 */
public class DBManager {

	private DataSource ods = null;
	private Connection conn = null;
	private Statement state = null;
	private ResultSet rs = null;
	
	//Retrieves the Data Source
	private DBManager() {
		try {
			Context ic = new InitialContext();
			ods = (DataSource)ic.lookup("java:comp/env/jdbc/rosterdb");
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}
	
	/**
	 * Static method to return the singleton instance of a DBManager
	 * @return
	 */
	public static DBManager getInstance() {
		return DBManagerSingleton.DBMS_INSTANCE;
	}
	
	/**
	 * Determines if there is a connection and result set and creates both if none exists.
	 * @return a connection object to the database
	 */
	private void openConnection() {
		
		try {
			if(conn == null || conn.isClosed()) {
				
				System.out.println("In Open Connection - Connection is null or closed");
				
				//Create connection and statement
				//Reset resultset back to null
				conn = ods.getConnection();
				state = conn.createStatement();
				rs = null;
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	
	/**
	 * Executes any select statements to the database and returns the results
	 * @return Cached version of the resultset
	 */
	public CachedRowSet executeQuerySelect(String sql) {
		this.openConnection();

		CachedRowSet crs = null;
		
		try {
			System.out.println("Running Query in executeQuerySelect: " + sql);
			
			rs = state.executeQuery(sql);
			crs = new CachedRowSetImpl();
			crs.populate(rs);
			
		} catch (SQLException se) {
			se.printStackTrace();
		}

		this.closeConnection();
		
		return crs;
	}
	
	/**
	 * Executes all INSERT, UPDATE, or DELETE statements to the database and returns all generated key values if any
	 * @return A list of Auto Generated Keys from insert, update, or delete if applicable.
	 */
	public ArrayList<Integer> executeQueryUpdate(HashMap<String, String[]> sqlsWithAkgRows) {
		this.openConnection();
		
		ArrayList<Integer> genKeysList = new ArrayList<Integer>();
		Savepoint sp = null;
		PreparedStatement ps = null;
		
		try {			
			//Ensure all database operations are transactions by setting turning off autocommit
			conn.setAutoCommit(false);
			
			//Create Savepoint
			sp = conn.setSavepoint("EQU_SP1");
			
			//Use an iterator to traverse the collection of queries with possible string array of auto gen key fields
			//Run the queries and retrieve the auto gen key generated if an insert with fields are provided
			Iterator<Map.Entry<String, String[]>> iter = sqlsWithAkgRows.entrySet().iterator();
			
			while (iter.hasNext()) {
				Map.Entry<String, String[]> elem = iter.next();
				String[] agkRows = elem.getValue();
				
				ps = conn.prepareStatement(elem.getKey(), agkRows);
				int rowCount = ps.executeUpdate();
				
				System.out.println("Rowcount on exec Update: " + rowCount);
				
				//If a statement was run that changed nothing in the database that was expected to, throw SQL error
				if(rowCount == 0) {
					throw new SQLException("Execute Query on sql string that updated no database rows");
				}
				
				//Only request generated keys if there is list of row headings from method parameters
				if(agkRows != null) {
					rs = ps.getGeneratedKeys();
				}
				
				//Determine if the sql statement yielded any generated keys, if not then it's null
				//Add a generated key or null value to the genKeys array always
				//Add gen key when row headings provided or null if not (i.e. UPDATE, DELETE, or INSERT with no agk's)
				Integer aKey = null; 
						
				if(rs != null) {
					rs.next();
					int curKey = rs.getInt(1);
					System.out.println("ID: " + curKey);

					aKey = new Integer(curKey);
					
					//Populate the Arraylist in order of the query run
					genKeysList.add(aKey);
				}
				else {
					genKeysList.add(null);
				}
				
				//Reset the resultset for the next iteration.
				rs = null;
			}
			
			//Commit all transaction from the batch DML statements
			conn.commit();
			
			//Re-establish autocommit
			conn.setAutoCommit(true);
			
		} catch (SQLException se) {
			se.printStackTrace();
			
			//An error occured processing a DML statement, rollback whole transaction and return null
			try { conn.rollback(sp); } catch (SQLException se1) { se1.printStackTrace(); }
			genKeysList = null;
		}

		this.closeConnection();
		
		return genKeysList;
	}
	
	/**
	 * Closes all the connections to the database if they are set.  Uses package DBUtils.
	 */
	private void closeConnection() {
		System.out.println("Closing Connection");
		DbUtils.closeQuietly(conn, state, rs);
	}
	
	/**
	 * Private static class that simply holds the DBManager singleton instance.
	 */
	private static class DBManagerSingleton {
		private static final DBManager DBMS_INSTANCE = new DBManager();
	}
	
	
	/** TEST TEST TEST
	 * @param args
	 */
	public static void main(String[] args) {
		DBManager dbm = DBManager.getInstance();
		
		String quer = "SELECT * FROM employee;";
		
		CachedRowSet res = dbm.executeQuerySelect(quer);

		try {
			while (res.next()) {
				int emp_id = res.getInt(1);
				String fname = res.getString(2);
				String lname = res.getString(3);
				String email = res.getString(4);
				String uname = res.getString(5);
				String encpwd = res.getString(6);

				System.out.println("Emp Id: " + emp_id + " First: " + fname + " Last: " + lname + " Email: " + email + " User: " + uname + " Pass: " + encpwd);
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}

}
