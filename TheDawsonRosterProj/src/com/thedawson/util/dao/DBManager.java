/**
 * 
 */
package com.thedawson.util.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.sql.Statement;
import java.util.ArrayList;

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
				
				//Create connection
				conn = ods.getConnection();
				state = conn.createStatement();
				
				System.out.println("Setting AutoCommit to False");
				
				//Ensure all database operations are transactions by setting turning off autocommit
				conn.setAutoCommit(false);
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
	 * Executes all INSERT, UPDATE, or DELETE statements to the database
	 * @return A list of Auto Generated Keys from insert, update, or delete if applicable.
	 */
	public ArrayList<CachedRowSet> executeQueryUpdate(ArrayList<String> sqls) {
		this.openConnection();
		
		ArrayList<CachedRowSet> crs = new ArrayList<CachedRowSet>();
		Savepoint sp = null;
		
		try {
			//Create Savepoint
			sp = conn.setSavepoint("ECSelect_SP1");
			
			
			for (String s : sqls) {
				state.executeUpdate(s, Statement.RETURN_GENERATED_KEYS);
				rs = state.getGeneratedKeys();
				CachedRowSet oneCrs = new CachedRowSetImpl();
				oneCrs.populate(rs);
				
				//Populate the Arraylist in order of the query run from sqls Arraylist
				crs.add(oneCrs);
			}
			
			//Commit all transaction from the batch DML statements
			conn.commit();
		} catch (SQLException se) {
			se.printStackTrace();
			
			//An error occured processing a DML statement, rollback whole transaction and return null
			try { conn.rollback(sp); } catch (SQLException se1) { se1.printStackTrace(); }
			crs = null;
		}

		this.closeConnection();
		
		return crs;
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
