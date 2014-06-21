package com.thedawson.util.dao;

//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;


//import oracle.jdbc.pool.OracleDataSource;
//import org.apache.commons.dbutils.DbUtils;
import com.thedawson.util.model.EmployeeModel;
import com.thedawson.util.model.HotelModel;
import com.thedawson.util.model.JobTitleModel;
import com.thedawson.util.model.WorkDayModel;
import com.thedawson.util.model.WorkScheduleModel;

public class RosterDAOOracleImpl implements RosterDAO {
	
	//The DB object used to connect to DB and run queries
	private DBManager dbm = DBManager.getInstance();
	
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
		
		HashMap<String, String[]> sqlWithAkgRows = new HashMap<String, String[]>();
		ArrayList<Integer> genKeysList = null;
		JobTitleModel jtm = null;

		//Create SQL Query and execute it
		String sql = "INSERT INTO jobtitle VALUES (null, '" + title + "')";
		String[] akgRows = {"job_id"};
		sqlWithAkgRows.put(sql, akgRows);

		System.out.println(sql);

		//Retrieve the auto generated key from the database
		genKeysList = dbm.executeQueryUpdate(sqlWithAkgRows);
		
		//If there was a SQL Error in executeQueryUpdate then genKeyList will be null
		if(genKeysList == null) {
			return jtm;
		}
		
		//No error, so process genKeyList
		Integer jtGenKey = genKeysList.get(0);
		int jobGk = jtGenKey.intValue();
		
		//Create the job title object model for return
		System.out.println("Generated Key Job Title: " + jobGk);
		jtm = new JobTitleModel(jobGk, title);
		
		return jtm;
	}



	/* Deletes a job title from the database
	 * @see com.thedawson.util.dao.RosterDAO#removeJobTitle(int)
	 * @param the job title database id to remove
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	@Override
	public boolean removeJobTitle(int jobid) {
		HashMap<String, String[]> sqlWithAkgRows = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String sql = "DELETE FROM jobtitle WHERE job_id = " + jobid;
		sqlWithAkgRows.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if(dbm.executeQueryUpdate(sqlWithAkgRows) != null) {
			return true;
		}
		
		return false;
	}



	/* Updates the job title from the database
	 * @see com.thedawson.util.dao.RosterDAO#updateJobTitle(int, java.lang.String)
	 * @param the job title database id to update and the title value to update the row with
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	@Override
	public boolean updateJobTitle(int jobid, String title) {
		HashMap<String, String[]> sqlWithAkgRows = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String sql = "UPDATE jobtitle SET job_title = '" + title + "' WHERE job_id = " + jobid;
		sqlWithAkgRows.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if (dbm.executeQueryUpdate(sqlWithAkgRows) != null) {
			return true;
		}
		
		return false;
	}



	/* Retrieve all the job titles in the jobtitle table in the database
	 * @see com.thedawson.util.dao.RosterDAO#getJobTitles()
	 * @return the arraylist of all the job title rows in the database in model objects
	 */
	@Override
	public ArrayList<JobTitleModel> getJobTitles() {
		ArrayList<JobTitleModel> jtmList = new ArrayList<JobTitleModel>();
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM jobtitle";
		
		System.out.println(sql);
		
		CachedRowSet crs = dbm.executeQuerySelect(sql);
		
		try {
			while (crs.next()) {
				
				int jobId = crs.getInt(1);
				String jobTitle = crs.getString(2);

				jtmList.add(new JobTitleModel(jobId, jobTitle));
			}
		} catch (SQLException se) {
			jtmList = null;
			se.printStackTrace();
		}
		
		return jtmList;
	}



	/* Retrieves a particular job title from the jobtitle table in the database based on job id
	 * @see com.thedawson.util.dao.RosterDAO#getJobTitleById(int)
	 * @param jobid the job id you want to retrieve from the database.
	 * @return a job title database result in model form with the associated jobid
	 */
	@Override
	public JobTitleModel getJobTitleById(int jobid) {
		JobTitleModel jtm = null;
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM jobtitle where job_id = " + jobid;
		
		System.out.println(sql);
		
		CachedRowSet crs = dbm.executeQuerySelect(sql);
		
		//If crs is empty then no row exists with provided jobid, return null job title model below
		//Otherwise do the try catch and create job title model object
		if(crs.size() != 0) {
			try {
				crs.next();

				int jobId = crs.getInt(1);
				String jobTitle = crs.getString(2);

				jtm = new JobTitleModel(jobId, jobTitle);

			} catch (SQLException se) {
				jtm = null;
				se.printStackTrace();
			}
		}
		
		return jtm;
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

}
