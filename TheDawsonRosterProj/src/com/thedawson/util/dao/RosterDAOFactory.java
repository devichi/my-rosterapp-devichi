package com.thedawson.util.dao;

import javax.naming.InitialContext;

/**
 * Factory that generates DAO object necessary for data access
 * @author Victor Lau
 *
 */
public class RosterDAOFactory {
	private static RosterDAO dao = null;
	
	public RosterDAO getDao() {
	  
		//Roster DAO has already been set so simply return it
		if(dao != null) {
			return dao;
		}
	  
		//Roster DAO has not been set, create an instance
		//Use DAO class implementation set in web.xml
		try {
			InitialContext ic = new InitialContext();
			String daoImplClass = (String) ic.lookup("java:comp/env/DAO.Impl");
			
			System.out.println("Dao Impl Class: " + daoImplClass);
			
			dao = (RosterDAO) Class.forName(daoImplClass).newInstance();
			
			System.out.println("DAO has been set with a new instance");
			
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return dao;
	}
	
	public static void main(String[] args) {
		RosterDAOFactory rf = new RosterDAOFactory();
		RosterDAO rd = rf.getDao();
		
		System.out.println ("Is dao object null: " + (rd==null));
		
		System.out.println("Creating a connection with Factory DAO object");
		
		//rd.connect();
		
		System.out.println("Factory DAO connection completed");
		
	}
}
