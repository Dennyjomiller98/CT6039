package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.ChildBean;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class ChildConnections extends AbstractOracleConnections
{
	//Used to execute DB actions for Child Users
	public ChildConnections()
	{
		//Empty Constructor
	}

	public boolean registerChild(ChildBean beanToRegister)
	{
		boolean ret = false;
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Add Child to DB
				String values = "'" +beanToRegister.getFirstname()
						+ "','" + beanToRegister.getSurname()
						+ "','" + beanToRegister.getEmail()
						+ "','" + beanToRegister.getDOB()
						+ "','" + beanToRegister.getAddress()
						+ "','" + beanToRegister.getYear()
						+ "','" + beanToRegister.getPword()
						+ "','" + beanToRegister.getEmailForHomework()
						+ "','" + beanToRegister.getEmailForCalender()
						+ "','" + beanToRegister.getEmailForProfile() + "'";
				String query = "INSERT INTO " + CHILDRENS_COLLECTION +
						"(Firstname, Surname, Email, DOB, Address, Year, Pword, Homework_Email, Calender_Email, Profile_Email)" + " VALUES (" + values + ")";
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
			ret = true;
		}
		return ret;
	}

	private void executeRegisterQuery(Connection oracleClient, String query) throws SQLException
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

	public void login()
	{
		//Needs Auth
	}

	public void logout()
	{
		//Needs Auth
	}

	public void deleteAccount()
	{
		//Needs Auth
	}

	public boolean checkUserExists(String email)
	{
		//Default to true in case of an error.
		boolean ret = true;

		//Connect to DB, check if email is present
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select Query
				String query = "SELECT * FROM " + CHILDRENS_COLLECTION + "WHERE EMAIL='" + email +"'";
				//Execute query
				ArrayList<ChildBean> allChildren = executeQuery(oracleClient, query);
				if(allChildren.isEmpty())
				{
					ret = false;
				}
				else
				{
					LOG.error("User Exists, cannot create new user.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve all students from Oracle", e);
		}

		return ret;
	}

	private ArrayList<ChildBean> executeQuery(Connection oracleClient, String query) throws SQLException
	{
		//Executes SQL Query, any Children found will populate the ArrayList.
		ArrayList<ChildBean> allChildren = new ArrayList<>();
		try (Statement statement = oracleClient.createStatement())
		{
			ResultSet resultSet = statement.executeQuery(query);
			while (resultSet.next())
			{
				ChildBean bean = new ChildBean();
				bean.setFirstname(resultSet.getString("Firstname"));
				bean.setSurname(resultSet.getString("Surname"));
				bean.setEmail(resultSet.getString("Email"));
				bean.setDOB(resultSet.getString("DOB"));
				bean.setAddress(resultSet.getString("Address"));
				bean.setPword(resultSet.getString("Password"));
				bean.setEmailForHomework(Boolean.parseBoolean(resultSet.getString("Email_Homework")));
				bean.setEmailForCalender(Boolean.parseBoolean(resultSet.getString("Email_Calender")));
				bean.setEmailForProfile(Boolean.parseBoolean(resultSet.getString("Email_Profile")));

				//Add bean to list of students
				allChildren.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		return allChildren;
	}
}
