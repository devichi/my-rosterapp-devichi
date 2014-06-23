/**
 * 
 */
package com.thedawson.util.model;

/**
 * Model class to store Job Title information from database.
 * @author Victor Lau
 */
public class JobTitleModel {
	private int jobTitleId;
	private String jobTitleName;
	private String isActive;
	
	//Constructor
	public JobTitleModel(int jobTitleId, String jobTitleName, String isActive) {
		this.jobTitleId = jobTitleId;
		this.jobTitleName = jobTitleName;
		this.isActive = isActive;
	}

	//Getters and Setters for the class
	public int getJobTitleId() {
		return jobTitleId;
	}

	public void setJobTitleId(int jobTitleId) {
		this.jobTitleId = jobTitleId;
	}

	public String getJobTitleName() {
		return jobTitleName;
	}

	public void setJobTitleName(String jobTitleName) {
		this.jobTitleName = jobTitleName;
	}

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
}
