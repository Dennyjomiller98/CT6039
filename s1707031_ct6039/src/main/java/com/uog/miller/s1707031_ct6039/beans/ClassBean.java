package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class ClassBean
{
	//Bean for teacher, children, what year they are (reception/year 1 etc)...also used for progress & hw tables
	static final Logger LOG = Logger.getLogger(ClassBean.class);
	private String fEventId;
	private String fName;
	private String fTeacher;
	private String fYear;

	public ClassBean()
	{
		//Empty No-Args
	}

	public ClassBean(ResultSet resultSet)
	{
		try
		{
			fEventId = resultSet.getString("Event_Id");
			fName = resultSet.getString("Name");
			fTeacher = resultSet.getString("Teacher");
			fYear = resultSet.getString("Year");
		}
		catch (SQLException throwables)
		{
			LOG.error("Unable to create Bean from ResultSet", throwables);
		}
	}

	public String getEventId()
	{
		return fEventId;
	}
	public void setEventId(String eventId)
	{
		fEventId = eventId;
	}

	public String getName()
	{
		return fName;
	}
	public void setName(String name)
	{
		fName = name;
	}

	public String getTeacher()
	{
		return fTeacher;
	}
	public void setTeacher(String teacher)
	{
		fTeacher = teacher;
	}

	public String getYear()
	{
		return fYear;
	}
	public void setYear(String year)
	{
		fYear = year;
	}
}
