package com.uog.miller.s1707031_ct6039.oracle;

import java.util.HashMap;
import java.util.Map;

public class YearConnections extends AbstractOracleConnections
{
	//Used to store information for each year
	public YearConnections()
	{
		//Empty Constructor
	}

	public Map<String, String> getAllYearsAndNames()
	{
		//Get Map<yearID, yearName> of all years in DB
		Map<String, String> allYears = new HashMap<>();
		return allYears;
	}

	public String getYearIDFromName(String name)
	{
		String ret = null;
		return ret;
	}

	public String getYearNameFromID(String ID)
	{
		String ret = null;
		return ret;
	}
}
