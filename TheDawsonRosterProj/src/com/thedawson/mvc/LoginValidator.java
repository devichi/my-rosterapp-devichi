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
		
		JobTitleModel jtm = rd.getJobTitleById(3);
		//JobTitleModel jtm = rd.getJobTitleById(8888);
		
		if (jtm != null) {
			System.out.println("Job Title ID: " + jtm.getJobTitleId());
			System.out.println("Job Title Name: " + jtm.getJobTitleName());
		}
		
		/*
		System.out.println("Calling the DAO object add Job Title method");
		
		JobTitleModel jtm = rd.addJobTitle("Spaceman");
		
		if(jtm != null) {
			System.out.println("Finished creating job Title, Print returned JT Model object details");
			System.out.println("Job Title ID: " + jtm.getJobTitleId());
			System.out.println("Job Title Name: " + jtm.getJobTitleName());

			System.out.println("Printing all Job Titles in the database");

			for(int i=0; i < 2; i++) {
				ArrayList<JobTitleModel> jtmList = rd.getJobTitles();

				Iterator<JobTitleModel> iter = jtmList.iterator();
				while (iter.hasNext()) {
					JobTitleModel curJtm = (JobTitleModel) iter.next();

					System.out.println("JT Id: " + curJtm.getJobTitleId() + " JT title: " + curJtm.getJobTitleName());
				}

				if (i == 0) {
					System.out.println("Updating the job title just created");

					boolean updateResult = rd.updateJobTitle(jtm.getJobTitleId(), "Superstar");
					//boolean updateResult = rd.updateJobTitle(123456, "Superstar");
					
					if(updateResult == true) {
						jtm.setJobTitleName("Superstar");
						System.out.println("Job Title ID: " + jtm.getJobTitleId());
						System.out.println("Job Title Name: " + jtm.getJobTitleName());
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
	*/		
	}
	
}
