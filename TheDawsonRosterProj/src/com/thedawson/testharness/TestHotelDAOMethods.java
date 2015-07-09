package com.thedawson.testharness;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.util.ArrayList;
import java.util.Iterator;

import com.thedawson.util.dao.DAOFactory;
import com.thedawson.util.model.*;

/**
 * Servlet implementation class LoginValidator
 */
public class TestHotelDAOMethods extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		PrintWriter out = response.getWriter();
		
		out.println("The POST WORKS!!!!  VIctor");
		
		DAOFactory df = DAOFactory.getInstance();
		
		//Test all 3 variations of get all employee directories Y/N/All
		for(int k = 0; k < 3; k++) {
			ArrayList<HotelModel> hListGAH = null; 
					
			switch(k) {
			case 0: 
				System.out.println("Only Active Hotels");
				hListGAH = df.getRosterDao().getAllHotels("Y");
				break;
			case 1:
				System.out.println("Only Inactive Hotels");
				hListGAH = df.getRosterDao().getAllHotels("N");
				break;
			case 2:
				System.out.println("All Hotels");
				hListGAH = df.getRosterDao().getAllHotels(null);
				break;
			}
			
			Iterator<HotelModel> iterGAH = hListGAH.iterator();
			while (iterGAH.hasNext()) {
				HotelModel curHGAH = (HotelModel) iterGAH.next();
	
				System.out.println("H ID: " + curHGAH.getHotelId() + " H Name: " + curHGAH.getHotelName() + " H Addr: " + curHGAH.getHotelAddress() + 
						" H City: " + curHGAH.getHotelCity() + " H Prov: " + curHGAH.getHotelProv()+ " H Pcd: " + curHGAH.getHotelPostCd() + " H Country: " + curHGAH.getHotelCountry() +  
						" H Tel: " + curHGAH.getHotelTelephoneNum() + " H Fax: " + curHGAH.getHotelFaxNum() + " H Active: " + curHGAH.getIsActive());
			}
		}
		
		
		HotelModel hm = df.getRosterDao().getHotelById(8888);
		System.out.println("Get Hotel invalid id: " + hm);
		
		hm = df.getRosterDao().getHotelById(3);
		
		if (hm != null) {
			System.out.println("H ID: " + hm.getHotelId() + " H Name: " + hm.getHotelName() + " H Addr: " + hm.getHotelAddress() + 
					" H City: " + hm.getHotelCity() + " H Prov: " + hm.getHotelProv() + " H Pcd: " + hm.getHotelPostCd() +" H Country: " + hm.getHotelCountry() +  
					" H Tel: " + hm.getHotelTelephoneNum() + " H Fax: " + hm.getHotelFaxNum() + " H Active: " + hm.getIsActive());
		}
		
		System.out.println("Calling the DAO object add Hotel method");
		
		//Invalid Entry
		hm = df.getRosterDao().addHotel(null, null, null, null, null, null, null, null);
		System.out.println("Add Invalid Hotel: " + hm);
		
		//Valid Entry
		hm = df.getRosterDao().addHotel("Mariot", "1234 Five St.", "Montreal", "Quebec", "P2R 5Y8", "Canada", "(555)222-3333", null);
		
		if(hm != null) {
			System.out.println("Finished creating Hotel, Print returned H Model object details");
			System.out.println("H ID: " + hm.getHotelId() + " H Name: " + hm.getHotelName() + " H Addr: " + hm.getHotelAddress() + 
					" H City: " + hm.getHotelCity() + " H Prov: " + hm.getHotelProv() + " H Pcd: " + hm.getHotelPostCd() +" H Country: " + hm.getHotelCountry() +  
					" H Tel: " + hm.getHotelTelephoneNum() + " H Fax: " + hm.getHotelFaxNum() + " H Active: " + hm.getIsActive());

			System.out.println("Printing all Hotels in the database");

			for(int i=0; i < 3; i++) {
				ArrayList<HotelModel> hmList = df.getRosterDao().getAllHotels(null);

				Iterator<HotelModel> iter = hmList.iterator();
				while (iter.hasNext()) {
					HotelModel curhm = (HotelModel) iter.next();

					System.out.println("H ID: " + curhm.getHotelId() + " H Name: " + curhm.getHotelName() + " H Addr: " + curhm.getHotelAddress() + 
							" H City: " + curhm.getHotelCity() + " H Prov: " + curhm.getHotelProv() + " H Pcd: " + curhm.getHotelPostCd() + " H Country: " + curhm.getHotelCountry() +  
							" H Tel: " + curhm.getHotelTelephoneNum() + " H Fax: " + curhm.getHotelFaxNum() + " H Active: " + curhm.getIsActive());
				}

				if (i == 0) {
					System.out.println("Updating the Hotel just created");

					boolean updateResult = df.getRosterDao().updateHotel(123456, "Fountana", "2314 Paris Rd.", "Paris", "Île-de-France", "75008", "France", "(514)666-7777", "");
					System.out.println("Update Hotel invalid id: " + updateResult);
					
					updateResult = df.getRosterDao().updateHotel(hm.getHotelId(), "Fountana", "2314 Paris Rd.", "Paris", "Île-de-France", "75008", "France", "(514)666-7777", "");
					
					if(updateResult == true) {
						hm.setHotelName("Fountana");
						hm.setHotelAddress("2314 Paris Rd.");
						hm.setHotelCity("Paris");
						hm.setHotelProv("Île-de-France");
						hm.setHotelPostCd("75008");
						hm.setHotelCountry("France");
						hm.setHotelTelephoneNum("(514)666-7777");
						System.out.println("H ID: " + hm.getHotelId() + " H Name: " + hm.getHotelName() + " H Addr: " + hm.getHotelAddress() + 
								" H City: " + hm.getHotelCity() + " H Prov: " + hm.getHotelProv() + " H Pcd: " + hm.getHotelPostCd() +" H Country: " + hm.getHotelCountry() +  
								" H Tel: " + hm.getHotelTelephoneNum() + " H Fax: " + hm.getHotelFaxNum() + " H Active: " + hm.getIsActive());
					}
				}
				else if(i == 1) {
					System.out.println("Changing the active status of Hotel to No");
					
					boolean csResult = df.getRosterDao().setHotelActiveStatus(88888889, false);
					System.out.println("Hotel Active Status invalid id: " + csResult);
					
					csResult = df.getRosterDao().setHotelActiveStatus(hm.getHotelId(), false);
					
					if(csResult == true) {
						hm.setIsActive(false);
						System.out.println("H ID: " + hm.getHotelId() + " H Name: " + hm.getHotelName() + " H Addr: " + hm.getHotelAddress() + 
								" H City: " + hm.getHotelCity() + " H Prov: " + hm.getHotelProv() + " H Pcd: " + hm.getHotelPostCd() +" H Country: " + hm.getHotelCountry() +  
								" H Tel: " + hm.getHotelTelephoneNum() + " H Fax: " + hm.getHotelFaxNum() + " H Active: " + hm.getIsActive());
					}
				}
			}

			System.out.println("Deleting the Hotel just created");

			boolean deleteResult = df.getRosterDao().removeHotel(99999999);
			System.out.println("Delete Hotel invalid id: " + deleteResult);
			
			deleteResult = df.getRosterDao().removeHotel(hm.getHotelId());
			
			System.out.println("Remove Hotel Successful: " + deleteResult);
		}
		else {
			System.out.println("SQL Error in addHotel, Hotel model is: " + hm);
		}
	}
	
}
