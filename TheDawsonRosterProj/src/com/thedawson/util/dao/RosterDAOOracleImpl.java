package com.thedawson.util.dao;

//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.Statement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.sql.rowset.CachedRowSet;

import com.thedawson.util.model.EmployeeDirModel;
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
		
		//Ensure the title is not null value first, then proceed
		if(title == null) {
			return null;
		}
		
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		ArrayList<Integer> genKeysList = null;
		JobTitleModel jtm = null;

		//Create SQL Query and execute it
		String sql = "INSERT INTO jobtitle VALUES (null, '" + title + "', 'Y')";
		String[] akgCols = {"job_id"};
		sqlsWithAkgCols.put(sql, akgCols);

		System.out.println(sql);

		//Retrieve the auto generated key from the database
		genKeysList = dbm.executeQueryUpdateAuto(sqlsWithAkgCols);
		
		//If there was a SQL Error in executeQueryUpdateAuto then genKeyList will be null
		if(genKeysList == null) {
			return jtm;
		}
		
		//No error, so process genKeyList
		Integer jtGenKey = genKeysList.get(0);
		int jobGk = jtGenKey.intValue();
		
		//Create the job title object model for return
		System.out.println("Generated Key Job Title: " + jobGk);
		jtm = new JobTitleModel(jobGk, title, true);
		
		return jtm;
	}



	/* Deletes a job title from the database
	 * @see com.thedawson.util.dao.RosterDAO#removeJobTitle(int)
	 * @param the job title database id to remove
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	@Override
	public boolean removeJobTitle(int jobid) {
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String sql = "DELETE FROM jobtitle WHERE job_id = " + jobid;
		sqlsWithAkgCols.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if(dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}
		
		return true;
	}

	
	
	/* Changes the Active status of the job title
	 * @param state true for activate, false for make inactive
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	@Override
	public boolean setJobTitleActiveStatus(int jobid, boolean state) {
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String status = null;
		
		if(state) {
			status = "Y";
		}
		else {
			status = "N";
		}
			
		
		String sql = "UPDATE jobtitle SET is_active = '" + status + "' WHERE job_id = " + jobid;
		sqlsWithAkgCols.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if(dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}
		
		return true;
	}


	/* Updates the job title from the database
	 * @see com.thedawson.util.dao.RosterDAO#updateJobTitle(int, java.lang.String)
	 * @param the job title database id to update and the title value to update the row with
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	@Override
	public boolean updateJobTitle(int jobid, String title) {	
		
		//Ensure the title is not null value first, then proceed
		if(title == null) {
			return false;
		}
		
		HashMap<String, String[]> sqlWithAkgCols = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String sql = "UPDATE jobtitle SET job_title = '" + title + "' WHERE job_id = " + jobid;
		sqlWithAkgCols.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if (dbm.executeQueryUpdateAuto(sqlWithAkgCols) == null) {
			return false;
		}
		
		return true;
	}



	/* Retrieve all the job titles in the jobtitle table in the database
	 * @see com.thedawson.util.dao.RosterDAO#getJobTitles()
	 * @return the arraylist of all the job title rows in the database in model objects
	 */
	@Override
	public ArrayList<JobTitleModel> getAllJobTitles() {
		ArrayList<JobTitleModel> jtmList = new ArrayList<JobTitleModel>();
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM jobtitle";
		
		System.out.println(sql);
		
		CachedRowSet crs = dbm.executeQuerySelect(sql);
		
		try {
			while (crs.next()) {
				
				int jobId = crs.getInt(1);
				String jobTitle = crs.getString(2);
				String isActive = crs.getString(3);
				
				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				jtmList.add(new JobTitleModel(jobId, jobTitle, isActiveBool));
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
				String isActive = crs.getString(3);
				
				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				jtm = new JobTitleModel(jobId, jobTitle, isActiveBool);

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
				
				//Ensure all of the String parameters are not null first, then proceed
				if(firstN == null || lastN == null || email == null || userid == null || pwd == null) {
					return null;
				}
				
				EmployeeModel em = null;

				//Create SQL Query and execute it
				try {
					//Open a manual connection
					dbm.openConnection();
					
					//Set Auto Commit off manually
					dbm.setAutoCommit(false);
					
					//Execute query to insert into employee table
					String sql = "INSERT INTO employee VALUES (null, '" + firstN + "', '" + lastN + "', '" + email + "', '" 
							+ userid + "', '" + pwd + "', " + "'Y')"; 
					String[] akgCols = {"e_id"};

					System.out.println(sql);

					//Retrieve the auto generated key from the database
					Integer empGk = dbm.executeOneQueryUpdate(sql, akgCols);

					System.out.println("Generated Key Employee: " + empGk);
					
					//If no auto gen keys were returned then there's a problem
					if(empGk == null) {
						throw new SQLException ("Query failed to retrieve generated keys when expected");
					}

					//No error, so continue processing
					//Execute query to insert into employee directory table
					String sql2 = "INSERT INTO employeedir VALUES (null, " + hotelid + ", " + empGk + ", " + jobid + ", 'Y')";
					String[] akgCols2 = {"empldir_id"};

					System.out.println(sql2);

					//Commit and close all connections with true parameter after the query update
					Integer empDirGk = dbm.executeOneQueryUpdate(sql2, akgCols2);

					System.out.println("Generated Key Employee Dir: " + empDirGk);
					
					//Create the employee object model for return
					em = new EmployeeModel(empGk, firstN, lastN, email, userid, pwd, true);
					
					//Commit the transaction
					dbm.commit();
					
					//Set Auto Commit back to on manually
					dbm.setAutoCommit(true);
					
					//Close Connection manually
					dbm.closeConnection();
					
				} catch (SQLException se) {
					se.printStackTrace();
					
					//Rollback manually
					try { dbm.rollback(); } catch (SQLException se2) { se2.printStackTrace(); }
					em = null;
				}
				
				return em;
	}

	
	
	/* Deletes an employee from the database
	 * @see com.thedawson.util.dao.RosterDAO#removeEmployee(int)
	 * @param the employee database id to remove
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	@Override
	public boolean removeEmployee(int empid) {
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		//Delete from Employee Directory first since there are constraints, cannot delete Employee first
		String sql = "DELETE FROM employeedir WHERE emp_id = " + empid;
		sqlsWithAkgCols.put(sql, null);
		
		System.out.println(sql);
		
		//Add the remove Employee query 2nd in the queue
		String sql2 = "DELETE FROM employee WHERE e_id = " + empid;
		sqlsWithAkgCols.put(sql2, null);
		
		System.out.println(sql2);
		
		//Execute the db removal from Employee Dir and Employee in one call.
		if(dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}
		
		return true;
	}
	
	
	
	/* Changes the Active status of the employee
	 * @param state true for activate, false for make inactive
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	@Override
	public boolean setEmployeeActiveStatus(int empid, boolean state) {
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String status = null;
		
		if(state) {
			status = "Y";
		}
		else {
			status = "N";
		}
			
		String sql = "UPDATE employee SET is_active = '" + status + "' WHERE e_id = " + empid;
		sqlsWithAkgCols.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if(dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}
		
		return true;
	}


	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateEmployee(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	@Override
	public boolean updateEmployee(int empid, String firstN, String lastN,
			String email, String userid, String pwd, int hotelid, int jobid) {
		
		//Ensure all String parameters are not null first, then proceed
		if(firstN == null || lastN == null || email == null || userid == null || pwd == null) {
			return false;
		}

		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();

		//Create SQL Query and execute it
		String sql = "UPDATE employee SET first_name = '" + firstN + "', "
				+ "last_name = '" + lastN + "', "
				+ "email = '" + email + "', "
				+ "username = '" + userid + "', "
				+ "encr_pwd = '" + pwd + "' "
				+ " WHERE e_id = " + empid;
		sqlsWithAkgCols.put(sql, null);

		System.out.println(sql);

		//Execute the db removal
		if (dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}

		return true;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllEmployees()
	 */
	@Override
	public ArrayList<EmployeeModel> getAllEmployees() {
		
		ArrayList<EmployeeModel> emList = new ArrayList<EmployeeModel>();
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM employee";
		
		System.out.println(sql);
		
		CachedRowSet crs = dbm.executeQuerySelect(sql);
		
		try {
			while (crs.next()) {
				
				int empId = crs.getInt(1);
				String firstN = crs.getString(2);
				String lastN = crs.getString(3);
				String email = crs.getString(4);
				String userid = crs.getString(5);
				String pwd = crs.getString(6);
				String isActive = crs.getString(7);
				
				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				emList.add(new EmployeeModel(empId, firstN, lastN, email, userid, pwd, isActiveBool));
			}
		} catch (SQLException se) {
			emList = null;
			se.printStackTrace();
		}
		
		return emList;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getEmployeeById(int)
	 */
	@Override
	public EmployeeModel getEmployeeById(int empid) {
		EmployeeModel em = null;
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM employee where e_id = " + empid;
		
		System.out.println(sql);
		
		CachedRowSet crs = dbm.executeQuerySelect(sql);
		
		//If crs is empty then no row exists with provided empid, return null employee model below
		//Otherwise do the try catch and create employee model object
		if(crs.size() != 0) {
			try {
				crs.next();

				int empId = crs.getInt(1);
				String firstN = crs.getString(2);
				String lastN = crs.getString(3);
				String email = crs.getString(4);
				String userid = crs.getString(5);
				String pwd = crs.getString(6);
				String isActive = crs.getString(7);
				
				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				em = new EmployeeModel(empId, firstN, lastN, email, userid, pwd, isActiveBool);

			} catch (SQLException se) {
				em = null;
				se.printStackTrace();
			}
		}
		
		return em;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addEmployeeDir(int, int, int)
	 */
	@Override
	public EmployeeDirModel addEmployeeDir(int hotelid, int empid, int jobid) {
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		ArrayList<Integer> genKeysList = null;
		EmployeeDirModel edm = null;
		
		//Create SQL Query and execute it
		String sql = "INSERT INTO employeedir VALUES (null, " + hotelid + ", " + empid + ", " + jobid + ", 'Y')";
		String[] akgCols = {"empldir_id"};
		sqlsWithAkgCols.put(sql, akgCols);
		
		System.out.println(sql);

		//Commit and close all connections with true parameter after the query update
		genKeysList = dbm.executeQueryUpdateAuto(sqlsWithAkgCols);
		
		//If there was a SQL Error in executeQueryUpdateAuto then genKeyList will be null
		if(genKeysList == null) {
			return edm;
		}

		//No error, so process genKeyList
		Integer edGenKey = genKeysList.get(0);
		int edGk = edGenKey.intValue();

		//Create the emp dir object model for return
		System.out.println("Generated Key Empl Dir: " + edGk);
		edm = new EmployeeDirModel(edGk, hotelid, empid, jobid, true);

		return edm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#removeEmployeeDir(int)
	 */
	@Override
	public boolean removeEmployeeDir(int empdirid) {
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String sql = "DELETE FROM employeedir WHERE empldir_id = " + empdirid;
		sqlsWithAkgCols.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if(dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}
		
		return true;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#setEmployeeDirActiveStatus(int, boolean)
	 */
	@Override
	public boolean setEmployeeDirActiveStatus(int empdirid, boolean state) {
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String status = null;
		
		if(state) {
			status = "Y";
		}
		else {
			status = "N";
		}
			
		
		String sql = "UPDATE employeedir SET is_active = '" + status + "' WHERE empldir_id = " + empdirid;
		sqlsWithAkgCols.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if(dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}
		
		return true;	
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateEmployeeDir(int, int, int, int)
	 */
	@Override
	public boolean updateEmployeeDir(int empdirid, int hotelid, int empid,
			int jobid) {

		HashMap<String, String[]> sqlWithAkgCols = new HashMap<String, String[]>();

		//Create SQL Query and execute it
		String sql = "UPDATE employeedir SET hotel_id = '" + hotelid + "', "
				+ "emp_id = '" + empid + "' "
				+ "WHERE empldir_id = " + empdirid;
		sqlWithAkgCols.put(sql, null);

		System.out.println(sql);

		//Execute the db removal
		if (dbm.executeQueryUpdateAuto(sqlWithAkgCols) == null) {
			return false;
		}

		return true;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllEmployeeDirs()
	 */
	@Override
	public ArrayList<EmployeeDirModel> getAllEmployeeDirs() {
		ArrayList<EmployeeDirModel> edList = new ArrayList<EmployeeDirModel>();
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM employeedir";
		
		System.out.println(sql);
		
		CachedRowSet crs = dbm.executeQuerySelect(sql);
		
		try {
			while (crs.next()) {
				
				int empdirId = crs.getInt(1);
				int hotelId = crs.getInt(2);
				int empId = crs.getInt(3);
				int jobId = crs.getInt(4);
				String isActive = crs.getString(5);
				
				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				edList.add(new EmployeeDirModel(empdirId, hotelId, empId, jobId, isActiveBool));
			}
		} catch (SQLException se) {
			edList = null;
			se.printStackTrace();
		}
		
		return edList;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getEmployeeDirById(int)
	 */
	@Override
	public EmployeeDirModel getEmployeeDirById(int empdirid) {
		EmployeeDirModel edm = null;
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM employeedir where empldir_id = " + empdirid;
		
		System.out.println(sql);
		
		CachedRowSet crs = dbm.executeQuerySelect(sql);
		
		//If crs is empty then no row exists with provided empdirid, return null employee dir model below
		//Otherwise do the try catch and create employee dir model object
		if(crs.size() != 0) {
			try {
				crs.next();

				int empdirId = crs.getInt(1);
				int hotelId = crs.getInt(2);
				int empId = crs.getInt(3);
				int jobId = crs.getInt(4);
				String isActive = crs.getString(5);
				
				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				edm = new EmployeeDirModel(empdirId, hotelId, empId, jobId, isActiveBool);

			} catch (SQLException se) {
				edm = null;
				se.printStackTrace();
			}
		}
		
		return edm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addHotel(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public HotelModel addHotel(String hname, String haddr, String hcity,
			String hcntry, String hphone, String hfax) {
		
		//Ensure none of the String parameters are null first, then proceed
		if(hname == null || haddr == null || hcity == null || hcntry == null || hphone == null || hfax == null) {
			return null;
		}

		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		ArrayList<Integer> genKeysList = null;
		HotelModel hm = null;

		//Create SQL Query and execute it
		String sql = "INSERT INTO hotel VALUES (null, '" + hname + "', '" + haddr + "', '" + hcity + "', '" + hcntry + "', '" 
							+ hphone + "', '" + hfax + "', 'Y')";
		String[] akgCols = {"h_id"};
		sqlsWithAkgCols.put(sql, akgCols);

		System.out.println(sql);

		//Retrieve the auto generated key from the database
		genKeysList = dbm.executeQueryUpdateAuto(sqlsWithAkgCols);

		//If there was a SQL Error in executeQueryUpdateAuto then genKeyList will be null
		if(genKeysList == null) {
			return hm;
		}

		//No error, so process genKeyList
		Integer hGenKey = genKeysList.get(0);
		int hotGk = hGenKey.intValue();

		//Create the hotel object model for return
		System.out.println("Generated Key Hotel: " + hotGk);
		hm = new HotelModel(hotGk, hname, haddr, hcity, hcntry, hphone, hfax, true);

		return hm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#removeHotel(int)
	 */
	@Override
	public boolean removeHotel(int hotelid) {
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String sql = "DELETE FROM hotel WHERE h_id = " + hotelid;
		sqlsWithAkgCols.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if(dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}
		
		return true;
	}


	/* Changes the Active status of the job title
	 * @param state true for activate, false for make inactive
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	@Override
	public boolean setHotelActiveStatus(int hotelid, boolean state) {
		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();
		
		//Create SQL Query and execute it
		String status = null;
		
		if(state) {
			status = "Y";
		}
		else {
			status = "N";
		}
			
		
		String sql = "UPDATE hotel SET is_active = '" + status + "' WHERE h_id = " + hotelid;
		sqlsWithAkgCols.put(sql, null);
		
		System.out.println(sql);
		
		//Execute the db removal
		if(dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}
		
		return true;
	}
	

	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateHotel(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public boolean updateHotel(int hotelid, String hname, String haddr,
			String hcity, String hcntry, String hphone, String hfax) {
		
		//Ensure the String parameters are not null first, then proceed
		if(hname == null || haddr == null || hcity == null || hcntry == null || hphone == null || hfax == null) {
			return false;
		}

		HashMap<String, String[]> sqlsWithAkgCols = new HashMap<String, String[]>();

		//Create SQL Query and execute it
		String sql = "UPDATE hotel SET h_name = '" + hname + "', "
				+ "h_addr = '" + haddr + "', "
				+ "h_city = '" + hcity + "', "
				+ "h_country = '" + hcntry + "', "
				+ "h_tel = '" + hphone + "', "
				+ "h_fax = '" + hfax + "' "
				+ " WHERE h_id = " + hotelid;
		sqlsWithAkgCols.put(sql, null);

		System.out.println(sql);

		//Execute the db removal
		if (dbm.executeQueryUpdateAuto(sqlsWithAkgCols) == null) {
			return false;
		}

		return true;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllHotels()
	 */
	@Override
	public ArrayList<HotelModel> getAllHotels() {
		ArrayList<HotelModel> hList = new ArrayList<HotelModel>();
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM hotel";
		
		System.out.println(sql);
		
		CachedRowSet crs = dbm.executeQuerySelect(sql);
		
		try {
			while (crs.next()) {
				
				int hotelId = crs.getInt(1);
				String hName = crs.getString(2);
				String hAddr = crs.getString(3);
				String hCity = crs.getString(4);
				String hCntry = crs.getString(5);
				String hTel = crs.getString(6);
				String hFax = crs.getString(7);
				String isActive = crs.getString(8);
				
				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				hList.add(new HotelModel(hotelId, hName, hAddr, hCity, hCntry, hTel, hFax, isActiveBool));
			}
		} catch (SQLException se) {
			hList = null;
			se.printStackTrace();
		}
		
		return hList;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getHotelById(int)
	 */
	@Override
	public HotelModel getHotelById(int hotelid) {
		HotelModel hm = null;
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM hotel where h_id = " + hotelid;
		
		System.out.println(sql);
		
		CachedRowSet crs = dbm.executeQuerySelect(sql);
		
		//If crs is empty then no row exists with provided h_id, return null hotel model below
		//Otherwise do the try catch and create hotel model object
		if(crs.size() != 0) {
			try {
				crs.next();

				int hotelId = crs.getInt(1);
				String hName = crs.getString(2);
				String hAddr = crs.getString(3);
				String hCity = crs.getString(4);
				String hCntry = crs.getString(5);
				String hTel = crs.getString(6);
				String hFax = crs.getString(7);
				String isActive = crs.getString(8);
				
				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				hm = new HotelModel(hotelId, hName, hAddr, hCity, hCntry, hTel, hFax, isActiveBool);

			} catch (SQLException se) {
				hm = null;
				se.printStackTrace();
			}
		}
		
		return hm;
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
	public boolean deleteAWorkSchedule(int ws_id) {
		// TODO Auto-generated method stub
		return false;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateCurWorkSchedule(int, int, java.util.Date, java.util.Date)
	 */
	@Override
	public boolean updateCurWorkSchedule(int ws_id, int hotelid, Date startD,
			Date endD) {
		// TODO Auto-generated method stub
		return false;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllSchedules()
	 */
	@Override
	public ArrayList<WorkScheduleModel> getAllSchedules() {
		// TODO Auto-generated method stub
		return null;
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
	public boolean deleteAWorkDay(int wd_id) {
		// TODO Auto-generated method stub
		return false;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateAWorkDay(int, int, int, int, java.util.Date, double)
	 */
	@Override
	public boolean updateAWorkDay(int wd_id, int wrksched_id, int emp_id,
			int job_id, Date shift_date, double shift_len) {
		// TODO Auto-generated method stub
		return false;
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
