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
	
	//ThreadLocal access to the singleton instance of the database connection
	private static final ThreadLocal<DBManager> DBMS_INSTANCE = new ThreadLocal<DBManager>(){
		@Override
		protected DBManager initialValue() {
			return new DBManager();
		}
	};
	
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
		return DBMS_INSTANCE.get();
	}
	
	/**
	 * Determines if there is a connection and result set and creates both if none exists.
	 * @return a connection object to the database
	 */
	public void openConnection() {
		
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
	 * Closes all the connections to the database if they are set.  Uses package DBUtils.
	 */
	public void closeConnection() {
		System.out.println("Closing Connection");
		DbUtils.closeQuietly(conn, state, rs);
	}
	
	/**
	 * Manually sets the auto commit state
	 * @param state true for on, false for off
	 * @throws SQLException 
	 */
	public void setAutoCommit(boolean state) throws SQLException {
		if(conn != null || !conn.isClosed()) {
			conn.setAutoCommit(state);
		}
		else {
			throw new SQLException ("Connection is closed, cannot set Auto Commit: " + state);
		}
	}
	
	/**
	 * Manually commits any transaction on the connection
	 * @throws SQLException
	 */
	public void commit() throws SQLException {
		if(conn != null || !conn.isClosed()) {
			conn.commit();
		}
		else  {
			throw new SQLException ("Connection is closed, cannot commit transactions");
		}
	}
	
	public void rollback() throws SQLException {
		if(conn != null || !conn.isClosed()) {
			conn.rollback();
		}
		else  {
			throw new SQLException ("Connection is closed, cannot rollback transactions");
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
	
	/* Executes all INSERT, UPDATE, or DELETE statements to the database and returns all generated key values if any
	 * @param sqlsWithAkgRows a list of sql statements and their corresponding auto gen key columns to be returned
	 * @param closeTrans determines if at the end of this method the transaction and connection should be closed
	 *        must be closed on a 2nd call if not
	 * @return A list of Auto Generated Keys from insert, update, or delete if applicable.
	 */
	public ArrayList<Integer> executeQueryUpdateAuto(HashMap<String, String[]> sqlsWithAkgCols) {
		//Open Connection
		this.openConnection();
		
		Savepoint sp = null;
		ArrayList<Integer> genKeysList = new ArrayList<Integer>();
		
		try {
			//Ensure all database operations are transactions by setting turning off autocommit
			conn.setAutoCommit(false);

			//Create Savepoint
			sp = conn.setSavepoint("EQU_SP1");

			//Execute Query Multiple
			//Traverse the collection of queries mapped to possible array of auto gen key columns
			//Run the queries and retrieve the auto gen key created if an insert with fields are provided
			Iterator<Map.Entry<String, String[]>> iter = sqlsWithAkgCols.entrySet().iterator();
			
			while (iter.hasNext()) {
				Map.Entry<String, String[]> elem = iter.next();
				String[] akgCols = elem.getValue();
				
				//Execute one query update at a time
				Integer aKey = this.executeOneQueryUpdate(elem.getKey(), akgCols);
				
				//Populate the Arraylist in order of the query run
				//Add a generated key or null value to the genKeys array always
				//Add gen key when row headings provided or null if not (i.e. UPDATE, DELETE, or INSERT with no akg's)
				genKeysList.add(aKey);
				
				//Reset the resultset for the next iteration.
				rs = null;
			}
			
			//Commit all transaction from the batch DML statements
			conn.commit();

			//Auto Commit On, end transaction
			conn.setAutoCommit(true);
			
			
		} catch (SQLException se) {
			se.printStackTrace();
			
			//An error occured processing a DML statement, rollback whole transaction and return null
			try { conn.rollback(sp); } catch (SQLException se1) { se1.printStackTrace(); }
			genKeysList = null;
		}
		
		//Close Connection
		this.closeConnection();
		
		return genKeysList;
	}
	
	/** Executes one query INSERT, UPDATE, DELETE on the database
	 * @param sql a string represent a SQL statemetn
	 * @param akgCols a string array with column headings or null for returning auto gen keys for this query
	 * @return either the value of the auto generated key in the database or null for no auto gen key returned
	 */
	public Integer executeOneQueryUpdate(String sql, String[] akgCols) throws SQLException {
		
		//Check if the connection is available for the manual execution case
		if(conn == null || conn.isClosed()) {
			throw new SQLException ("Connection is closed, cannot execute the one query");
		}
		
		PreparedStatement ps = null;		
			
		ps = conn.prepareStatement(sql, akgCols);
		int rowCount = ps.executeUpdate();

		System.out.println("Rowcount on exec Update: " + rowCount);

		//If a statement was run that changed nothing in the database that was expected to, throw SQL error
		if(rowCount == 0) {
			throw new SQLException("Execute Query on sql string that updated no database rows");
		}

		//Only request generated keys if there is list of row headings from method parameters
		if(akgCols != null) {
			rs = ps.getGeneratedKeys();
		}

		//Determine if the sql statement yielded any generated keys, if not then resultset is null 
		if(rs != null) {
			rs.next();
			int curKey = rs.getInt(1);
			System.out.println("ID: " + curKey);

			return new Integer(curKey);
		}

		return null;			
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
