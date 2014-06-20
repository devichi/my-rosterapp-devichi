package com.thedawson.util.dao;

//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

//import oracle.jdbc.pool.OracleDataSource;

//import org.apache.commons.dbutils.DbUtils;

import com.thedawson.util.model.EmployeeModel;
import com.thedawson.util.model.HotelModel;
import com.thedawson.util.model.JobTitleModel;
import com.thedawson.util.model.WorkDayModel;
import com.thedawson.util.model.WorkScheduleModel;

public class RosterDAOOracleImpl implements RosterDAO {
		
	/**
	 * Method to test connection to the database.  Not used in actual application.
	 */
	/*
	@Override
	public void connect() {
		
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		try {
			OracleDataSource ods = new OracleDataSource();
			ods.setURL("jdbc:oracle:thin:@//localhost:1521/TDROSTERDB");
			ods.setUser("tdroster_admin");
			ods.setPassword("tdroster1379");

			conn = ods.getConnection();
			stmt = conn.createStatement();

			rs = stmt.executeQuery("SELECT * from employee");

			while(rs.next()) {
				int emp_id = rs.getInt(1);
				String fname = rs.getString(2);
				String lname = rs.getString(3);
				String email = rs.getString(4);
				String uname = rs.getString(5);
				String encpwd = rs.getString(6);
				
			    System.out.println("Emp Id: " + emp_id + " First: " + fname + " Last: " + lname + " Email: " + email + " User: " + uname + " Pass: " + encpwd);
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
		}
		finally {
			DbUtils.closeQuietly(rs);
			DbUtils.closeQuietly(stmt);
			DbUtils.closeQuietly(conn);
		}
	}
	*/
	
	/* Adds a job title row to the database
	 * @see com.thedawson.util.dao.RosterDAO#addJobTitle(java.lang.String)
	 * @param title The title of the new job position
	 * @return the new job title database details wrapped in a model object
	 */
	@Override
	public JobTitleModel addJobTitle(String title) {
		
		//Create Connection
		DBManager dbm = DBManager.getInstance();

		//Create SQL Query and execute it
		String sql_update = "INSERT INTO jobtitle VALUES (null, '" + title + "')";

		System.out.println(sql_update);

		//dbm.executeQueryUpdate(sql_update);
		
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#removeJobTitle(int)
	 */
	@Override
	public void removeJobTitle(int jobid) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateJobTitle(int, java.lang.String)
	 */
	@Override
	public void updateJobTitle(int jobid, String title) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getJobTitles()
	 */
	@Override
	public ArrayList<JobTitleModel> getJobTitles() {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getJobTitleById(int)
	 */
	@Override
	public JobTitleModel getJobTitleById(int jobid) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addEmployee(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public EmployeeModel addEmployee(String firstN, String lastN, String email,
			String userid, String pwd, int hotelid, int jobid) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#removeEmployee(int)
	 */
	@Override
	public void removeEmployee(int empid) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateEmployee(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public void updateEmployee(int empid, String firstN, String lastN,
			String email, String userid, String pwd, int hotelid, int jobid) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllEmployees()
	 */
	@Override
	public ArrayList<EmployeeModel> getAllEmployees() {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getEmployeeById(int)
	 */
	@Override
	public EmployeeModel getEmployeeById(int empid) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addHotel(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public HotelModel addHotel(String hname, String haddr, String hcity,
			String hcntry, String hphone, String hfax) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#removeHotel(int)
	 */
	@Override
	public void removeHotel(int hotelid) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateHotel(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public void updateHotel(int hotelid, String hname, String haddr,
			String hcity, String hcntry, String hphone, String hfax) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllHotels()
	 */
	@Override
	public ArrayList<HotelModel> getAllHotels() {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getHotelById(int)
	 */
	@Override
	public HotelModel getHotelById(int hotelid) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addNextWorkSchedule(int, java.util.Date, java.util.Date)
	 */
	@Override
	public WorkScheduleModel addNextWorkSchedule(int hotelid, Date startD,
			Date endD) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#deleteAWorkSchedule(int)
	 */
	@Override
	public void deleteAWorkSchedule(int ws_id) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateCurWorkSchedule(int, int, java.util.Date, java.util.Date)
	 */
	@Override
	public void updateCurWorkSchedule(int ws_id, int hotelid, Date startD,
			Date endD) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getSchedulesByDate(java.util.Date, java.util.Date)
	 */
	@Override
	public ArrayList<WorkScheduleModel> getSchedulesByDate(Date startD,
			Date endD) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getScheduleByID(int)
	 */
	@Override
	public WorkScheduleModel getScheduleByID(int ws_id) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addWorkDay(int, int, int, java.util.Date, double)
	 */
	@Override
	public WorkDayModel addWorkDay(int wrksched_id, int emp_id, int job_id,
			Date shift_date, double shift_len) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#deleteAWorkDay(int)
	 */
	@Override
	public void deleteAWorkDay(int wd_id) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateAWorkDay(int, int, int, int, java.util.Date, double)
	 */
	@Override
	public void updateAWorkDay(int wd_id, int wrksched_id, int emp_id,
			int job_id, Date shift_date, double shift_len) {
		// TODO Auto-generated method stub
		
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getWorkDaysByDate(java.util.Date, java.util.Date)
	 */
	@Override
	public ArrayList<WorkDayModel> getWorkDaysByDate(Date startD, Date endD) {
		// TODO Auto-generated method stub
		return null;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getWorkDaysBySchedIDs(java.util.ArrayList)
	 */
	@Override
	public ArrayList<WorkDayModel> getWorkDaysBySchedIDs(
			ArrayList<Integer> sched_ids) {
		// TODO Auto-generated method stub
		return null;
	}



	public static void main (String[] args) {
		RosterDAOOracleImpl ri = new RosterDAOOracleImpl();
		
		//ri.connect();
		//ri.addJobTitle("Sexy");
	}
}
