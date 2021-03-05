package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.ClassBean;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class ClassConnections extends AbstractOracleConnections
{
	//Used to gather Class details
	public ClassConnections()
	{
		//Empty Constructor
	}

	//Checks if the teacher user is currently a teacher for a class.
	// If so, their account cannot be deleted until the class is assigned a new user.
	public boolean isTeacherForClass(String email)
	{
		//True default, just in case an error occurs, safer to not delete account
		boolean ret = true;
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select Query
				String query = "SELECT * FROM " + CLASS_COLLECTION + " WHERE Email='" + email.replace("'", "''") + "'";
				//Execute query
				ArrayList<ClassBean> allClasses = executeQuery(oracleClient, query);
				ret = !allClasses.isEmpty();
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve class info from Oracle", e);
		}
		return ret;
	}

	private ArrayList<ClassBean> executeQuery(Connection oracleClient, String query) throws SQLException
	{
		ArrayList<ClassBean> allEvents = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				ClassBean bean = new ClassBean(resultSet);
				//Add bean to list of students
				allEvents.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		return allEvents;
	}

	public void addClass()
	{
		//TODO - impl, add ClassBean and ClassLinkBean
	}

	public void getMyClass()
	{
		//TODO - impl, get ClassBean and ClassLinkBean
	}

	public void updateClass()
	{
		//TODO - impl, update ClassBean and ClassLinkBean (if needed)
	}

	public void deleteClass()
	{
		//TODO - impl, remove ClassBean and ClassLinkBean (if safe to do so)
	}
}
