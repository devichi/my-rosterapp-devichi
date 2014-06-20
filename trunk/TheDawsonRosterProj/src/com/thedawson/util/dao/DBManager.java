/**
 * 
 */
package com.thedawson.util.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import javax.sql.rowset.CachedRowSet;

import org.apache.commons.dbutils.DbUtils;

import com.sun.rowset.CachedRowSetImpl;

/**
 * Manages the database connection and resultset objects to the database.
 * @author Victor Lau
 */
public class DBManager {

	private DataSource ods = null;
	private Connection conn = null;
	private Statement state = null;
	private ResultSet rs = null;
	
	//Retrieves the Data Source
	public DBManager() {
		try {
			Context ic = new InitialContext();
			ods = (DataSource)ic.lookup("java:comp/env/jdbc/rosterdb");
			System.out.println("Datasource Null: " + (ods==null));
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}
	
	/**
	 * Determines if there is a connection and result set and creates both if none exists.
	 * @return a connection object to the database
	 */
	private void openConnection() {
		
		try {
			if(conn == null || conn.isClosed()) {

				//Create connection
				conn = ods.getConnection();
				state = conn.createStatement();
			}
		} catch (SQLException se) {
			se.printStackTrace();
		}
	}
	
	public CachedRowSet executeQuerySelect(String sql) {
		this.openConnection();

		CachedRowSet crs = null;
		
		try {
			rs = state.executeQuery(sql);
			crs = new CachedRowSetImpl();
			crs.populate(rs);
		} catch (SQLException se) {
			se.printStackTrace();
		}

		this.closeConnection();
		
		return crs;
	}
	
	public int executeQueryUpdate(String sql) {
		this.openConnection();

		int result = -1;
		
		try {
			result = state.executeUpdate(sql);
		} catch (SQLException se) {
			se.printStackTrace();
		}

		this.closeConnection();
		
		return result;
	}
	
	/**
	 * Closes all the connections to the database if they are set.  Uses package DBUtils.
	 */
	private void closeConnection() {
		DbUtils.closeQuietly(conn, state, rs);
	}
	
	/** TEST TEST TEST
	 * @param args
	 */
	public static void main(String[] args) {
		DBManager dbm = new DBManager();
		
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
