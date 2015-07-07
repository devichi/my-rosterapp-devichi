package com.thedawson.util.model;

public class EmployeeDirModel {
	private int employeeDirId;
	private int hotelId;
	private int employeeId;
	private int jobId;
	private boolean isActive;
	
	//Constructor
	public EmployeeDirModel(int employeeDirId, int hotelId, int employeeId,
			int jobId, boolean isActive) {
		this.employeeDirId = employeeDirId;
		this.hotelId = hotelId;
		this.employeeId = employeeId;
		this.jobId = jobId;
		this.isActive = isActive;
	}
	
	//Getters and Setters for the class
	public int getEmployeeDirId() {
		return employeeDirId;
	}

	public void setEmployeeDirId(int employeeDirId) {
		this.employeeDirId = employeeDirId;
	}
	
	public int getHotelId() {
		return hotelId;
	}
	
	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}
	
	public int getEmployeeId() {
		return employeeId;
	}
	
	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}
	
	public int getJobId() {
		return jobId;
	}
	
	public void setJobId(int jobId) {
		this.jobId = jobId;
	}
	
	public boolean getIsActive() {
		return isActive;
	}
	
	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
}
