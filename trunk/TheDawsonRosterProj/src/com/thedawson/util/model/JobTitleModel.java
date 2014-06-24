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
	private boolean isActive;
	
	//Constructor
	public JobTitleModel(int jobTitleId, String jobTitleName, boolean isActive) {
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

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
