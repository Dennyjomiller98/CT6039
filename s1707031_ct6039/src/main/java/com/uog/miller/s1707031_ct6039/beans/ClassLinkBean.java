package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class ClassLinkBean
{
	static final Logger LOG = Logger.getLogger(ClassLinkBean.class);
	private String fEventId;
	private String fEmail;

	public ClassLinkBean()
	{
		//Empty No-Args
	}

	public ClassLinkBean(ResultSet resultSet)
	{
		try
		{
			fEventId = resultSet.getString("Event_Id");
			fEmail = resultSet.getString("Child_Email");
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

	public String getEmail()
	{
		return fEmail;
	}
	public void setEmail(String email)
	{
		fEmail = email;
	}
}
