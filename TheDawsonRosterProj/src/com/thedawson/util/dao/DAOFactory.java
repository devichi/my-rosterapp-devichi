package com.thedawson.util.dao;

import java.sql.SQLException;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

/**
 * Factory that generates DAO object necessary for data access
 * @author Victor Lau
 *
 */
public class DAOFactory {
	//ThreadLocal access to the singleton instance of the database connection
	private static final ThreadLocal<DAOFactory> DBMS_INSTANCE = new ThreadLocal<DAOFactory>(){
		@Override
		protected DAOFactory initialValue() {
			return new DAOFactory();
		}
	};
		
	private static DataSource ods = null;
	private static RosterDAO dao = null;
	
	//Retrieves the Data Source
	private DAOFactory() {
		try {
			Context ic = new InitialContext();
			ods = (DataSource)ic.lookup("java:comp/env/jdbc/tdrosterdb");
			
			System.out.println("Oracle Data Source null: " + (ods == null));
		} catch (NamingException ne) {
			ne.printStackTrace();
		}
	}
	
	/**
	 * Static method to return the singleton instance of a DAOFactory
	 * @return
	 */
	public static DAOFactory getInstance() {
		return DBMS_INSTANCE.get();
	}
	
	public RosterDAO getRosterDao() {
	  
		//Roster DAO has already been set so simply return it
		if(dao != null) {
			try {
				if(dao.getConnection() == null || dao.getConnection().isClosed()) {
					dao.setConnection(ods.getConnection());
				}
			} catch(SQLException se) {
				se.printStackTrace();
				return null;
			}
			
			return dao;
		}
	  
		//Roster DAO has not been set, create an instance
		//Use DAO class implementation set in web.xml
		try {
			InitialContext ic = new InitialContext();
			String daoImplClass = (String) ic.lookup("java:comp/env/DAO.Impl");
			
			System.out.println("Dao Impl Class: " + daoImplClass);
			
			dao = (RosterDAO) Class.forName(daoImplClass).newInstance();
			dao.setConnection(ods.getConnection());
			
			System.out.println("DAO has been set with a new instance");
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return dao;
	}
	
	public static void main(String[] args) {
		DAOFactory rf = DAOFactory.getInstance();
		RosterDAO rd = rf.getRosterDao();
		
		System.out.println ("Is dao object null: " + (rd==null));
		
		System.out.println("Creating a connection with Factory DAO object");
		
		//rd.connect();
		
		System.out.println("Factory DAO connection completed");
		
	}
}
