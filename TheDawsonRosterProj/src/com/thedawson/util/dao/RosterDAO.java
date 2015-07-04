package com.thedawson.util.dao;

import java.sql.Date;
import java.util.ArrayList;

import com.thedawson.util.model.EmployeeDirModel;
import com.thedawson.util.model.EmployeeModel;
import com.thedawson.util.model.HotelModel;
import com.thedawson.util.model.JobTitleModel;
import com.thedawson.util.model.WorkDayModel;
import com.thedawson.util.model.WorkScheduleModel;
/**
 * Interface for all RosterDAO implementations
 * @author Victor Lau
 *
 */
public abstract class RosterDAO extends BaseDAO {
	public abstract JobTitleModel addJobTitle(String title);
	public abstract boolean removeJobTitle(int jobid);
	public abstract boolean setJobTitleActiveStatus(int jobid, boolean state);
	public abstract boolean updateJobTitle(int jobid, String title);
	public abstract ArrayList<JobTitleModel> getAllJobTitles();
	public abstract JobTitleModel getJobTitleById(int jobid);
	public abstract EmployeeModel addEmployee(String firstN, String lastN, String email, String userid, String pwd, int hotelid, int jobid);
	public abstract boolean removeEmployee(int empid);
	public abstract boolean setEmployeeActiveStatus(int empid, boolean state);
	public abstract boolean updateEmployee(int empid, String firstN, String lastN, String email, String userid, String pwd, int hotelid, 
			int jobid);
	public abstract ArrayList<EmployeeModel> getAllEmployees();
	public abstract EmployeeModel getEmployeeById(int empid);
	public abstract EmployeeDirModel addEmployeeDir(int hotelid, int empid, int jobid);
	public abstract boolean removeEmployeeDir(int empdirid);
	public abstract boolean setEmployeeDirActiveStatus(int empdirid, boolean state);
	public abstract boolean updateEmployeeDir(int empdirid, int hotelid, int empid, int jobid);
	public abstract ArrayList<EmployeeDirModel> getAllEmployeeDirs();
	public abstract EmployeeDirModel getEmployeeDirById(int empdirid);
	public abstract HotelModel addHotel(String hname, String haddr, String hcity, String hcntry, String hphone, String hfax);
	public abstract boolean removeHotel(int hotelid);
	public abstract boolean setHotelActiveStatus(int hotelid, boolean state);
	public abstract boolean updateHotel(int hotelid, String hname, String haddr, String hcity, String hcntry, String hphone, String hfax);
	public abstract ArrayList<HotelModel> getAllHotels();
	public abstract HotelModel getHotelById(int hotelid);
	public abstract WorkScheduleModel addNextWorkSchedule(int hotelid, Date startD, Date endD);
	public abstract boolean deleteAWorkSchedule(int wsid);
	public abstract boolean updateCurWorkSchedule(int wsid, int hotelid, Date startD, Date endD);
	public abstract ArrayList<WorkScheduleModel> getAllSchedules();
	public abstract ArrayList<WorkScheduleModel> getSchedulesByDate(Date startD, Date endD);
	public abstract WorkScheduleModel getScheduleByID(int wsid);
	public abstract WorkDayModel addWorkDay(int wkschedid, int empid, int jobid, Date shiftDate, double shiftLen);
	public abstract boolean deleteAWorkDay(int wdid);
	public abstract boolean updateAWorkDay(int wdid, int wkschedid, int empid, int jobid, Date shiftDate, double shiftLen);
	public abstract ArrayList<WorkDayModel> getWorkDaysByDate(Date startD, Date endD);
}
