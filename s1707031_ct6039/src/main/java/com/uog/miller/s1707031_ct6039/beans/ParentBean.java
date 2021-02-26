package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class ParentBean
{
	static final Logger LOG = Logger.getLogger(ParentBean.class);
	private String fEmail;
	private String fAddress;
	private String fDOB;
	private String fLinkedChildIds;
	private String fPword;
	private boolean fHomeworkEmail;
	private boolean fCalendarEmail;
	private boolean fProfileEmail;
	private String fName;
	private String fSurname;

	public ParentBean()
	{
		//Empty Constructor
	}

	public ParentBean(ResultSet resultSet)
	{
		try
		{
			fName = resultSet.getString("Firstname");
			fSurname = resultSet.getString("Surname");
			fEmail = resultSet.getString("Email");
			fDOB = resultSet.getString("DOB");
			fAddress = resultSet.getString("Address");
			fLinkedChildIds = resultSet.getString("linkedChildIds");
			fPword = resultSet.getString("Pword");
			fHomeworkEmail = resultSet.getBoolean("Homework_Email");
			fCalendarEmail = resultSet.getBoolean("Calendar_Email");
			fProfileEmail = resultSet.getBoolean("Profile_Email");
		}
		catch (SQLException throwables)
		{
			LOG.error("Unable to create Bean from ResultSet", throwables);
		}
	}

	public String getFirstname()
	{
		return fName;
	}
	public void setFirstname(String name)
	{
		fName = name;
	}

	public String getSurname()
	{
		return fSurname;
	}
	public void setSurname(String surname)
	{
		fSurname = surname;
	}

	public String getEmail()
	{
		return fEmail;
	}
	public void setEmail(String email)
	{
		fEmail = email;
	}

	public String getDOB()
	{
		return fDOB;
	}
	public void setDOB(String dob)
	{
		fDOB = dob;
	}

	public String getAddress()
	{
		return fAddress;
	}
	public void setAddress(String addr)
	{
		fAddress = addr;
	}

	public String getLinkedChildIds()
	{
		return fLinkedChildIds;
	}
	public void setLinkedChildIds(String linkedChildIds)
	{
		fLinkedChildIds = linkedChildIds;
	}

	public String getPword()
	{
		return fPword;
	}
	public void setPword(String pword)
	{
		fPword = pword;
	}

	/**
	 *	Settings ID - Used to know when to email User
	 * */
	public boolean getEmailForHomework()
	{
		return fHomeworkEmail;
	}
	public void setEmailForHomework(boolean settings)
	{
		fHomeworkEmail = settings;
	}
	public boolean getEmailForCalendar()
	{
		return fCalendarEmail;
	}
	public void setEmailForCalendar(boolean settings)
	{
		fCalendarEmail = settings;
	}
	public boolean getEmailForProfile()
	{
		return fProfileEmail;
	}
	public void setEmailForProfile(boolean settings)
	{
		fProfileEmail = settings;
	}
}
