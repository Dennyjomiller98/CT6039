package com.uog.miller.s1707031_ct6039.oracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class AbstractOracleConnections
{
	static final Logger LOG = Logger.getLogger(AbstractOracleConnections.class);

	public static final String ORACLE_USERNAME = "s1707031";
	public static final String ORACLE_DRIVER = "oracle.jdbc.driver.OracleDriver";
	public static final String ORACLE_DRIVER_URL = "jdbc:oracle:thin:@//oracle.glos.ac.uk:1521/orclpdb.chelt.local";
	public static final String ORACLE_PASSWORD = "UOGs1707031";

	public static final String CHILDRENS_COLLECTION = "ct6039_children";
	public static final String TEACHERS_COLLECTION = "ct6039_teachers";
	public static final String PARENTS_COLLECTION = "ct6039_parents";
	public static final String RESOURCES_COLLECTION = "ct6039_resources";
	public static final String CALENDERS_COLLECTION = "ct6039_calender";
	public static final String HOMEWORKS_COLLECTION = "ct6039_homeworks";
	public static final String PROGRESS_COLLECTION = "ct6039_progress";
	private Connection oracleClient;

	protected AbstractOracleConnections()
	{
		//Empty constructor
	}

	public Connection getOracleClient()
	{
		init();
		return oracleClient;
	}

	public void init()
	{
		try
		{
			oracleClient = DriverManager.getConnection(ORACLE_DRIVER_URL, ORACLE_USERNAME, ORACLE_PASSWORD);
		}
		catch (SQLException throwables)
		{
			LOG.error(throwables);
		}
	}

	public void setOracleDriver()
	{
		try
		{
			//Is required or oracleClient Connection is always null
			Class.forName(ORACLE_DRIVER);
		}
		catch (Exception e)
		{
			LOG.error("Driver class not found", e);
		}
	}
}
