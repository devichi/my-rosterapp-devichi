package com.thedawson.util.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.apache.commons.dbutils.DbUtils;

/**
 * All DAO objects will extend this base DAO Class.
 * Simply manages the connection object
 * @author Victor Lau
 *
 */
public class BaseDAO {
	private Connection conn = null;
	
	protected Connection getConnection() {
		try {
			if(conn != null || !conn.isClosed()) {
				return conn;
			}
		} catch(SQLException se) {
			se.printStackTrace();
		}
		
		return null;
	}
	
	protected void setConnection(Connection conn) {
		if (conn != null) {
			this.conn = conn;
		}
	}
	
	protected void closeConnection(PreparedStatement ps, ResultSet rs) {
		DbUtils.closeQuietly(conn, ps, rs);
	}
}
