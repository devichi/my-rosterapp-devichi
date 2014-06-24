/**
 * 
 */
package com.thedawson.util.model;

/**
 * Model class to store Hotel information from database.
 * @author Victor Lau
 */
public class HotelModel {
	private int hotelId;
	private String hotelName;
	private String hotelAddress;
	private String hotelCity;
	private String hotelCountry;
	private String hotelTelephoneNum;
	private String hotelFaxNum;
	private boolean isActive;
	
	//Constructor
	public HotelModel(int hotelId, String hotelName, String hotelAddress,
			String hotelCity, String hotelCountry, String hotelTelephoneNum,
			String hotelFaxNum, boolean isActive) {
		this.hotelId = hotelId;
		this.hotelName = hotelName;
		this.hotelAddress = hotelAddress;
		this.hotelCity = hotelCity;
		this.hotelCountry = hotelCountry;
		this.hotelTelephoneNum = hotelTelephoneNum;
		this.hotelFaxNum = hotelFaxNum;
		this.isActive = isActive;
	}

	//Getters and Setters for the class
	public int getHotelId() {
		return hotelId;
	}

	public void setHotelId(int hotelId) {
		this.hotelId = hotelId;
	}

	public String getHotelName() {
		return hotelName;
	}

	public void setHotelName(String hotelName) {
		this.hotelName = hotelName;
	}

	public String getHotelAddress() {
		return hotelAddress;
	}

	public void setHotelAddress(String hotelAddress) {
		this.hotelAddress = hotelAddress;
	}

	public String getHotelCity() {
		return hotelCity;
	}

	public void setHotelCity(String hotelCity) {
		this.hotelCity = hotelCity;
	}

	public String getHotelCountry() {
		return hotelCountry;
	}

	public void setHotelCountry(String hotelCountry) {
		this.hotelCountry = hotelCountry;
	}

	public String getHotelTelephoneNum() {
		return hotelTelephoneNum;
	}

	public void setHotelTelephoneNum(String hotelTelephoneNum) {
		this.hotelTelephoneNum = hotelTelephoneNum;
	}

	public String getHotelFaxNum() {
		return hotelFaxNum;
	}

	public void setHotelFaxNum(String hotelFaxNum) {
		this.hotelFaxNum = hotelFaxNum;
	}

	public boolean getIsActive() {
		return isActive;
	}

	public void setIsActive(boolean isActive) {
		this.isActive = isActive;
	}
	
}
