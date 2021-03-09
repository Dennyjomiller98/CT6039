package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.ChildBean;
import com.uog.miller.s1707031_ct6039.beans.LinkBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LinkedConnections extends AbstractOracleConnections
{
	//Allows linking of Child/Parent accounts and Class/Children, to allow Parents/Guardian to view more information
	public LinkedConnections()
	{
		//Empty Constructor
	}

	public void linkChildren(String parent, String children)
	{
		//Any children linked allow the parent/guardian to view info on the child
		setOracleDriver();
		try
		{
			String[] split = children.split(",");
			for (String child : split)
			{
				AbstractOracleConnections conn = new AbstractOracleConnections();
				Connection oracleClient = conn.getOracleClient();
				if(oracleClient != null)
				{
					//Add Event to DB
					String values = "'" +child.replace("'", "''").trim()
							+ "','" + parent.replace("'", "''").trim() + "'";
					String query = "INSERT INTO " + PARENT_LINKS_COLLECTION +
							"(Child_Email, Parent_Email)" + " VALUES (" + values + ")";
					//Execute query
					executeAdditionQuery(oracleClient, query);
				}
				else
				{
					LOG.error("connection failure");
				}
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to add parent-child link to Oracle DB", e);
		}
	}

	private void executeAdditionQuery(Connection oracleClient, String query) throws SQLException
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

	private ArrayList<ChildBean> getChildren()
	{
		ArrayList<ChildBean> allChildren = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select Query
				String query = "SELECT * FROM " + CHILDRENS_COLLECTION;
				//Execute query
				allChildren = executeQuery(oracleClient, query);
				if(allChildren.isEmpty())
				{
					LOG.error("Could not find Children.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve Children", e);
		}
		return allChildren;
	}

	public List<ChildBean> getAllChildrenFromLinks(String parentEmail)
	{
		List<ChildBean> allBeans = new ArrayList<>();
		List<LinkBean> allChildLinksForParent = findAllChildLinksForParent(parentEmail);
		for (LinkBean childLink : allChildLinksForParent)
		{
			ChildConnections connections = new ChildConnections();
			List<ChildBean> childBean = connections.getChildBean(childLink.getChildEmail().trim());
			allBeans.addAll(childBean);
		}
		return allBeans;
	}

	//Get all links for parent
	public String getAllLinks(String parentEmail)
	{
		List<String> allIds = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select Query
				String query = "SELECT * FROM " + PARENT_LINKS_COLLECTION + " WHERE Parent_Email='"+ parentEmail+"'";
				//Execute query
				allIds = executeLinkQuery(oracleClient, query);
				if(allIds.isEmpty())
				{
					LOG.error("Could not find Children.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve Children", e);
		}

		return String.join(", ", allIds);
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
				ChildBean childBean = new ChildBean(resultSet);
				allChildren.add(childBean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		return allChildren;
	}

	private List<String> executeLinkQuery(Connection oracleClient, String query) throws SQLException
	{
		List<String> allIds = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				String eventId = resultSet.getString("Event_Id");
				if(eventId != null)
				{
					allIds.add(eventId);
				}
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		return allIds;
	}

	//Gets children for Parent register/update linking
	public Map<String, String> getAllChildren()
	{
		Map<String, String> foundChildren = new HashMap<>();
		ArrayList<ChildBean> children = getChildren();
		for (ChildBean child : children)
		{
			String fullName = child.getFirstname() + " " + child.getSurname();
			foundChildren.put(child.getEmail(), fullName);
		}
		return foundChildren;
	}

	public List<LinkBean> findAllLinksForChild(String email)
	{
		List<LinkBean> ret = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select Query
				String query = "SELECT * FROM " + PARENT_LINKS_COLLECTION + " WHERE Child_Email='"+ email+"'";
				//Execute query
				List<LinkBean> linkBeans = executeLinkBeanQuery(oracleClient, query);
				if(linkBeans.isEmpty())
				{
					LOG.error("Could not find Links.");
				}
				else
				{
					ret = linkBeans;
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve LinkBeans", e);
		}
		return ret;
	}

	public List<LinkBean> findAllChildLinksForParent(String parentEmail)
	{
		List<LinkBean> ret = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select Query
				String query = "SELECT * FROM " + PARENT_LINKS_COLLECTION + " WHERE Parent_Email='"+ parentEmail+"'";
				//Execute query
				List<LinkBean> linkBeans = executeLinkBeanQuery(oracleClient, query);
				if(linkBeans.isEmpty())
				{
					LOG.error("Could not find Links.");
				}
				else
				{
					ret = linkBeans;
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve LinkBeans", e);
		}
		return ret;
	}

	private List<LinkBean> executeLinkBeanQuery(Connection oracleClient, String query) throws SQLException
	{
		List<LinkBean> allLinkBeans = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				LinkBean bean = new LinkBean(resultSet);
				allLinkBeans.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		return allLinkBeans;
	}

	public void removeLink(String linkId)
	{
		LOG.debug("Attempting to delete parent-child link: " + linkId);
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "DELETE FROM " + PARENT_LINKS_COLLECTION
						+ " WHERE Event_Id='" + linkId.trim() + "'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to delete parent-child link", e);
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
