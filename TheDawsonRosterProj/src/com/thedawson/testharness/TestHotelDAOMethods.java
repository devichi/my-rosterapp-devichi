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
public class TestHotelDAOMethods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		out.println("The POST WORKS!!!!  VIctor");
		
		DAOFactory df = DAOFactory.getInstance();
		
		
		JobTitleModel jtm = df.getRosterDao().getJobTitleById(5);
		//JobTitleModel jtm = df.getRosterDao().getJobTitleById(8888);
		
		if (jtm != null) {
			System.out.println("Job Title ID: " + jtm.getJobTitleId());
			System.out.println("Job Title Name: " + jtm.getJobTitleName());
			System.out.println("Job Title Active: " + jtm.getIsActive());
		}
		
		System.out.println("Calling the DAO object add Job Title method");
		
		jtm = df.getRosterDao().addJobTitle("Spaceman");
		//jtm = df.getRosterDao().addJobTitle(null);
		
		if(jtm != null) {
			System.out.println("Finished creating job Title, Print returned JT Model object details");
			System.out.println("Job Title ID: " + jtm.getJobTitleId());
			System.out.println("Job Title Name: " + jtm.getJobTitleName());
			System.out.println("Job Title isActive: " + jtm.getIsActive());

			System.out.println("Printing all Job Titles in the database");

			for(int i=0; i < 3; i++) {
				ArrayList<JobTitleModel> jtmList = df.getRosterDao().getAllJobTitles();

				Iterator<JobTitleModel> iter = jtmList.iterator();
				while (iter.hasNext()) {
					JobTitleModel curJtm = (JobTitleModel) iter.next();

					System.out.println("JT Id: " + curJtm.getJobTitleId() + " JT title: " + curJtm.getJobTitleName() + 
										" isActive: " + curJtm.getIsActive());
				}

				if (i == 0) {
					System.out.println("Updating the job title just created");

					boolean updateResult = df.getRosterDao().updateJobTitle(jtm.getJobTitleId(), "Superstar");
					//boolean updateResult = df.getRosterDao().updateJobTitle(123456, "Superstar");
					
					if(updateResult == true) {
						jtm.setJobTitleName("Superstar");
						System.out.println("Job Title ID: " + jtm.getJobTitleId());
						System.out.println("Job Title Name: " + jtm.getJobTitleName());
						System.out.println("Job Title isActive: " + jtm.getIsActive());
					}
				}
				else if(i == 1) {
					System.out.println("Changing the active status of job title to No");
					
					boolean csResult = df.getRosterDao().setJobTitleActiveStatus(jtm.getJobTitleId(), false);
					//boolean csResult = df.getRosterDao().setJobTitleActiveStatus(88888889, false);
					
					if(csResult == true) {
						jtm.setIsActive(false);
						System.out.println("Job Title ID: " + jtm.getJobTitleId());
						System.out.println("Job Title Name: " + jtm.getJobTitleName());
						System.out.println("Job Title isActive: " + jtm.getIsActive());
					}
				}
			}

			System.out.println("Deleting the job title just created");

			boolean deleteResult = df.getRosterDao().removeJobTitle(jtm.getJobTitleId());
			//boolean deleteResult = df.getRosterDao().removeJobTitle(99999999);
			
			System.out.println("Remove Job Title Successful: " + deleteResult);
		}
		else {
			System.out.println("SQL Error in addJobTitle, job title model is: " + jtm);
		}
	}
	
}
