/**
 * 
 */
package com.thedawson.util.model;

import java.util.Calendar;

/**
 * Model class to store Work Day information from database.
 * @author Victor Lau
 */
public class WorkDayModel {
	private int workDayId;
	private int workScheduleId;
	private int employeeId;
	private int jobTitleId;
	private Calendar shiftDateAndTime;
	
	//Constructor
	public WorkDayModel(int workDayId, int workScheduleId, int employeeId,
			int jobTitleId, Calendar shiftDateAndTime) {
		this.workDayId = workDayId;
		this.workScheduleId = workScheduleId;
		this.employeeId = employeeId;
		this.jobTitleId = jobTitleId;
		this.shiftDateAndTime = shiftDateAndTime;
	}

	//Getters and Setters for the class
	public int getWorkDayId() {
		return workDayId;
	}

	public void setWorkDayId(int workDayId) {
		this.workDayId = workDayId;
	}

	public int getWorkScheduleId() {
		return workScheduleId;
	}

	public void setWorkScheduleId(int workScheduleId) {
		this.workScheduleId = workScheduleId;
	}

	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public int getJobTitleId() {
		return jobTitleId;
	}

	public void setJobTitleId(int jobTitleId) {
		this.jobTitleId = jobTitleId;
	}

	public Calendar getShiftDateAndTime() {
		return shiftDateAndTime;
	}

	public void setShiftDateAndTime(Calendar shiftDateAndTime) {
		this.shiftDateAndTime = shiftDateAndTime;
	}
}
