package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class TeacherBean
{
	static final Logger LOG = Logger.getLogger(TeacherBean.class);

	private String fTitle;
	private String fYear;
	private String fName;
	private String fEmail;
	private String fSurname;
	private String fDOB;
	private String fAddress;
	private String fPword;
	private boolean fHomeworkEmail;
	private boolean fCalendarEmail;
	private boolean fProfileEmail;

	public TeacherBean()
	{
		//Empty No-Args
	}

	public TeacherBean(ResultSet resultSet)
	{
		try
		{
			fName = resultSet.getString("Firstname");
			fSurname = resultSet.getString("Surname");
			fEmail = resultSet.getString("Email");
			fDOB = resultSet.getString("DOB");
			fAddress = resultSet.getString("Address");
			fYear = resultSet.getString("Year");
			fPword = resultSet.getString("Pword");
			fTitle = resultSet.getString("Title");
			fHomeworkEmail = resultSet.getBoolean("Homework_Email");
			fCalendarEmail = resultSet.getBoolean("Calender_Email");
			fProfileEmail = resultSet.getBoolean("Profile_Email");
		}
		catch(SQLException throwables)
		{
			LOG.error("Unable to create Bean from ResultSet", throwables);
		}
	}

	public String getTitle()
	{
		return fTitle;
	}
	public void setTitle(String title)
	{
		fTitle = title;
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

	public String getYear()
	{
		return fYear;
	}
	public void setYear(String year)
	{
		fYear = year;
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
