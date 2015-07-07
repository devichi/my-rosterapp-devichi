package com.thedawson.testharness;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Iterator;

import com.thedawson.util.dao.DAOFactory;
import com.thedawson.util.model.*;

/**
 * Servlet implementation class LoginValidator
 */
public class TestEmployeeDAOMethods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		out.println("The POST WORKS!!!!  VIctor");
		
		DAOFactory df = DAOFactory.getInstance();
		
		//Test all 3 variations of get all employees Y/N/All
		for(int k = 0; k < 3; k++) {
			ArrayList<EmployeeModel> emListGAE = null; 
					
			switch(k) {
			case 0: 
				System.out.println("Only Active Employees");
				emListGAE = df.getRosterDao().getAllEmployees("Y");
				break;
			case 1:
				System.out.println("Only Inactive Employees");
				emListGAE = df.getRosterDao().getAllEmployees("N");
				break;
			case 2:
				System.out.println("All Employees");
				emListGAE = df.getRosterDao().getAllEmployees(null);
				break;
			}
	
			Iterator<EmployeeModel> iterGAE = emListGAE.iterator();
			while (iterGAE.hasNext()) {
				EmployeeModel curEmGAE = (EmployeeModel) iterGAE.next();
	
				System.out.println("Eid: " + curEmGAE.getEmployeeId() + " FName: " + curEmGAE.getFirstName() + " LName: " + curEmGAE.getLastName() + " Email: " + curEmGAE.getEmail() 
						+ " User: " + curEmGAE.getUserName() + " Pwd: " + curEmGAE.getEncrPassword() + " isActive: " + curEmGAE.getIsActive());
			}
		}
		
		EmployeeModel em = df.getRosterDao().getEmployeeById(7777);
		System.out.println("Get Employee By Id Invalid Result: " + em);
		
		em = df.getRosterDao().getEmployeeById(4);
		
		
		if (em != null) {
			System.out.println("Eid: " + em.getEmployeeId() + " FName: " + em.getFirstName() + " LName: " + em.getLastName() + " Email: " + em.getEmail() 
					+ " User: " + em.getUserName() + " Pwd: " + em.getEncrPassword() + " isActive: " + em.getIsActive());
		}
		
		System.out.println("Calling the DAO object add Employee method");
		
		em = df.getRosterDao().addEmployee("Danny", "Fodor", "danny.fodor@gmail.com", "dfod", "dfod123", 1, 9);
		System.out.println("Add Employee with invalid job id: " + em);
		
		em = df.getRosterDao().addEmployee("Danny", "Fodor", "danny.fodor@gmail.com", "dfod", "dfod123", 99, 4);
		System.out.println("Add Employee with invalid hotel id: " + em);
		
		//Valid Employee Add
		em = df.getRosterDao().addEmployee("Danny", "Fodor", "danny.fodor@gmail.com", "dfod", "dfod123", 1, 4);
		
		if(em != null) {
			System.out.println("Finished creating Employee, Print returned Emp Model object details");
			System.out.println("EId: " + em.getEmployeeId() + " FName: " + em.getFirstName() + " LName: " + em.getLastName() + " Email: " + em.getEmail() 
					+ " User: " + em.getUserName() + " Pwd: " + em.getEncrPassword() + " isActive: " + em.getIsActive());
			
			System.out.println("Printing the entire employee dir to show newly created employee details");
			
			ArrayList<EmployeeDirModel> emDirList = df.getRosterDao().getAllEmployeeDirs(null);

			Iterator<EmployeeDirModel> iter_dir = emDirList.iterator();
			while (iter_dir.hasNext()) {
				EmployeeDirModel curemdir = (EmployeeDirModel) iter_dir.next();

				System.out.println("EdirId: " + curemdir.getEmployeeDirId() + " HotId: " + curemdir.getHotelId() 
						 + " EmpId: " + curemdir.getEmployeeId() + " JobId: " + curemdir.getJobId() + " isActive: " + curemdir.getIsActive());
			}
			
			System.out.println("Printing all Employees in the database");

			for(int i=0; i < 3; i++) {
				ArrayList<EmployeeModel> emList = df.getRosterDao().getAllEmployees(null);

				Iterator<EmployeeModel> iter = emList.iterator();
				while (iter.hasNext()) {
					EmployeeModel curem = (EmployeeModel) iter.next();

					System.out.println("EId: " + curem.getEmployeeId() + " FName: " + curem.getFirstName() + " LName: " + curem.getLastName() + " Email: " + curem.getEmail() 
							+ " User: " + curem.getUserName() + " Pwd: " + curem.getEncrPassword() + " isActive: " + curem.getIsActive());
				}

				if (i == 0) {
					System.out.println("Updating the employee just created");

					boolean updateResult = false;
					
					//Test various scenarios for updating employee
					for(int j=0; j <9; j++) {
						
						if(j == 0) {
							//Incorrect employee Id test
							updateResult = df.getRosterDao().updateEmployee(123456, "Debbie", "Doolittle", "deb.doo@gmail.com", "ddoo", "ddoo123");
						}
						else if (j == 1) {
							//Unique Email tests
							updateResult = df.getRosterDao().updateEmployee(em.getEmployeeId(), "Debbie", "Doolittle", "vchilau@gmail.com", "ddoo", "ddoo123");	
						}
						else if (j == 2) {
							//Unique Userid tests
							updateResult = df.getRosterDao().updateEmployee(em.getEmployeeId(), "Debbie", "Doolittle", "deb.doo@gmail.com", "sjun", "ddoo123");
						}
						else if (j == 3) {
							//Email Constraint tests - no @ sign
							updateResult = df.getRosterDao().updateEmployee(em.getEmployeeId(), "Debbie", "Doolittle", "deb.doogmail.com", "ddoo", "ddoo123");
						}
						else if (j == 4) {
							//Email Constraint tests - invalid char before @
							updateResult = df.getRosterDao().updateEmployee(em.getEmployeeId(), "Debbie", "Doolittle", "!deb.doo@gmail.com", "ddoo", "ddoo123");
						}
						else if (j == 5) {
							//Email Constraint tests - invald char after @
							updateResult = df.getRosterDao().updateEmployee(em.getEmployeeId(), "Debbie", "Doolittle", "deb.doo@gm%ail.com", "ddoo", "ddoo123");
						}
						else if (j == 6) {
							//Email Constraint tests - char length too short after .
							updateResult = df.getRosterDao().updateEmployee(em.getEmployeeId(), "Debbie", "Doolittle", "deb.doo@gmail.c", "ddoo", "ddoo123");
						}
						else if (j == 7) {
							//Email Constraint tests - char length too long after .
							updateResult = df.getRosterDao().updateEmployee(em.getEmployeeId(), "Debbie", "Doolittle", "deb.doo@gmail.commm", "ddoo", "ddoo123");
						}
						else {
							//Valid update scenario
							updateResult = df.getRosterDao().updateEmployee(em.getEmployeeId(), "Debbie", "Doolittle", "deb.doo@gmail.com", "ddoo", "ddoo123");
						}
						
						if(updateResult == true) {
							em.setFirstName("Debbie");
							em.setLastName("Doolittle");
							em.setEmail("deb.doo@gmail.com");
							em.setUserName("ddoo");
							em.setEncrPassword("ddoo123");
							System.out.println("EId: " + em.getEmployeeId() + " FName: " + em.getFirstName() + " LName: " + em.getLastName() + " Email: " + em.getEmail() 
									+ " User: " + em.getUserName() + " Pwd: " + em.getEncrPassword() + " isActive: " + em.getIsActive());
						}
						else {
							System.out.println("Failed Update Scenario #" + j + ": " + updateResult);
						}
					}
				}
				else if(i == 10) {
					System.out.println("Changing the active status of employee to No");
					
					boolean csResult = df.getRosterDao().setEmployeeActiveStatus(88888889, false);
					System.out.println("Set Employee Active Status invalid employee id: " + csResult);
					
					csResult = df.getRosterDao().setEmployeeActiveStatus(em.getEmployeeId(), false);
					
					if(csResult == true) {
						em.setIsActive(false);
						System.out.println("FName: " + em.getFirstName() + " LName: " + em.getLastName() + " Email: " + em.getEmail() 
								+ " User: " + em.getUserName() + " Pwd: " + em.getEncrPassword() + " isActive: " + em.getIsActive());
					}
				}
			}

			System.out.println("Setting the active status of employee created to No and its directory entries to No to sim Delete");

			boolean deactResult = df.getRosterDao().deactivateEmployee(888888);
			System.out.println("Deactivate employee bad Emp Id: " + deactResult);
			
			deactResult = df.getRosterDao().deactivateEmployee(em.getEmployeeId());
			System.out.println("Deactivate employee Successful: " + deactResult);
		}
		else {
			System.out.println("SQL Error in addEmployee, employee model is: " + em);
		}
	}
	
}
