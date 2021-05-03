package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.TeacherBean;
import com.uog.miller.s1707031_ct6039.mail.Emailer;
import java.sql.*;
import java.util.ArrayList;

public class TeacherConnections extends AbstractOracleConnections
{
	//Used for DB connections to Teacher Table and Teacher related requests
	public TeacherConnections()
	{
		//Empty Constructor
	}

	public boolean registerTeacher(TeacherBean beanToRegister)
	{
		boolean ret = false;
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Add Teacher to DB
				String values = "'" +beanToRegister.getFirstname().replace("'", "''")
						+ "','" + beanToRegister.getTitle().replace("'", "''")
						+ "','" + beanToRegister.getSurname().replace("'", "''")
						+ "','" + beanToRegister.getEmail().replace("'", "''")
						+ "','" + beanToRegister.getDOB()
						+ "','" + beanToRegister.getAddress().replace("'", "''")
						+ "','" + beanToRegister.getYear()
						+ "','" + beanToRegister.getPword().replace("'", "''")
						+ "','" + beanToRegister.getEmailForHomework()
						+ "','" + beanToRegister.getEmailForCalendar()
						+ "','" + beanToRegister.getEmailForProfile() + "'";
				String query = "INSERT INTO " + TEACHERS_COLLECTION +
						"(Firstname, Title, Surname, Email, DOB, Address, Year, Pword, Homework_Email, Calender_Email, Profile_Email)" + " VALUES (" + values + ")";
				//Execute query
				executeRegisterQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to register User to Oracle", e);
		}

		//Check account is created
		boolean userExists = checkUserExists(beanToRegister.getEmail());
		if(userExists)
		{
			//Email (Based on user account preferences)
			boolean shouldEmail = shouldUserBeNotified(beanToRegister.getEmail(), NotificationType.PROFILE);
			if(shouldEmail)
			{
				//Process Mail to inform user of Update
				Emailer emailer = new Emailer();
				emailer.generateMailForProfileCreate(beanToRegister.getEmail(), beanToRegister.getFirstname());
			}
			ret = true;
		}
		return ret;
	}

	private void executeRegisterQuery(Connection oracleClient, String query) throws SQLException
	{
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			preparedStatement.executeUpdate(query);
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();
	}

	public TeacherBean login(String email, String pword)
	{
		TeacherBean ret = null;
		boolean userExists = checkUserExists(email);
		if(userExists)
		{
			ret = validateCredentials(email, pword);
		}
		return ret;
	}

	public TeacherBean validateCredentials(String email, String pword)
	{
		//Default to true in case of an error.
		TeacherBean ret = null;
		email = email.toLowerCase();

		//Connect to DB, check if email is present
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select Query
				String query = "SELECT * FROM " + TEACHERS_COLLECTION + " WHERE Email='" + email.replace("'", "''") +"' AND Pword='" + pword.replace("'", "''") + "'";
				//Execute query
				ArrayList<TeacherBean> allTeachers = executeQuery(oracleClient, query);
				if(allTeachers.size() == 1)
				{
					ret = allTeachers.get(0);
				}
				else
				{
					LOG.error("Multiple matching users found, this shouldn't happen.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve user from Oracle", e);
		}

		return ret;
	}

	public boolean checkUserExists(String email)
	{
		//Default to true in case of an error.
		boolean ret = true;
		email = email.toLowerCase();
		//Connect to DB, check if email is present
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select Query
				String query = "SELECT * FROM " + TEACHERS_COLLECTION + " WHERE Email='" + email.replace("'", "''") +"'";
				//Execute query
				ArrayList<TeacherBean> allTeachers = executeQuery(oracleClient, query);
				if(allTeachers.isEmpty())
				{
					ret = false;
				}
				else
				{
					LOG.debug("User Exists, cannot create new user.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to see if Teacher exists", e);
		}
		return ret;
	}

	private ArrayList<TeacherBean> executeQuery(Connection oracleClient, String query) throws SQLException
	{
		//Executes SQL Query, any Teachers found will populate the ArrayList.
		ArrayList<TeacherBean> allTeachers = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				TeacherBean bean = new TeacherBean(resultSet);
				//Add bean to list of students
				allTeachers.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		return allTeachers;
	}

	public void deleteAccount(String email)
	{
		LOG.debug("Attempting to delete user:" + email);
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "DELETE FROM " + TEACHERS_COLLECTION
						+ " WHERE Email='" + email + "'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to delete user in oracle DB");
		}
	}

	public void updateAccount(TeacherBean teacherBean)
	{
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "UPDATE " + TEACHERS_COLLECTION + " SET Firstname ='" + teacherBean.getFirstname().replace("'", "''")
						+"', Surname='"+ teacherBean.getSurname().replace("'", "''")
						+"', Title='"+ teacherBean.getTitle().replace("'", "''")
						+"', DOB='"+ teacherBean.getDOB().replace("'", "''")
						+"', Address='"+ teacherBean.getAddress().replace("'", "''")
						+"', Year='"+ teacherBean.getYear()
						+"', Pword='"+ teacherBean.getPword().replace("'", "''")
						+"', Homework_Email='"+ teacherBean.getEmailForHomework()
						+"', Calender_Email='"+ teacherBean.getEmailForCalendar()
						+"', Profile_Email='"+ teacherBean.getEmailForProfile()
						+"' WHERE Email='"+ teacherBean.getEmail() +"'";
				executeUpdateQuery(oracleClient, query);

				//Email (Based on user account preferences)
				boolean shouldEmail = shouldUserBeNotified(teacherBean.getEmail(), NotificationType.PROFILE);
				if(shouldEmail)
				{
					//Process Mail to inform user of Update
					Emailer emailer = new Emailer();
					emailer.generateMailForProfileUpdate(teacherBean.getEmail(), teacherBean.getFirstname());
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to update teacher information", e);
		}
	}

	private void executeUpdateQuery(Connection oracleClient, String query) throws SQLException
	{
		try (Statement statement = oracleClient.createStatement())
		{
			statement.executeUpdate(query);
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();
	}
}
