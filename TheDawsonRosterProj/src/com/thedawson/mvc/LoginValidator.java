package com.thedawson.mvc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.thedawson.util.dao.RosterDAO;
import com.thedawson.util.dao.RosterDAOFactory;
import com.thedawson.util.model.EmployeeModel;
import com.thedawson.util.model.JobTitleModel;

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
		
		
		RosterDAOFactory rdf = new RosterDAOFactory();
		RosterDAO rd = rdf.getDao();
		
		/*
		EmployeeModel em = rd.addEmployee("Danny", "Fodor", "danny.fodor@gmail.com", "dfod", "dfod123", 1, 4);
		//EmployeeModel em = rd.addEmployee("Danny", "Fodor", "danny.fodor@gmail.com", "dfod", "dfod123", 1, 9);
		
		if(em != null) {
			System.out.println("FName: " + em.getFirstName() + " LName: " + em.getLastName() + " Email: " + em.getEmail() 
							+ " User: " + em.getUserName() + " Pwd: " + em.getEncrPassword() + " isActive: " + em.getIsActive());
		
		}
		else {
			System.out.println("Employee Model Null: " + (em == null));
		}
		*/
		
		/*JobTitleModel jtm = rd.getJobTitleById(3);
		//JobTitleModel jtm = rd.getJobTitleById(8888);
		
		if (jtm != null) {
			System.out.println("Job Title ID: " + jtm.getJobTitleId());
			System.out.println("Job Title Name: " + jtm.getJobTitleName());
			System.out.println("Job Title Active: " + jtm.getIsActive());
		}
		*/
		
		
		System.out.println("Calling the DAO object add Job Title method");
		
		JobTitleModel jtm = rd.addJobTitle("Spaceman");
		//JobTitleModel jtm = rd.addJobTitle(null);
		
		if(jtm != null) {
			System.out.println("Finished creating job Title, Print returned JT Model object details");
			System.out.println("Job Title ID: " + jtm.getJobTitleId());
			System.out.println("Job Title Name: " + jtm.getJobTitleName());
			System.out.println("Job Title isActive: " + jtm.getIsActive());

			System.out.println("Printing all Job Titles in the database");

			for(int i=0; i < 3; i++) {
				ArrayList<JobTitleModel> jtmList = rd.getJobTitles();

				Iterator<JobTitleModel> iter = jtmList.iterator();
				while (iter.hasNext()) {
					JobTitleModel curJtm = (JobTitleModel) iter.next();

					System.out.println("JT Id: " + curJtm.getJobTitleId() + " JT title: " + curJtm.getJobTitleName() + 
										" isActive: " + curJtm.getIsActive());
				}

				if (i == 0) {
					System.out.println("Updating the job title just created");

					boolean updateResult = rd.updateJobTitle(jtm.getJobTitleId(), "Superstar");
					//boolean updateResult = rd.updateJobTitle(123456, "Superstar");
					
					if(updateResult == true) {
						jtm.setJobTitleName("Superstar");
						System.out.println("Job Title ID: " + jtm.getJobTitleId());
						System.out.println("Job Title Name: " + jtm.getJobTitleName());
						System.out.println("Job Title isActive: " + jtm.getIsActive());
					}
				}
				else if(i == 1) {
					System.out.println("Changing the active status of job title to No");
					
					boolean csResult = rd.setJobTitleActiveStatus(jtm.getJobTitleId(), false);
					
					if(csResult == true) {
						jtm.setIsActive("N");
						System.out.println("Job Title ID: " + jtm.getJobTitleId());
						System.out.println("Job Title Name: " + jtm.getJobTitleName());
						System.out.println("Job Title isActive: " + jtm.getIsActive());
					}
				}
			}

			System.out.println("Deleting the job title just created");

			boolean deleteResult = rd.removeJobTitle(jtm.getJobTitleId());
			//boolean deleteResult = rd.removeJobTitle(99999999);
			
			System.out.println("Remove Job Title Successful: " + deleteResult);
		}
		else {
			System.out.println("SQL Error in addJobTitle, job title model is: " + jtm);
		}
		
	}
	
}
