/**
 * 
 */
package com.thedawson.util.model;

import java.util.Calendar;

/**
 * Model class to store Work Schedule information from database.
 * @author Victor Lau
 */
public class WorkScheduleModel {
	private int workScheduleId;
	private int hotelId;
	private Calendar startDate;
	private Calendar endDate;
	
	//Constructor
	public WorkScheduleModel(int workScheduleId, int hotelId, Calendar startDate,
			Calendar endDate) {
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

	public Calendar getStartDate() {
		return startDate;
	}

	public void setStartDate(Calendar startDate) {
		this.startDate = startDate;
	}

	public Calendar getEndDate() {
		return endDate;
	}

	public void setEndDate(Calendar endDate) {
		this.endDate = endDate;
	}
}
