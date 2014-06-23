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
	private String email;
	private String userName;
	private String encrPassword;
	private String isActive;
	
	//Constructor
	public EmployeeModel(int employeeId, String firstName, String lastName, String email, String userName, 
						String encrPassword, String isActive) {
		this.employeeId = employeeId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.userName = userName;
		this.encrPassword = encrPassword;
		this.isActive = isActive;
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

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
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

	public String getIsActive() {
		return isActive;
	}

	public void setIsActive(String isActive) {
		this.isActive = isActive;
	}
	
}
