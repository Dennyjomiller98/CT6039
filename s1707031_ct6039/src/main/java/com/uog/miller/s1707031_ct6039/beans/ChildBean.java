package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;

public class ChildBean
{
	private String fName;
	private String fSurname;
	private String fEmail;
	private String fDOB;
	private String fAddress;
	private String fYear;
	private String fPword;
	private boolean fProfileEmail;
	private boolean fCalenderEmail;
	private boolean fHomeworkEmail;

	public ChildBean()
	{
		//Empty No-Args
	}

	public ChildBean(ResultSet resultSet)
	{
		//fName = resultSet.name etc...
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
	public boolean getEmailForCalender()
	{
		return fCalenderEmail;
	}
	public void setEmailForCalender(boolean settings)
	{
		fCalenderEmail = settings;
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
