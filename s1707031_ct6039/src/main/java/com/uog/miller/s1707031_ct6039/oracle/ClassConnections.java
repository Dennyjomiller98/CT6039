package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.ClassBean;
import com.uog.miller.s1707031_ct6039.beans.ClassLinkBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

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

	public void addClass(ClassBean newClass)
	{
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Add Class to DB
				String values = "'" +newClass.getName().replace("'", "''").trim()
						+ "','" + newClass.getTeacher().replace("'", "''").trim()
						+ "','" + newClass.getYear().replace("'", "''").trim() + "'";
				String query = "INSERT INTO " + CLASS_COLLECTION +
						"(Name, Teacher, Year)" + " VALUES (" + values + ")";
				//Execute query
				executeAdditionQuery(oracleClient, query);
				oracleClient.close();
			}
			else
			{
				LOG.error("connection failure");
			}

		}
		catch(Exception e)
		{
			LOG.error("Unable to add class to Oracle DB", e);
		}
	}

	public void addClassLinks(List<ClassLinkBean> newLinks)
	{
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Add Class Link to DB
				for (ClassLinkBean newLink : newLinks)
				{
					String linkValues = "'" +newLink.getEventId().replace("'", "''").trim()
							+ "','" + newLink.getEmail().replace("'", "''").trim() + "'";
					String linkQuery = "INSERT INTO " + CLASS_LINKS_COLLECTION +
							"(Event_Id, Child_Email)" + " VALUES (" + linkValues + ")";
					//Execute query
					executeAdditionQuery(oracleClient, linkQuery);
				}
				oracleClient.close();

			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to add class to Oracle DB", e);
		}
	}

	public List<ClassLinkBean> getClassLinksFromChildEmail(String childEmail)
	{
		ArrayList<ClassLinkBean> ret = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + CLASS_LINKS_COLLECTION + " WHERE Child_Email='" + childEmail +"'";

				//Execute query
				ArrayList<ClassLinkBean> allLinks = executeRetrieveClassLinkQuery(oracleClient, query);
				if(!allLinks.isEmpty())
				{
					ret = allLinks;
				}
				else
				{
					LOG.debug("No Links Exists, cannot retrieve event.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to find existing class links", e);
		}
		return ret;
	}

	public List<ClassBean> getClassFromTeacherEmail(String teacherEmail)
	{
		ArrayList<ClassBean> ret = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + CLASS_COLLECTION + " WHERE Teacher='" + teacherEmail +"'";

				//Execute query
				ArrayList<ClassBean> allLinks = executeQuery(oracleClient, query);
				if(!allLinks.isEmpty())
				{
					ret = allLinks;
				}
				else
				{
					LOG.debug("No Links Exists, cannot retrieve event.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to find existing class links", e);
		}
		return ret;
	}

	public ClassBean getClassFromId(String classId)
	{
		ClassBean ret = new ClassBean();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + CLASS_COLLECTION + " WHERE Event_Id='" + classId +"'";

				//Execute query
				ArrayList<ClassBean> allLinks = executeQuery(oracleClient, query);
				if(!allLinks.isEmpty())
				{
					ret = allLinks.get(0);
				}
				else
				{
					LOG.debug("No Links Exists, cannot retrieve event.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to find existing class links", e);
		}
		return ret;
	}

	public List<ClassLinkBean> getChildrenFromClassId(String classId)
	{
		ArrayList<ClassLinkBean> ret = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + CLASS_LINKS_COLLECTION + " WHERE Event_Id='" + classId +"'";

				//Execute query
				ArrayList<ClassLinkBean> allLinks = executeRetrieveClassLinkQuery(oracleClient, query);
				if(!allLinks.isEmpty())
				{
					ret = allLinks;
				}
				else
				{
					LOG.debug("No Links Exists, cannot retrieve event.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to find existing class links", e);
		}
		return ret;
	}

	public void updateClass(ClassBean beanToUpdate)
	{
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "UPDATE " + CLASS_COLLECTION + " SET Name ='" + beanToUpdate.getName().replace("'", "''")
						+"', Teacher='"+ beanToUpdate.getTeacher().replace("'", "''")
						+"', Year='"+ beanToUpdate.getYear().replace("'", "''")
						+"' WHERE Event_Id='"+ beanToUpdate.getEventId() +"'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to update class information");
		}
	}

	public void deleteClass(String classId)
	{
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "DELETE FROM " + CLASS_COLLECTION
						+ " WHERE Event_Id='" + classId + "'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to delete class in oracle DB");
		}
	}

	public void deleteClassLinks(String classId)
	{
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "DELETE FROM " + CLASS_LINKS_COLLECTION
						+ " WHERE Event_Id='" + classId + "'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to delete class links in oracle DB");
		}
	}

	private void executeAdditionQuery(Connection oracleClient, String query)
	{
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			preparedStatement.executeUpdate(query);
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
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

	private ArrayList<ClassLinkBean> executeRetrieveClassLinkQuery(Connection oracleClient, String query) throws SQLException
	{
		ArrayList<ClassLinkBean> allEvents = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				ClassLinkBean bean = new ClassLinkBean(resultSet);
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
}
