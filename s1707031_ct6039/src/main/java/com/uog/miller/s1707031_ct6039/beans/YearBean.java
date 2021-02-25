package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class YearBean
{
	static final Logger LOG = Logger.getLogger(YearBean.class);
	private String fID;
	private String fName;

	public YearBean()
	{
		//Empty Constructor
	}

	public YearBean(ResultSet resultSet)
	{
		try
		{
			fName = resultSet.getString("Firstname");
			fID = resultSet.getString("Column_Id");
		}
		catch(SQLException e)
		{
			LOG.error("Unable to create Bean from ResultSet", e);
		}
	}

	public String getName()
	{
		return fName;
	}
	public void setName(String name)
	{
		fName = name;
	}

	public String getID()
	{
		return fID;
	}
	public void setID(String id)
	{
		fID = id;
	}
}
