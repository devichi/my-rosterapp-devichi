/**
 * 
 */
package com.thedawson.util.model;

import java.sql.Date;

/**
 * Model class to store Work Schedule information from database.
 * @author Victor Lau
 */
public class WorkScheduleModel {
	private int workScheduleId;
	private int hotelId;
	private Date startDate;
	private Date endDate;
	
	//Constructor
	public WorkScheduleModel(int workScheduleId, int hotelId, Date startDate,
			Date endDate) {
		this.workScheduleId = workScheduleId;
		this.hotelId = hotelId;
		this.startDate = startDate;
		this.endDate = endDate;
	}

	//Getters and Setters for the class
	public int getWorkScheduleId() {
		return workScheduleId;
	}

	public void setWorkScheduleId(int workScheduleId) {
		this.workScheduleId = workScheduleId;
	}

	public int getHotelId() {
		return hotelId;
	}

	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}
}
