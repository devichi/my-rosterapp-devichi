package com.thedawson.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.CachedRowSet;

import com.thedawson.util.dao.DBManager;

/**
 * Servlet implementation class LoginValidator
 */
public class LoginValidator extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		out.println("The POST WORKS!!!!  VIctor");
		
		
		DBManager dbm = DBManager.getInstance();
		
		String quer = "SELECT * FROM employee";
		
		System.out.println("Creating a connection with DBManager object");
		
		CachedRowSet res = dbm.executeQuerySelect(quer);

		System.out.println ("Is CachedRowSet object null: " + (res==null));
		
		if(res != null) {
		
			System.out.println("Printing Cached Row Set results");
			
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
		
		System.out.println ("Finished DBManager Test!!!");
		
		
	}

}
