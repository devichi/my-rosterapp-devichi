package com.thedawson.util.model;

/**
 * Model class to store Employee information from database.
 * @author Victor Lau
 *
 */
public class EmployeeModel {
	private int employeeId;
	private String firstName;
	private String lastName;
	private String userName;
	private String encrPassword;
	
	//Constructor
	public EmployeeModel(int employeeId, String firstName, String lastName, String userName, String encrPassword) {
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.userName = userName;
		this.encrPassword = encrPassword;
	}

	//Getters and Setters for the class
	public int getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(int employeeId) {
		this.employeeId = employeeId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getEncrPassword() {
		return encrPassword;
	}

	public void setEncrPassword(String encrPassword) {
		this.encrPassword = encrPassword;
	}
	
}
