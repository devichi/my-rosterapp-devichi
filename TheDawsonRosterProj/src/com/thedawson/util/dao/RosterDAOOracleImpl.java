package com.thedawson.util.dao;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Savepoint;
import java.util.ArrayList;
import java.util.Iterator;

import com.thedawson.util.model.EmployeeDirModel;
import com.thedawson.util.model.EmployeeModel;
import com.thedawson.util.model.HotelModel;
import com.thedawson.util.model.JobTitleModel;
import com.thedawson.util.model.WorkDayModel;
import com.thedawson.util.model.WorkScheduleModel;

public class RosterDAOOracleImpl extends RosterDAO {
	
	private ResultSet rs = null;
	private PreparedStatement ps = null;
	
	/* Adds a job title row to the database
	 * @param title The title of the new job position
	 * @return the new job title database details wrapped in a model object
	 */
	public JobTitleModel addJobTitle(String title) {
		
		//Ensure the title is not null value first, then proceed
		if(title == null) {
			return null;
		}
		
		JobTitleModel jtm = null;
		
		//Create SQL Query and execute it
		String sql = "INSERT INTO jobtitle VALUES (null, (?), (?))";
		String[] akgCols = {"job_id"};
		
		int jobGk = -1;
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql, akgCols);
			ps.setString(1, title);
			ps.setString(2, "Y");

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}

			//Retrieve the auto generated key from the database
			rs = ps.getGeneratedKeys();
			
			if(rs.next()) {
				jobGk = rs.getInt(1);
			}
			
			//If no auto gen keys were returned then there's a problem
			if(jobGk == -1) {
				throw new SQLException ("Query failed to retrieve generated keys when expected");
			}
		
			System.out.println("Job Title Gen Key: " + jobGk);
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			//Create Job Title Model object for return
			jtm = new JobTitleModel(jobGk, title, true);
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			jtm = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return jtm;
	}
	
	/* Deletes a job title from the database
	 * @param the job title database id to remove
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	public boolean removeJobTitle(int jobid) {
		
		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String sql = "DELETE FROM jobtitle WHERE job_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, jobid);
			int rowsUpdated = ps.executeUpdate();

			System.out.println(ps);

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
		
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}

	
	
	/* Changes the Active status of the job title
	 * @param state true for activate, false for make inactive
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	public boolean setJobTitleActiveStatus(int jobid, boolean state) {
		
		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String status = null;
		
		if(state) {
			status = "Y";
		}
		else {
			status = "N";
		}
		
		String sql = "UPDATE jobtitle SET is_active = (?) WHERE job_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = getConnection().setSavepoint("Savepoint"); 
			ps = this.getConnection().prepareStatement(sql);
			ps.setString(1, status);
			ps.setInt(2, jobid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
		
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}


	/* Updates the job title from the database
	 * @param the job title database id to update and the title value to update the row with
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	public boolean updateJobTitle(int jobid, String title) {	
		
		//Ensure the title is not null value first, then proceed
		if(title == null) {
			return false;
		}
		
		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String sql = "UPDATE jobtitle SET job_title = (?) WHERE job_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setString(1, title);
			ps.setInt(2, jobid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
		
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}



	/* Retrieve all the job titles in the jobtitle table in the database
	 * @return the arraylist of all the job title rows in the database in model objects
	 */
	public ArrayList<JobTitleModel> getAllJobTitles(String activeStatus) {
		//Check activeStatus is a valid value
		if(activeStatus != "Y" && activeStatus != "N" && activeStatus != null) {
			return null;
		}
		
		ArrayList<JobTitleModel> jtmList = new ArrayList<JobTitleModel>();
		
		//Create SQL Query depending on activeStatus and execute it
		String sql = "SELECT * FROM jobtitle";
		
		if(activeStatus != null) {
			switch (activeStatus) {
			case "Y": 
				sql = sql + " WHERE is_active = 'Y'";
				break;
			case "N":
				sql = sql + " WHERE is_active = 'N'";
				break;
			}
		}
				
		System.out.println(sql);
		
		try {
			ps = this.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {

				int jobId = rs.getInt("job_id");
				String jobTitle = rs.getString("job_title");
				String isActive = rs.getString("is_active");

				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				jtmList.add(new JobTitleModel(jobId, jobTitle, isActiveBool));
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
			jtmList = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return jtmList;
	}



	/* Retrieves a particular job title from the jobtitle table in the database based on job id
	 * @see com.thedawson.util.dao.RosterDAO#getJobTitleById(int)
	 * @param jobid the job id you want to retrieve from the database.
	 * @return a job title database result in model form with the associated jobid
	 */
	public JobTitleModel getJobTitleById(int jobid) {
		JobTitleModel jtm = null;
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM jobtitle where job_id = (?)";
		
		try {
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, jobid);
			
			rs = ps.executeQuery();
			
			//If rs is not empty then a row exists with provided jobid, create job title model object
			//Otherwise return null job title model below
			if(rs.next()) {
				int jobId = rs.getInt("job_id");
				String jobTitle = rs.getString("job_title");
				String isActive = rs.getString("is_active");

				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				jtm = new JobTitleModel(jobId, jobTitle, isActiveBool);
			}
					
		} catch (SQLException se) {
			se.printStackTrace();
			jtm = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return jtm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addEmployee(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public EmployeeModel addEmployee(String firstN, String lastN, String email,
			String userid, String pwd, int hotelid, int jobid) {
				
				//Ensure all of the String parameters are not null first, then proceed
				if(firstN == null || lastN == null || email == null || userid == null || pwd == null) {
					return null;
				}
				
				EmployeeModel em = null;
				Savepoint sp = null;
				int empGk = -1;
				int empdirGk = -1;

				try {
					
					//Create a transaction
					this.getConnection().setAutoCommit(false);
					sp = this.getConnection().setSavepoint("Savepoint");
					
					//Execute query to insert into employee table
					String sql = "INSERT INTO employee VALUES (null, (?), (?), (?), (?), (?), (?))"; 
					String[] akgCols = {"e_id"};

					ps = this.getConnection().prepareStatement(sql, akgCols);
					ps.setString(1, firstN);
					ps.setString(2, lastN);
					ps.setString(3, email);
					ps.setString(4, userid);
					ps.setString(5, pwd);
					ps.setString(6, "Y");
					
					System.out.println(ps);
					
					int rowsUpdated = ps.executeUpdate();
					
					//Check to see if the query failed to updated the database
					if(rowsUpdated == 0) {
						throw new SQLException ("Query failed to update any rows");
					}

					//Retrieve the auto generated key from the database
					rs = ps.getGeneratedKeys();
					
					if(rs.next()) {
						empGk = rs.getInt(1);
					}

					System.out.println("Generated Key Employee: " + empGk);
					
					//If no auto gen keys were returned then there's a problem
					if(empGk == -1) {
						throw new SQLException ("Query failed to retrieve generated keys when expected");
					}

					//No error, so continue processing
					//Execute query to insert into employee directory table
					String sql2 = "INSERT INTO employeedir VALUES (null, (?), (?), (?), (?))";
					String[] akgCols2 = {"empldir_id"};
					
					ps = this.getConnection().prepareStatement(sql2, akgCols2);
					ps.setInt(1, hotelid);
					ps.setInt(2, empGk);
					ps.setInt(3, jobid);
					ps.setString(4, "Y");

					System.out.println(ps);

					int rowsUpdated2 = ps.executeUpdate();
					
					//Check to see if the query failed to updated the database
					if(rowsUpdated2 == 0) {
						throw new SQLException ("Query failed to update any rows");
					}

					//Retrieve the auto generated key from the database
					rs = ps.getGeneratedKeys();
					
					if(rs.next()) {
						empdirGk = rs.getInt(1);
					}
					
					//If no auto gen keys were returned then there's a problem
					if(empdirGk == -1) {
						throw new SQLException ("Query failed to retrieve generated keys when expected");
					}


					System.out.println("Generated Key Employee Dir: " + empdirGk);
					
					//Create the employee object model for return
					em = new EmployeeModel(empGk, firstN, lastN, email, userid, pwd, true);
					
					//End Transaction
					this.getConnection().commit();
					this.getConnection().setAutoCommit(true);
					
				} catch (SQLException se) {
					se.printStackTrace();
					try { this.getConnection().rollback(sp); } catch (SQLException se2) { se2.printStackTrace(); }
					em = null;
				}
				finally {
					this.closeConnection(ps, rs);
					rs = null;
					ps = null;
				}
				
				return em;
	}

	
	
	/* Deactivates an employee in the database, but keeps its historic record data intact
	 * @see com.thedawson.util.dao.RosterDAO#removeEmployee(int)
	 * @param the employee database id to remove
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	public boolean deactivateEmployee(int empid) {
		
		boolean returnStatus = true;
		
		//Create SQL Query and execute it
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			
			//Deactive all Employee Directory entries for this employee (i.e. he can no longer work)
			//Get all the directory entries for this employee
			String sql1 = "SELECT * FROM employeedir WHERE emp_id = " + empid;
			ps = this.getConnection().prepareStatement(sql1);
			rs = ps.executeQuery();
			
			//Set each entry to inactive
			while(rs.next()) {
				int curEmpDirID = rs.getInt("empldir_id");
				String sql2 = "UPDATE employeedir SET is_active = (?) WHERE empldir_id = (?)";
				ps = this.getConnection().prepareStatement(sql2);
				ps.setString(1, "N");
				ps.setInt(2, curEmpDirID);
				
				int rowsUpdated = ps.executeUpdate();
				
				if(rowsUpdated == 0) {
					throw new SQLException ("Query failed to update any rows on Employee Dir");
				}
			}
			
			//Deactive the employee in the employee table 		
			String sql3 = "UPDATE employee SET is_active = (?) WHERE e_id = (?)";
			ps = this.getConnection().prepareStatement(sql3);
			ps.setString(1, "N");
			ps.setInt(2, empid);

			int rowsUpdated1 = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated1 == 0) {
				throw new SQLException ("Query failed to update any rows on Employee");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch (SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}
	
	/* Deletes an employee from the database
	 * @see com.thedawson.util.dao.RosterDAO#removeEmployee(int)
	 * @param the employee database id to remove
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	public boolean removeEmployee(int empid) {
		
		boolean returnStatus = true;
		
		//Create SQL Query and execute it
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			
			//Delete from Employee Directory first since there are constraints, cannot delete Employee first
			String sql = "DELETE FROM employeedir WHERE emp_id = (?)";
			
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, empid);
			
			int rowsUpdated = ps.executeUpdate();
			
			//Check to see if the query failed to updated the database
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			System.out.println(ps);
					
			String sql2 = "DELETE FROM employee WHERE e_id = " + empid;
			
			ps = this.getConnection().prepareStatement(sql2);
			ps.setInt(1, empid);
			
			int rowsUpdated2 = ps.executeUpdate();
			
			//Check to see if the query failed to updated the database
			if(rowsUpdated2 == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			System.out.println(ps);
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch (SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}
	
	/* Changes the Active status of the employee
	 * @param state true for activate, false for make inactive
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	public boolean setEmployeeActiveStatus(int empid, boolean state) {
		
		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String status = null;
		
		if(state) {
			status = "Y";
		}
		else {
			status = "N";
		}
		
		String sql = "UPDATE employee SET is_active = (?) WHERE e_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = getConnection().setSavepoint("Savepoint"); 
			ps = this.getConnection().prepareStatement(sql);
			ps.setString(1, status);
			ps.setInt(2, empid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
		
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}


	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateEmployee(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int, int)
	 */
	public boolean updateEmployee(int empid, String firstN, String lastN,
			String email, String userid, String pwd) {
		
		//Ensure all String parameters are not null first, then proceed
		if(firstN == null || lastN == null || email == null || userid == null || pwd == null) {
			return false;
		}
		
		boolean returnStatus = false;

		//Create SQL Query and execute it
		String sql = "UPDATE employee SET first_name = (?), "
				+ "last_name = (?), "
				+ "email = (?), "
				+ "username = (?), "
				+ "encr_pwd = (?) "
				+ "WHERE e_id = (?)";

		Savepoint sp = null;

		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setString(1, firstN);
			ps.setString(2, lastN);
			ps.setString(3, email);
			ps.setString(4, userid);
			ps.setString(5, pwd);
			ps.setInt(6, empid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}

			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);

			returnStatus = true;

		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}

		return returnStatus;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllEmployees()
	 */
	public ArrayList<EmployeeModel> getAllEmployees(String activeStatus) {
		//Check activeStatus is a valid value
		if(activeStatus != "Y" && activeStatus != "N" && activeStatus != null) {
			return null;
		}
		
		ArrayList<EmployeeModel> emList = new ArrayList<EmployeeModel>();
		
		//Create SQL Query depending on activeStatus and execute it
		String sql = "SELECT * FROM employee";
		
		if(activeStatus != null) {
			switch (activeStatus) {
			case "Y": 
				sql = sql + " WHERE is_active = 'Y'";
				break;
			case "N":
				sql = sql + " WHERE is_active = 'N'";
				break;
			}
		}

		System.out.println(sql);

		try {
			ps = this.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();

			while (rs.next()) {

				int empId = rs.getInt("e_id");
				String firstN = rs.getString("first_name");
				String lastN = rs.getString("last_name");
				String email = rs.getString("email");
				String userid = rs.getString("username");
				String pwd = rs.getString("encr_pwd");
				String isActive = rs.getString("is_active");

				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				emList.add(new EmployeeModel(empId, firstN, lastN, email, userid, pwd, isActiveBool));
			}

		} catch (SQLException se) {
			se.printStackTrace();
			emList = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return emList;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getEmployeeById(int)
	 */
	public EmployeeModel getEmployeeById(int empid) {
		EmployeeModel em = null;
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM employee where e_id = (?)";
		
		System.out.println(sql);
		
		try {
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, empid);
			
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			//If rs is not empty then a row exists with provided empid, create employee model object
			//Otherwise return null employee model below
			if(rs.next()) {
				int empId = rs.getInt("e_id");
				String firstN = rs.getString("first_name");
				String lastN = rs.getString("last_name");
				String email = rs.getString("email");
				String userid = rs.getString("username");
				String pwd = rs.getString("encr_pwd");
				String isActive = rs.getString("is_active");

				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				em = new EmployeeModel(empId, firstN, lastN, email, userid, pwd, isActiveBool);
			}
					
		} catch (SQLException se) {
			se.printStackTrace();
			em = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return em;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addEmployeeDir(int, int, int)
	 */
	public EmployeeDirModel addEmployeeDir(int hotelid, int empid, int jobid) {
		
		EmployeeDirModel edm = null;
		
		int edGk = -1;
		Savepoint sp = null;
		
		try {
			//Check if the hotelid, empid, and jobid are active
			//At the same time check if the ids valid to a save code exec below
			String checkSql_hid = "SELECT * FROM hotel WHERE h_id = " + hotelid;
			String checkSql_eid = "SELECT * FROM employee WHERE e_id = " + empid;
			String checkSql_jid = "SELECT * FROM jobtitle WHERE job_id = " + jobid;
			
			for (int i=0; i < 3; i++) {
				
				String curQuery = null;
				switch (i) {
				case 0:
					curQuery = checkSql_hid;
					break;
				case 1:
					curQuery = checkSql_eid;
					break;
				default:
					curQuery = checkSql_jid;
					break;
				}
				
				System.out.println("Current query: " + curQuery);
				
				ps = this.getConnection().prepareStatement(curQuery);
				rs = ps.executeQuery();
				
				//Get the first entry and ONLY entry in the resultset
				if(rs.next()) {
					//Note that is_active is consistent column name in the database over all 3 tables
					String isActiveInd = rs.getString("is_active");
					
					//Check if the is_active indicator is "Y" or "N"
					//if no then we know that you cannot add this info to the directory, it's using an inactive id, error out
					//Otherwise we can continue adding the directory entry
					if(isActiveInd.equals("N")) {
						throw new SQLException("Error: Attempting to add Employee Directory Entry using Inactive value");
					}
				}
				else {
					//No entries returned from query when expected or b/c id was invalid, return without adding emp dir entry
					throw new SQLException("Error: No result returned from query for checking is_active, add emp dir aborted");
				}
			}
			
			//Create SQL Query and execute it
			String sql = "INSERT INTO employeedir VALUES (null, (?), (?), (?), (?))";
			String[] akgCols = {"empldir_id"};
			
			System.out.println(sql);

			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql, akgCols);
			ps.setInt(1, hotelid);
			ps.setInt(2, empid);
			ps.setInt(3, jobid);
			ps.setString(4, "Y");

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}

			//Retrieve the auto generated key from the database
			rs = ps.getGeneratedKeys();

			if(rs.next()) {
				edGk = rs.getInt(1);
			}

			//If no auto gen keys were returned then there's a problem
			if(edGk == -1) {
				throw new SQLException ("Query failed to retrieve generated keys when expected");
			}

			System.out.println("Job Title Gen Key: " + edGk);

			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);

			//Create Emp Dir Model object for return
			edm = new EmployeeDirModel(edGk, hotelid, empid, jobid, true);

		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			edm = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}

		return edm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#removeEmployeeDir(int)
	 */
	public boolean removeEmployeeDir(int empdirid) {
		
		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String sql = "DELETE FROM employeedir WHERE empldir_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, empdirid);
			int rowsUpdated = ps.executeUpdate();

			System.out.println(ps);

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
		
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#setEmployeeDirActiveStatus(int, boolean)
	 */
	public boolean setEmployeeDirActiveStatus(int empdirid, boolean state) {
		
		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String status = null;
		
		if(state) {
			status = "Y";
		}
		else {
			status = "N";
		}
			
		
		String sql = "UPDATE employeedir SET is_active = (?) WHERE empldir_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = getConnection().setSavepoint("Savepoint"); 
			ps = this.getConnection().prepareStatement(sql);
			ps.setString(1, status);
			ps.setInt(2, empdirid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
		
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;	
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateEmployeeDir(int, int, int, int)
	 */
	public boolean updateEmployeeDir(int empdirid, int hotelid, int empid,
			int jobid) {

		boolean returnStatus = false;
		Savepoint sp = null;
		
		try {
			//Check if the hotelid, empid, and jobid are active
			//At the same time check if the ids valid to a save code exec below
			String checkSql_hid = "SELECT * FROM hotel WHERE h_id = " + hotelid;
			String checkSql_eid = "SELECT * FROM employee WHERE e_id = " + empid;
			String checkSql_jid = "SELECT * FROM jobtitle WHERE job_id = " + jobid;
			
			for (int i=0; i < 3; i++) {
				
				String curQuery = null;
				switch (i) {
				case 0:
					curQuery = checkSql_hid;
					break;
				case 1:
					curQuery = checkSql_eid;
					break;
				default:
					curQuery = checkSql_jid;
					break;
				}
				
				System.out.println("Update: Current query: " + curQuery);
				
				ps = this.getConnection().prepareStatement(curQuery);
				rs = ps.executeQuery();
				
				//Get the first entry and ONLY entry in the resultset
				if(rs.next()) {
					
					//Note that is_active is consistent column name in the database over all 3 tables
					String isActiveInd = rs.getString("is_active");
					
					//Check if the is_active indicator is "Y" or "N"
					//if no then we know that you cannot add this info to the directory, it's using an inactive id, error out
					//Otherwise we can continue adding the directory entry
					if(isActiveInd.equals("N")) {
						throw new SQLException("Error: Attempting to update Employee Directory Entry using Inactive value");
					}
				}
				else {
					//No entries returned from query when expected or b/c id was invalid, return without updating emp dir entry
					throw new SQLException("Error: No result returned from query for checking is_active, update emp dir aborted");
				}
			}
			
			//Create SQL Query and execute it
			String sql = "UPDATE employeedir SET hotel_id = (?), "
					+ "emp_id = (?) "
					+ "WHERE empldir_id = (?)";
			
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, hotelid);
			ps.setInt(2, empid);
			ps.setInt(3, empdirid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
		
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllEmployeeDirs()
	 */
	public ArrayList<EmployeeDirModel> getAllEmployeeDirs(String activeStatus) {
		//Check activeStatus is a valid value
		if(activeStatus != "Y" && activeStatus != "N" && activeStatus != null) {
			return null;
		}
		
		ArrayList<EmployeeDirModel> edList = new ArrayList<EmployeeDirModel>();
		
		//Create SQL Query depending on activeStatus and execute it
		String sql = "SELECT * FROM employeedir";
		
		if(activeStatus != null) {
			switch (activeStatus) {
			case "Y": 
				sql = sql + " WHERE is_active = 'Y'";
				break;
			case "N":
				sql = sql + " WHERE is_active = 'N'";
				break;
			}
		}
		
		System.out.println(sql);
		
		try {
			ps = this.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {

				int empdirId = rs.getInt("empldir_id");
				int hotelId = rs.getInt("hotel_id");
				int empId = rs.getInt("emp_id");
				int jobId = rs.getInt("job_id");
				String isActive = rs.getString("is_active");

				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				edList.add(new EmployeeDirModel(empdirId, hotelId, empId, jobId, isActiveBool));
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
			edList = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return edList;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getEmployeeDirById(int)
	 */
	public EmployeeDirModel getEmployeeDirById(int empdirid) {
		EmployeeDirModel edm = null;
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM employeedir where empldir_id = (?)";
		
		try {
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, empdirid);
			
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			//If rs is not empty then a row exists with provided empdirid, create emp dir model object
			//Otherwise return null emp dir model below
			if(rs.next()) {
				int empdirId = rs.getInt("empldir_id");
				int hotelId = rs.getInt("hotel_id");
				int empId = rs.getInt("emp_id");
				int jobId = rs.getInt("job_id");
				String isActive = rs.getString("is_active");

				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				edm = new EmployeeDirModel(empdirId, hotelId, empId, jobId, isActiveBool);
			}
					
		} catch (SQLException se) {
			se.printStackTrace();
			edm = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return edm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addHotel(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public HotelModel addHotel(String hname, String haddr, String hcity,
			String hprov, String hpcd, String hcntry, String hphone, String hfax) {
		
		//Ensure none of the String parameters (except postal code and fax) are null first, then proceed
		if(hname == null || haddr == null || hcity == null || hprov == null || hcntry == null || hphone == null) {
			return null;
		}

		HotelModel hm = null;

		//Create SQL Query and execute it
		String sql = "INSERT INTO hotel VALUES (null, (?), (?), (?), (?), (?), (?), (?), (?), (?))";
		String[] akgCols = {"h_id"};

		int hGk = -1;
		Savepoint sp = null;

		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql, akgCols);
			ps.setString(1, hname);
			ps.setString(2, haddr);
			ps.setString(3, hcity);
			ps.setString(4, hprov);
			ps.setString(5, hpcd);
			ps.setString(6, hcntry);
			ps.setString(7, hphone);
			ps.setString(8, hfax);
			ps.setString(9, "Y");

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}

			//Retrieve the auto generated key from the database
			rs = ps.getGeneratedKeys();

			if(rs.next()) {
				hGk = rs.getInt(1);
			}

			//If no auto gen keys were returned then there's a problem
			if(hGk == -1) {
				throw new SQLException ("Query failed to retrieve generated keys when expected");
			}

			System.out.println("Job Title Gen Key: " + hGk);

			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);

			//Create Hotel Model object for return
			hm = new HotelModel(hGk, hname, haddr, hcity, hprov, hpcd, hcntry, hphone, hfax, true);

		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			hm = null;
		}
		finally {			
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}

		return hm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#removeHotel(int)
	 */
	public boolean removeHotel(int hotelid) {
		
		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String sql = "DELETE FROM hotel WHERE h_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, hotelid);
			int rowsUpdated = ps.executeUpdate();

			System.out.println(ps);

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
		
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}


	/* Changes the Active status of the job title
	 * @param state true for activate, false for make inactive
	 * @return boolean determine if the update ran successfully or was rolled back and failed
	 */
	public boolean setHotelActiveStatus(int hotelid, boolean state) {
		
		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String status = null;
		
		if(state) {
			status = "Y";
		}
		else {
			status = "N";
		}
			
		
		String sql = "UPDATE hotel SET is_active = (?) WHERE h_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = getConnection().setSavepoint("Savepoint"); 
			ps = this.getConnection().prepareStatement(sql);
			ps.setString(1, status);
			ps.setInt(2, hotelid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
		
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}
	

	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateHotel(int, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	public boolean updateHotel(int hotelid, String hname, String haddr,
			String hcity, String hprov, String hpcd, String hcntry, String hphone, String hfax) {
		
		//Ensure the String parameters (except postal code and fax) are not null first, then proceed
		if(hname == null || haddr == null || hcity == null || hprov == null || hcntry == null || hphone == null) {
			return false;
		}

		boolean returnStatus = false;

		//Create SQL Query and execute it
		String sql = "UPDATE hotel SET h_name = (?), "
				+ "h_addr = (?), "
				+ "h_city = (?), "
				+ "h_prov = (?), "
				+ "h_postcd = (?), "
				+ "h_country = (?), "
				+ "h_tel = (?), "
				+ "h_fax = (?) "
				+ "WHERE h_id = (?)";
		

		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setString(1, hname);
			ps.setString(2, haddr);
			ps.setString(3, hcity);
			ps.setString(4, hprov);
			ps.setString(5, hpcd);
			ps.setString(6, hcntry);
			ps.setString(7, hphone);
			ps.setString(8, hfax);
			ps.setInt(9, hotelid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
		
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllHotels()
	 */
	public ArrayList<HotelModel> getAllHotels(String activeStatus) {
		//Check activeStatus is a valid value
		if(activeStatus != "Y" && activeStatus != "N" && activeStatus != null) {
			return null;
		}
		
		ArrayList<HotelModel> hList = new ArrayList<HotelModel>();
		
		//Create SQL Query depending on activeStatus and execute it
		String sql = "SELECT * FROM hotel";
		
		if(activeStatus != null) {
			switch (activeStatus) {
			case "Y": 
				sql = sql + " WHERE is_active = 'Y'";
				break;
			case "N":
				sql = sql + " WHERE is_active = 'N'";
				break;
			}
		}
		
		System.out.println(sql);
		
		try {
			ps = this.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {

				int hotelId = rs.getInt("h_id");
				String hName = rs.getString("h_name");
				String hAddr = rs.getString("h_addr");
				String hCity = rs.getString("h_city");
				String hProv = rs.getString("h_prov");
				String hPcd = rs.getString("h_postcd");
				String hCntry = rs.getString("h_country");
				String hTel = rs.getString("h_tel");
				String hFax = rs.getString("h_fax");
				String isActive = rs.getString("is_active");

				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				hList.add(new HotelModel(hotelId, hName, hAddr, hCity, hProv, hPcd, hCntry, hTel, hFax, isActiveBool));
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
			hList = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return hList;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getHotelById(int)
	 */
	public HotelModel getHotelById(int hotelid) {
		
		HotelModel hm = null;
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM hotel where h_id = (?)";
		
		try {
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, hotelid);
			
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			//If rs is not empty then a row exists with provided hotelid, create hotel model object
			//Otherwise return null hotel model below
			if(rs.next()) {
				int hotelId = rs.getInt("h_id");
				String hName = rs.getString("h_name");
				String hAddr = rs.getString("h_addr");
				String hCity = rs.getString("h_city");
				String hProv = rs.getString("h_prov");
				String hPcd = rs.getString("h_postcd");
				String hCntry = rs.getString("h_country");
				String hTel = rs.getString("h_tel");
				String hFax = rs.getString("h_fax");
				String isActive = rs.getString("is_active");

				boolean isActiveBool = true;
				if(isActive.toUpperCase().equals("N")) {
					isActiveBool = false;
				}

				hm = new HotelModel(hotelId, hName, hAddr, hCity, hProv, hPcd, hCntry, hTel, hFax, isActiveBool);
			}
					
		} catch (SQLException se) {
			se.printStackTrace();
			hm = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return hm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addNextWorkSchedule(int, java.util.Date, java.util.Date)
	 */
	public WorkScheduleModel addNextWorkSchedule(int hotelid, Date startD,
			Date endD) {
		
		//Ensure the Dates are not null value first, then proceed
		if(startD == null || endD == null) {
			return null;
		}

		WorkScheduleModel wsm = null;
		
		//Create SQL Query and execute it
		String sql = "INSERT INTO workschedule VALUES (null, (?), (?), (?))";
		String[] akgCols = {"wksched_id"};

		int wsGk = -1;
		Savepoint sp = null;

		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql, akgCols);
			ps.setInt(1, hotelid);
			ps.setDate(2, startD);
			ps.setDate(3, endD);
			
			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}

			//Retrieve the auto generated key from the database
			rs = ps.getGeneratedKeys();

			if(rs.next()) {
				wsGk = rs.getInt(1);
			}

			//If no auto gen keys were returned then there's a problem
			if(wsGk == -1) {
				throw new SQLException ("Query failed to retrieve generated keys when expected");
			}

			System.out.println("Job Title Gen Key: " + wsGk);

			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);

			//Create Hotel Model object for return
			wsm = new WorkScheduleModel(wsGk, hotelid, startD, endD);

		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			wsm = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}

		return wsm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#deleteAWorkSchedule(int)
	 */
	public boolean deleteAWorkSchedule(int wsid) {
		
		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String sql = "DELETE FROM workschedule WHERE wksched_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, wsid);
			int rowsUpdated = ps.executeUpdate();

			System.out.println(ps);

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
		
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateCurWorkSchedule(int, int, java.util.Date, java.util.Date)
	 */
	public boolean updateCurWorkSchedule(int wsid, int hotelid, Date startD,
			Date endD) {
		
		//Ensure the title is not null value first, then proceed
		if(startD == null || endD == null) {
			return false;
		}
		
		boolean returnStatus = false;
		
		String sql = "UPDATE workschedule SET hotel_id = (?), "
				+ "start_date = (?), " 
				+ "end_date = (?) " 
				+ "WHERE wksched_id = (?)"; 

		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, hotelid);
			ps.setDate(2, startD);
			ps.setDate(3, endD);
			ps.setInt(4, wsid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
			
			returnStatus = true;
		
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getAllSchedules()
	 */
	public ArrayList<WorkScheduleModel> getAllSchedules() {
		ArrayList<WorkScheduleModel> wsList = new ArrayList<WorkScheduleModel>();
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM workschedule";
		
		System.out.println(sql);
		
		try {
			ps = this.getConnection().prepareStatement(sql);
			rs = ps.executeQuery();
			
			while (rs.next()) {

				int wkschedId = rs.getInt("wksched_id");
				int hotelId = rs.getInt("hotel_id");
				Date startD = rs.getDate("start_date");
				Date endD = rs.getDate("end_date");

				wsList.add(new WorkScheduleModel(wkschedId, hotelId, startD, endD));
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
			wsList = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return wsList;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getSchedulesByDate(java.util.Date, java.util.Date)
	 */
	public ArrayList<WorkScheduleModel> getSchedulesByDate(Date startD,
			Date endD) {
		
		//Check if the start date is after the end date
		//if so then invalid and return null
		if(startD.compareTo(endD) > 0) {
			return null;
		}
		
		//The dates are correct and in order so run query
		ArrayList<WorkScheduleModel> wsList = new ArrayList<WorkScheduleModel>();
		
		//Create SQL Query and execute it
		String sql = "SELECT * FROM workschedule WHERE (start_date >= (?) AND end_date <= (?)) " 
						+ "OR (start_date < (?) AND end_date > (?)) "
						+ "OR (start date < (?) AND end_date > (?)))";
		
		try {
			ps = this.getConnection().prepareStatement(sql);
			ps.setDate(1, startD);
			ps.setDate(2, endD);
			ps.setDate(3, startD);
			ps.setDate(4, startD);
			ps.setDate(5, endD);
			ps.setDate(6, endD);
			rs = ps.executeQuery();
			
			System.out.println(ps);
			
			while (rs.next()) {

				int wkschedId = rs.getInt("wksched_id");
				int hotelId = rs.getInt("hotel_id");
				Date startDate = rs.getDate("start_date");
				Date endDate = rs.getDate("end_date");

				wsList.add(new WorkScheduleModel(wkschedId, hotelId, startDate, endDate));
			}
			
		} catch (SQLException se) {
			se.printStackTrace();
			wsList = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}
		
		return wsList;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getScheduleByID(int)
	 */
	public WorkScheduleModel getScheduleByID(int wsid) {
		
		WorkScheduleModel wsm = null;

		//Create SQL Query and execute it
		String sql = "SELECT * FROM workschedule WHERE wksched_id = (?)";

		try {
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, wsid);
			
			System.out.println(ps);
			
			rs = ps.executeQuery();
			
			//If rs is not empty then a row exists with provided wsid, create work schedule model object
			//Otherwise return null work schedule model below
			if(rs.next()) {
				int wkschedId = rs.getInt("wksched_id");
				int hotelId = rs.getInt("hotel_id");
				Date startD = rs.getDate("start_date");
				Date endD = rs.getDate("end_date");

				wsm = new WorkScheduleModel(wkschedId, hotelId, startD, endD);
			}
					
		} catch (SQLException se) {
			se.printStackTrace();
			wsm = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}

		return wsm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#addWorkDay(int, int, int, java.util.Date, double)
	 */
	public WorkDayModel addWorkDay(int wkschedid, int empid, int jobid,
			Date shiftDate, double shiftLen) {

		//Ensure the Date is not null first, then proceed
		if(shiftDate == null) {
			return null;
		}

		WorkDayModel wdm = null;

		//Create SQL Query and execute it
		String sql = "INSERT INTO workday VALUES (null, (?), (?), (?), (?), (?))";
		String[] akgCols = {"wksched_id"};

		int wdGk = -1;
		Savepoint sp = null;

		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql, akgCols);
			ps.setInt(1, wkschedid);
			ps.setInt(2, empid);
			ps.setInt(3, jobid);
			ps.setDate(4, shiftDate);
			ps.setDouble(5, shiftLen);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}

			//Retrieve the auto generated key from the database
			rs = ps.getGeneratedKeys();

			if(rs.next()) {
				wdGk = rs.getInt(1);
			}

			//If no auto gen keys were returned then there's a problem
			if(wdGk == -1) {
				throw new SQLException ("Query failed to retrieve generated keys when expected");
			}

			System.out.println("Job Title Gen Key: " + wdGk);

			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);

			//Create Work Day Model object for return
			wdm = new WorkDayModel(wdGk, wkschedid, empid, jobid, shiftDate, shiftLen);

		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			wdm = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}

		return wdm;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#deleteAWorkDay(int)
	 */
	public boolean deleteAWorkDay(int wdid) {

		boolean returnStatus = false;
		
		//Create SQL Query and execute it
		String sql = "DELETE FROM workday WHERE wksched_id = (?)";
		
		Savepoint sp = null;
		
		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, wdid);
			int rowsUpdated = ps.executeUpdate();

			System.out.println(ps);

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}
			
			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);
		
			returnStatus = true;
			
		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}
		
		return returnStatus;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#updateAWorkDay(int, int, int, int, java.util.Date, double)
	 */
	public boolean updateAWorkDay(int wdid, int wkschedid, int empid,
			int jobid, Date shiftDate, double shiftLen) {
		
		//Ensure the shiftDate is not null value first, then proceed
		if(shiftDate == null) {
			return false;
		}

		boolean returnStatus = false;

		String sql = "UPDATE workday SET wksched_id = (?), "
				+ "emp_id = (?), " 
				+ "job_id = (?), " 
				+ "shift_date = (?), "
				+ "shift_len = (?) "
				+ "WHERE wkday_id = (?)"; 

		Savepoint sp = null;

		try {
			this.getConnection().setAutoCommit(false);
			sp = this.getConnection().setSavepoint("Savepoint");
			ps = this.getConnection().prepareStatement(sql);
			ps.setInt(1, wkschedid);
			ps.setInt(2, empid);
			ps.setInt(3, jobid);
			ps.setDate(4, shiftDate);
			ps.setDouble(5, shiftLen);
			ps.setInt(6, wdid);

			System.out.println(ps);

			int rowsUpdated = ps.executeUpdate();

			//Check to see if the query failed to updated the database 
			if(rowsUpdated == 0) {
				throw new SQLException ("Query failed to update any rows");
			}

			this.getConnection().commit();
			this.getConnection().setAutoCommit(true);

			returnStatus = true;

		} catch (SQLException se) {
			se.printStackTrace();
			try { this.getConnection().rollback(sp); } catch(SQLException se2) { se2.printStackTrace(); }
			returnStatus = false;
		}
		finally {
			this.closeConnection(ps, rs);
			ps = null;
		}

		return returnStatus;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getWorkDaysByDate(java.util.Date, java.util.Date)
	 */
	public ArrayList<WorkDayModel> getWorkDaysByDate(Date startD, Date endD) {
		
		//Check if the start date is after the end date
		//if so then invalid and return null
		if(startD.compareTo(endD) > 0) {
			return null;
		}

		//The dates are correct and in order so run query
		ArrayList<WorkDayModel> wdList = new ArrayList<WorkDayModel>();

		//Create SQL Query and execute it
		String sql = "SELECT * FROM workday WHERE shift_date >= (?) AND shift_date <= (?)";

		try {
			ps = this.getConnection().prepareStatement(sql);
			ps.setDate(1, startD);
			ps.setDate(2, endD);
			
			rs = ps.executeQuery();

			System.out.println(ps);

			while (rs.next()) {
				int wdId = rs.getInt("wkday_id");
				int wkschedId = rs.getInt("wksched_id");
				int empId = rs.getInt("emp_id");
				int jobId = rs.getInt("job_id");
				Date shiftDate = rs.getDate("shift_date");
				double shiftLen = rs.getDouble("shift_len");
				
				wdList.add(new WorkDayModel(wdId, wkschedId, empId, jobId, shiftDate, shiftLen));
			}

		} catch (SQLException se) {
			se.printStackTrace();
			wdList = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}

		return wdList;
	}



	/* (non-Javadoc)
	 * @see com.thedawson.util.dao.RosterDAO#getWorkDaysBySchedIDs(java.util.ArrayList)
	 */
	public ArrayList<WorkDayModel> getWorkDaysBySchedIDs(ArrayList<Integer> schedids) {
		
		ArrayList<WorkDayModel> wdmList = new ArrayList<WorkDayModel>();

		//Create SQL Query and execute it
		String sql = "SELECT * FROM workday WHERE wksched_id = (?)";

		try {
			ps = this.getConnection().prepareStatement(sql);
			
			//Iterate through all the schedule ids in the arraylist and retrieve all the workday results associated
			Iterator<Integer> schedIter = schedids.iterator();
			while(schedIter.hasNext()) {
				Integer curSchedId = schedIter.next();
				
				ps.setInt(1, curSchedId.intValue());

				System.out.println(ps);

				rs = ps.executeQuery();

				//If rs is not empty then at last one row exists with provided schedid, create workday model object and add to list
				//Otherwise return null work day model list below
				while(rs.next()) {
					int wdId = rs.getInt("wkday_id");
					int wkschedId = rs.getInt("wksched_id");
					int empId = rs.getInt("emp_id");
					int jobId = rs.getInt("job_id");
					Date shiftDate = rs.getDate("shift_date");
					double shiftLen = rs.getDouble("shift_len");

					wdmList.add(new WorkDayModel(wdId, wkschedId, empId, jobId, shiftDate, shiftLen));
				}
			}

		} catch (SQLException se) {
			se.printStackTrace();
			wdmList = null;
		}
		finally {
			this.closeConnection(ps, rs);
			rs = null;
			ps = null;
		}

		return wdmList;
	}
	
	/**
	 * Method to test connection to the database.  Not used in actual application.
	 */
	/*
	
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
}
