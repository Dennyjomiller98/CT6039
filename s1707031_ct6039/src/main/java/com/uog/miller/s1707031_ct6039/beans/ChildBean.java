package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;

public class ChildBean
{
	private String fName;

	public ChildBean(ResultSet resultSet)
	{
		//fName = userDoc.name
	}

	public String getFirstname()
	{
		return fName;
	}
	public void setFirstname(String name)
	{
		fName = name;
	}

}
