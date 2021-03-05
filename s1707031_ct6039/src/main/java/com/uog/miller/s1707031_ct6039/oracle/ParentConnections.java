package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.ParentBean;
import java.sql.*;
import java.util.ArrayList;

public class ParentConnections extends AbstractOracleConnections
{
	//Allows Parent/Guardian User related requests
	public ParentConnections()
	{
		//Empty Constructor
	}

	public boolean registerParent(ParentBean beanToRegister)
	{
		boolean ret = false;
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Add Parent to DB
				String values = "'" +beanToRegister.getFirstname().replace("'", "''")
						+ "','" + beanToRegister.getSurname().replace("'", "''")
						+ "','" + beanToRegister.getEmail().replace("'", "''")
						+ "','" + beanToRegister.getDOB()
						+ "','" + beanToRegister.getAddress().replace("'", "''")
						+ "','" + beanToRegister.getLinkedChildIds()
						+ "','" + beanToRegister.getPword().replace("'", "''")
						+ "','" + beanToRegister.getEmailForHomework()
						+ "','" + beanToRegister.getEmailForCalendar()
						+ "','" + beanToRegister.getEmailForProfile() + "'";
				String query = "INSERT INTO " + PARENTS_COLLECTION +
						"(Firstname, Surname, Email, DOB, Address, Linked_Child_Id, Pword, Homework_Email, Calender_Email, Profile_Email)" + " VALUES (" + values + ")";
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

	public ParentBean login(String email, String pword)
	{
		ParentBean ret = null;
		boolean userExists = checkUserExists(email);
		if(userExists)
		{
			ret = validateCredentials(email, pword);
		}
		return ret;
	}

	public ParentBean validateCredentials(String email, String pword)
	{
		//Default to true in case of an error.
		ParentBean ret = null;
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
				String query = "SELECT * FROM " + PARENTS_COLLECTION + " WHERE Email='" + email.replace("'", "''") +"' AND Pword='" + pword.replace("'", "''") + "'";
				//Execute query
				ArrayList<ParentBean> allParents = executeQuery(oracleClient, query);
				if(allParents.size() == 1)
				{
					ret = allParents.get(0);
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
				String query = "SELECT * FROM " + PARENTS_COLLECTION + " WHERE Email='" + email.replace("'", "''") +"'";
				//Execute query
				ArrayList<ParentBean> allParents = executeQuery(oracleClient, query);
				if(allParents.isEmpty())
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
			LOG.error("Unable to see if Parent exists", e);
		}
		return ret;
	}

	private ArrayList<ParentBean> executeQuery(Connection oracleClient, String query) throws SQLException
	{
		//Executes SQL Query, any Parents found will populate the ArrayList.
		ArrayList<ParentBean> allParents = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				ParentBean bean = new ParentBean(resultSet);
				//Add bean to list of students
				allParents.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		return allParents;
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
				String query = "DELETE FROM " + PARENTS_COLLECTION
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
			LOG.error("Unable to delete user in oracle DB", e);
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

	public void updateAccount(ParentBean bean)
	{
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "UPDATE " + PARENTS_COLLECTION + " SET Firstname ='" + bean.getFirstname().replace("'", "''")
						+"', Surname='"+ bean.getSurname().replace("'", "''")
						+"', DOB='"+ bean.getDOB().replace("'", "''")
						+"', Address='"+ bean.getAddress().replace("'", "''")
						+"', Linked_Child_Id='"+ bean.getLinkedChildIds()
						+"', Pword='"+ bean.getPword().replace("'", "''")
						+"', Homework_Email='"+ bean.getEmailForHomework()
						+"', Calender_Email='"+ bean.getEmailForCalendar()
						+"', Profile_Email='"+ bean.getEmailForProfile()
						+"' WHERE Email='"+ bean.getEmail() +"'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to update parent information", e);
		}
	}
}
