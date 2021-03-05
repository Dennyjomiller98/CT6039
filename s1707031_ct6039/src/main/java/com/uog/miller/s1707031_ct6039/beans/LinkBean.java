package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Bean for Parent-Child links, contains link ID, Child Email, and Parent Email
 * */
public class LinkBean
{
	private String fId;
	private String fChild;
	private String fParent;

	public LinkBean()
	{
		//Empty No-Args
	}

	public LinkBean(ResultSet resultSet) throws SQLException
	{
		fId = resultSet.getString("Event_Id");
		fChild = resultSet.getString("Child_Email");
		fParent = resultSet.getString("Parent_Email");
	}

	public String getId()
	{
		return fId;
	}
	public void setId(String id)
	{
		fId = id;
	}

	public String getChildEmail()
	{
		return fChild;
	}
	public void setChildEmail(String child)
	{
		fChild = child;
	}

	public String getParentEmail()
	{
		return fParent;
	}
	public void setParentEmail(String parent)
	{
		fParent = parent;
	}
}
