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
	public boolean removeEmployee(int empid);
	public boolean setEmployeeActiveStatus(int empid, boolean state);
	public boolean updateEmployee(int empid, String firstN, String lastN, String email, String userid, String pwd, int hotelid, int jobid);
	public ArrayList<EmployeeModel> getAllEmployees();
	public EmployeeModel getEmployeeById(int empid);
	
	public EmployeeDirModel addEmployeeDir(int hotelid, int empid, int jobid);
	public boolean removeEmployeeDir(int empdirid);
	public boolean setEmployeeDirActiveStatus(int empdirid, boolean state);
	public boolean updateEmployeeDir(int empdirid, int hotelid, int empid, int jobid);
	public ArrayList<EmployeeDirModel> getAllEmployeeDirs();
	public EmployeeModel getEmployeeDirById(int empdirid);
	
	public HotelModel addHotel(String hname, String haddr, String hcity, String hcntry, String hphone, String hfax);
	public boolean removeHotel(int hotelid);
	public boolean setHotelActiveStatus(int hotid, boolean state);
	public boolean updateHotel(int hotelid, String hname, String haddr, String hcity, String hcntry, String hphone, String hfax);
	public ArrayList<HotelModel> getAllHotels();
	public HotelModel getHotelById(int hotelid);
	
	public WorkScheduleModel addNextWorkSchedule(int hotelid, Date startD, Date endD);
	public boolean deleteAWorkSchedule(int ws_id);
	public boolean updateCurWorkSchedule(int ws_id, int hotelid, Date startD, Date endD);
	public ArrayList<WorkScheduleModel> getSchedulesByDate(Date startD, Date endD);
	public WorkScheduleModel getScheduleByID(int ws_id);
	
	public WorkDayModel addWorkDay(int wrksched_id, int emp_id, int job_id, Date shift_date, double shift_len);
	public boolean deleteAWorkDay(int wd_id);
	public boolean updateAWorkDay(int wd_id, int wrksched_id, int emp_id, int job_id, Date shift_date, double shift_len);
	public ArrayList<WorkDayModel> getWorkDaysByDate(Date startD, Date endD);
	public ArrayList<WorkDayModel> getWorkDaysBySchedIDs(ArrayList<Integer> sched_ids);
}
