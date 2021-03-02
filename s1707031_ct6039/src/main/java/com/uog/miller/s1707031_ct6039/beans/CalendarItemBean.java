package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class CalendarItemBean
{
	static final Logger LOG = Logger.getLogger(CalendarItemBean.class);
	private String fEventName;
	private String fEventId;
	private String fUser;
	private String fEventDate;
	private String fDateForUpdate;

	public CalendarItemBean()
	{
		//Empty No-Args
	}

	public CalendarItemBean(ResultSet resultSet)
	{
		try
		{
			fEventId = resultSet.getString("Event_Id");
			fEventName = resultSet.getString("Event_Name");
			fUser = resultSet.getString("User_Email");
			fEventDate = resultSet.getString("Event_Date");
			fDateForUpdate = resultSet.getString("Event_Update_Date");
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
	public void setEventId(String id)
	{
		fEventId = id;
	}

	public String getEventName()
	{
		return fEventName;
	}
	public void setEventName(String name)
	{
		fEventName = name;
	}

	public String getUser()
	{
		return fUser;
	}
	public void setUser(String userEmail)
	{
		fUser = userEmail;
	}

	public String getEventDate()
	{
		return fEventDate;
	}
	public void setEventDate(String date)
	{
		fEventDate = date;
	}

	public String getDateForUpdate()
	{
		return fDateForUpdate;
	}
	public void setDateForUpdate(String date)
	{
		fDateForUpdate = date;
	}
}
