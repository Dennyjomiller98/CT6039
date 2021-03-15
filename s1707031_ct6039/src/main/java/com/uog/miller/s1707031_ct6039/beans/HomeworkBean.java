package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class HomeworkBean
{
	static final Logger LOG = Logger.getLogger(HomeworkBean.class);
	private String fTeacher;
	private String fEventId;
	private String fName;
	private String fSetDate;
	private String fDueDate;
	private String fDescription;
	private String fClassId;

	public HomeworkBean()
	{
		//Empty No-Args
	}

	public HomeworkBean(ResultSet resultSet)
	{
		try
		{
			fEventId = resultSet.getString("Event_Id");
			fName = resultSet.getString("Name");
			fSetDate = resultSet.getString("Date_Set");
			fDueDate = resultSet.getString("Date_Due");
			fDescription = resultSet.getString("Description");
			fClassId = resultSet.getString("Class_Id");
			fTeacher = resultSet.getString("Tutor_Assigned");
		}
		catch (SQLException e)
		{
			LOG.error("Unable to create Bean from ResultSet", e);
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

	public String getName()
	{
		return fName;
	}
	public void setName(String name)
	{
		fName = name;
	}

	public String getSetDate()
	{
		return fSetDate;
	}
	public void setSetDate(String setDate)
	{
		fSetDate = setDate;
	}

	public String getDueDate()
	{
		return fDueDate;
	}
	public void setDueDate(String dueDate)
	{
		fDueDate = dueDate;
	}

	public String getDescription()
	{
		return fDescription;
	}
	public void setDescription(String description)
	{
		fDescription = description;
	}

	public String getClassId()
	{
		return fClassId;
	}
	public void setClassId(String classId)
	{
		fClassId = classId;
	}

	public String getTeacher()
	{
		return fTeacher;
	}
	public void setTeacher(String teacher)
	{
		fTeacher = teacher;
	}
}
