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
public class TestEmployeeDirDAOMethods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		out.println("The POST WORKS!!!!  VIctor");
		
		DAOFactory df = DAOFactory.getInstance();
		
		//Test all 3 variations of get all employee directories Y/N/All
		for(int k = 0; k < 3; k++) {
			ArrayList<EmployeeDirModel> edListGAED = null; 
					
			switch(k) {
			case 0: 
				System.out.println("Only Active Employee Dirs");
				edListGAED = df.getRosterDao().getAllEmployeeDirs("Y");
				break;
			case 1:
				System.out.println("Only Inactive Employees Dirs");
				edListGAED = df.getRosterDao().getAllEmployeeDirs("N");
				break;
			case 2:
				System.out.println("All Employees Dirs");
				edListGAED = df.getRosterDao().getAllEmployeeDirs(null);
				break;
			}
	
			Iterator<EmployeeDirModel> iterGAED = edListGAED.iterator();
			while (iterGAED.hasNext()) {
				EmployeeDirModel curEmGAE = (EmployeeDirModel) iterGAED.next();
	
				System.out.println("ED_ID: " + curEmGAE.getEmployeeDirId() + " ED_Hotel: " + curEmGAE.getHotelId() + " ED_EmpId: " + curEmGAE.getEmployeeId() +
						" ED_JobID: " + curEmGAE.getJobId() + " ED_Active: " + curEmGAE.getIsActive());
			}
		}
		
		
		EmployeeDirModel edm = df.getRosterDao().getEmployeeDirById(8888);
		System.out.println("Get Employee Dir invalid ED Id: " + edm);
		
		edm = df.getRosterDao().getEmployeeDirById(5);
		
		
		if (edm != null) {
			System.out.println("ED_ID: " + edm.getEmployeeDirId() + " ED_Hotel: " + edm.getHotelId() + " ED_EmpId: " + edm.getEmployeeId() +
					" ED_JobID: " + edm.getJobId() + " ED_Active: " + edm.getIsActive());
		}
		
		System.out.println("Calling the DAO object add Employee Dir method");
		
		//Error Check Add Employee - Add entry already in Empl Dir
		edm = df.getRosterDao().addEmployeeDir(1, 2, 4);
		System.out.println("Error Check Add Emp - Entry already in ED: " + edm);
		
		//Error Check Add Employee - Add Invalid Hotel ID
		edm = df.getRosterDao().addEmployeeDir(4, 2, 4);
		System.out.println("Error Check Add Emp - Invalid Hotel ID: " + edm);
		
		//Error Check Add Employee - Add Inactive Hotel ID
		edm = df.getRosterDao().addEmployeeDir(2, 2, 4);
		System.out.println("Error Check Add Emp - Invalid Hotel ID: " + edm);
		
		//Error Check Add Employee - Add Invalid Emp ID
		edm = df.getRosterDao().addEmployeeDir(3, 10, 4);
		System.out.println("Error Check Add Emp - Invalid Emp ID: " + edm);
		
		//Error Check Add Employee - Add Inactive Emp ID
		edm = df.getRosterDao().addEmployeeDir(3, 5, 2);
		System.out.println("Error Check Add Emp - Inactive Emp ID: " + edm);
		
		//Error Check Add Employee - Add Invalid Job ID
		edm = df.getRosterDao().addEmployeeDir(3, 3, 999);
		System.out.println("Error Check Add Emp - Invalid Job ID: " + edm);
		
		//Error Check Add Employee - Add Inactive Job ID
		edm = df.getRosterDao().addEmployeeDir(3, 4, 5);
		System.out.println("Error Check Add Emp - Inactive Job ID: " + edm);
		
		//Valid Scenario
		edm = df.getRosterDao().addEmployeeDir(3, 2, 4);
		
		
		if(edm != null) {
			System.out.println("Finished creating Employee Dir, Print returned ED Model object details");
			System.out.println("ED_ID: " + edm.getEmployeeDirId() + " ED_Hotel: " + edm.getHotelId() + " ED_EmpId: " + edm.getEmployeeId() +
					" ED_JobID: " + edm.getJobId() + " ED_Active: " + edm.getIsActive());

			System.out.println("Printing all Employee Dirs in the database");

			for(int i=0; i < 3; i++) {
				ArrayList<EmployeeDirModel> edmList = df.getRosterDao().getAllEmployeeDirs(null);

				Iterator<EmployeeDirModel> iter = edmList.iterator();
				while (iter.hasNext()) {
					EmployeeDirModel curedm = (EmployeeDirModel) iter.next();

					System.out.println("ED_ID: " + curedm.getEmployeeDirId() + " ED_Hotel: " + curedm.getHotelId() + " ED_EmpId: " + curedm.getEmployeeId() +
							" ED_JobID: " + curedm.getJobId() + " ED_Active: " + curedm.getIsActive());
				}

				if (i == 0) {
					System.out.println("Updating the Employee Dir just created");
					boolean updateResult = false;

					//Error Check Update Employee - Update entry already in Empl Dir
					updateResult = df.getRosterDao().updateEmployeeDir(edm.getEmployeeDirId(), 1, 4, 3);
					System.out.println("Error Check Update Emp - Entry already in ED: " + updateResult);
					
					//Error Check Update Employee - Update Invalid Hotel ID
					updateResult = df.getRosterDao().updateEmployeeDir(edm.getEmployeeDirId(), 2222, 4, 1);
					System.out.println("Error Check Update Emp - Invalid Hotel ID: " + updateResult);
					
					//Error Check Update Employee - Update Inactive Hotel ID
					updateResult = df.getRosterDao().updateEmployeeDir(edm.getEmployeeDirId(), 2, 2, 2);
					System.out.println("Error Check Update Emp - Invalid Hotel ID: " + updateResult);
					
					//Error Check Update Employee - Update Invalid Emp ID
					updateResult = df.getRosterDao().updateEmployeeDir(edm.getEmployeeDirId(), 1, 45, 3);
					System.out.println("Error Check Update Emp - Invalid Emp ID: " + updateResult);
					
					//Error Check Update Employee - Update Inactive Emp ID
					updateResult = df.getRosterDao().updateEmployeeDir(edm.getEmployeeDirId(), 1, 5, 2);
					System.out.println("Error Check Update Emp - Inactive Emp ID: " + updateResult);
					
					//Error Check Update Employee - Update Invalid Job ID
					updateResult = df.getRosterDao().updateEmployeeDir(edm.getEmployeeDirId(), 3, 4, 7);
					System.out.println("Error Check Update Emp - Invalid Job ID: " + updateResult);
					
					//Error Check Update Employee - Update Inactive Job ID
					updateResult = df.getRosterDao().updateEmployeeDir(edm.getEmployeeDirId(), 3, 4, 5);
					System.out.println("Error Check Update Emp - Inactive Job ID: " + updateResult);
					
					//Valid Scenario - Changed the Hotel
					//NOTE: If you change this scenario  change the below model set function and value as well
					updateResult = df.getRosterDao().updateEmployeeDir(edm.getEmployeeDirId(), 1, 2, 4);
					
					if(updateResult == true) {
						edm.setHotelId(1);
						System.out.println("ED_ID: " + edm.getEmployeeDirId() + " ED_Hotel: " + edm.getHotelId() + " ED_EmpId: " + edm.getEmployeeId() +
								" ED_JobID: " + edm.getJobId() + " ED_Active: " + edm.getIsActive());
					}
				}
				else if(i == 1) {
					System.out.println("Changing the active status of Employee Dir to No");
					
					boolean csResult = df.getRosterDao().setEmployeeDirActiveStatus(88888889, false);
					System.out.println("Change Active Status invalid Employee Dir id: " + csResult);
					
					csResult = df.getRosterDao().setEmployeeDirActiveStatus(edm.getEmployeeDirId(), false);
										
					if(csResult == true) {
						edm.setIsActive(false);
						System.out.println("ED_ID: " + edm.getEmployeeDirId() + " ED_Hotel: " + edm.getHotelId() + " ED_EmpId: " + edm.getEmployeeId() +
								" ED_JobID: " + edm.getJobId() + " ED_Active: " + edm.getIsActive());
					}
				}
			}

			System.out.println("Deleting the Employee Dir just created");

			boolean deleteResult = df.getRosterDao().removeEmployeeDir(99999999);
			System.out.println("Deleting Employee Dir with invalid Id: " + deleteResult);
			
			deleteResult = df.getRosterDao().removeEmployeeDir(edm.getEmployeeDirId());
			
			System.out.println("Remove Employee Dir Successful: " + deleteResult);
		}
		else {
			System.out.println("SQL Error in addEmployeeDir, Employee Dir model is: " + edm);
		}
	}
	
}
