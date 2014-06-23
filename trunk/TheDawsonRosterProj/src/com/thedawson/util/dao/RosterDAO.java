package com.thedawson.util.dao;

import java.util.ArrayList;
import java.util.Date;

import com.thedawson.util.model.*;

public interface RosterDAO {
	//connect is simply a test method to connect to the database
	//public void connect();
	
	//RosterDAO interface methods
	public JobTitleModel addJobTitle(String title);
	public boolean removeJobTitle(int jobid);
	public boolean setJobTitleActiveStatus(int jobid, boolean state);
	public boolean updateJobTitle(int jobid, String title);
	public ArrayList<JobTitleModel> getJobTitles();
	public JobTitleModel getJobTitleById(int jobid);
	
	public EmployeeModel addEmployee(String firstN, String lastN, String email, String userid, String pwd, int hotelid, int jobid);
	public void removeEmployee(int empid);
	public void updateEmployee(int empid, String firstN, String lastN, String email, String userid, String pwd, int hotelid, int jobid);
	public ArrayList<EmployeeModel> getAllEmployees();
	public EmployeeModel getEmployeeById(int empid);
	
	public HotelModel addHotel(String hname, String haddr, String hcity, String hcntry, String hphone, String hfax);
	public void removeHotel(int hotelid);
	public void updateHotel(int hotelid, String hname, String haddr, String hcity, String hcntry, String hphone, String hfax);
	public ArrayList<HotelModel> getAllHotels();
	public HotelModel getHotelById(int hotelid);
	
	public WorkScheduleModel addNextWorkSchedule(int hotelid, Date startD, Date endD);
	public void deleteAWorkSchedule(int ws_id);
	public void updateCurWorkSchedule(int ws_id, int hotelid, Date startD, Date endD);
	public ArrayList<WorkScheduleModel> getSchedulesByDate(Date startD, Date endD);
	public WorkScheduleModel getScheduleByID(int ws_id);
	
	public WorkDayModel addWorkDay(int wrksched_id, int emp_id, int job_id, Date shift_date, double shift_len);
	public void deleteAWorkDay(int wd_id);
	public void updateAWorkDay(int wd_id, int wrksched_id, int emp_id, int job_id, Date shift_date, double shift_len);
	public ArrayList<WorkDayModel> getWorkDaysByDate(Date startD, Date endD);
	public ArrayList<WorkDayModel> getWorkDaysBySchedIDs(ArrayList<Integer> sched_ids);
}
