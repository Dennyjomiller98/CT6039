package com.uog.miller.s1707031_ct6039.oracle;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class YearConnections extends AbstractOracleConnections
{
	//Used to store information for each year
	public YearConnections()
	{
		//Empty Constructor
	}

	//Returns Map<ID/Name> of all ClassYears in the school (Recep/Yr1/etc)
	public Map<String, String> getAllClassYears()
	{
		Map<String, String> allClassYears = new HashMap<>();

		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select Query
				String query = "SELECT * FROM " + YEAR_COLLECTION;
				//Execute query
				allClassYears = executeQuery(oracleClient, query);
				if(allClassYears.isEmpty())
				{
					LOG.error("Could not find ClassYears.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve Years", e);
		}
		return allClassYears;
	}

	private Map<String,String> executeQuery(Connection oracleClient, String query) throws SQLException
	{
		//Executes SQL Query, any Children found will populate the ArrayList.
		Map<String,String> allYears = new HashMap<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				String id = resultSet.getString("Id");
				String name = resultSet.getString("Name");
				//Add bean to list of students
				allYears.put(id, name);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		return allYears;
	}
}
