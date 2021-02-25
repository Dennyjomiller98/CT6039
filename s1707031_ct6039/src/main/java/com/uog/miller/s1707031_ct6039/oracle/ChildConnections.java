package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.ChildBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
				String values = "'" +beanToRegister.getFirstname().replace("'", "''")
						+ "','" + beanToRegister.getSurname().replace("'", "''")
						+ "','" + beanToRegister.getEmail().replace("'", "''")
						+ "','" + beanToRegister.getDOB()
						+ "','" + beanToRegister.getAddress().replace("'", "''")
						+ "','" + beanToRegister.getYear()
						+ "','" + beanToRegister.getPword().replace("'", "''")
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

	public ChildBean login(String email, String pword)
	{
		ChildBean ret = null;
		boolean userExists = checkUserExists(email);
		if(userExists)
		{
			ret = validateCredentials(email, pword);
		}
		return ret;
	}

	private ChildBean validateCredentials(String email, String pword)
	{
		//Default to true in case of an error.
		ChildBean ret = null;
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
				String query = "SELECT * FROM " + CHILDRENS_COLLECTION + " WHERE Email='" + email.replace("'", "''") +"' AND Pword='" + pword.replace("'", "''") + "'";
				//Execute query
				ArrayList<ChildBean> allChildren = executeQuery(oracleClient, query);
				if(allChildren.size() == 1)
				{
					ret = allChildren.get(0);
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

	public void deleteAccount()
	{
		//Needs Auth
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
				String query = "SELECT * FROM " + CHILDRENS_COLLECTION + " WHERE Email='" + email.replace("'", "''") +"'";
				//Execute query
				ArrayList<ChildBean> allChildren = executeQuery(oracleClient, query);
				if(allChildren.isEmpty())
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
			LOG.error("Unable to see if child exists", e);
		}

		return ret;
	}

	private ArrayList<ChildBean> executeQuery(Connection oracleClient, String query) throws SQLException
	{
		//Executes SQL Query, any Children found will populate the ArrayList.
		ArrayList<ChildBean> allChildren = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				ChildBean bean = new ChildBean(resultSet);
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
