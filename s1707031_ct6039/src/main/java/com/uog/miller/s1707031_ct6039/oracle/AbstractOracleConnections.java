package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.ChildBean;
import com.uog.miller.s1707031_ct6039.beans.ParentBean;
import com.uog.miller.s1707031_ct6039.beans.TeacherBean;
import java.sql.*;
import java.util.ArrayList;
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
	public static final String CALENDARS_COLLECTION = "ct6039_calendar";
	public static final String PARENT_LINKS_COLLECTION = "ct6039_parent_links";
	public static final String CLASS_LINKS_COLLECTION = "ct6039_class_links";
	public static final String CLASS_COLLECTION = "ct6039_class";
	public static final String YEAR_COLLECTION = "ct6039_year";
	public static final String HOMEWORKS_COLLECTION = "ct6039_homeworks";
	public static final String HOMEWORK_SUBMISSIONS_COLLECTION = "ct6039_homework_submissions";
	public static final String HOMEWORK_FILES_COLLECTION = "ct6039_homework_files";

	private Connection oracleClient;

	public enum NotificationType
	{
		PROFILE, HOMEWORK, CALENDAR
	}

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

	//Used to check if we should email the user (if user is even found)
	//User can be Student/Child/Parent
	public boolean shouldUserBeNotified(String userEmail, NotificationType notificationType)
	{
		boolean ret;
		ret = checkUserExists(userEmail, notificationType);
		return ret;
	}

	private boolean checkUserExists(String email, NotificationType notificationType)
	{
		//Default to false in case of an error.
		boolean ret = false;
		email = email.toLowerCase();
		//Connect to DB, check if email is present
		setOracleDriver();
		try
		{
			Connection client = getOracleClient();
			if(client != null)
			{
				//Select Query
				String childQuery = "SELECT * FROM " + CHILDRENS_COLLECTION + " WHERE Email='" + email.replace("'", "''") +"'";
				String teacherQuery = "SELECT * FROM " + TEACHERS_COLLECTION + " WHERE Email='" + email.replace("'", "''") +"'";
				String parentQuery = "SELECT * FROM " + PARENTS_COLLECTION + " WHERE Email='" + email.replace("'", "''") +"'";
				//Execute query
				ArrayList<ChildBean> allChildren = executeChildQuery(client, childQuery);
				ArrayList<TeacherBean> allTeachers = executeTeacherQuery(client, teacherQuery);
				ArrayList<ParentBean> allParents = executeParentQuery(client, parentQuery);
				if(!allChildren.isEmpty())
				{
					ChildBean bean = allChildren.get(0);
					//Check Notification Preferences
					if(notificationType.equals(NotificationType.CALENDAR))
					{
						ret = bean.getEmailForCalendar();
					}
					else if(notificationType.equals(NotificationType.HOMEWORK))
					{
						ret = bean.getEmailForHomework();
					}
					else if(notificationType.equals(NotificationType.PROFILE))
					{
						ret = bean.getEmailForProfile();
					}
				}
				if(!allParents.isEmpty())
				{
					ret = false;
				}
				if(!allTeachers.isEmpty())
				{
					ret = false;
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to get user preferences", e);
		}
		return ret;
	}

	private ArrayList<ChildBean> executeChildQuery(Connection oracleClient, String query) throws SQLException
	{
		ArrayList<ChildBean> allChildren = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				ChildBean bean = new ChildBean(resultSet);
				allChildren.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();
		return allChildren;
	}

	private ArrayList<ParentBean> executeParentQuery(Connection oracleClient, String query) throws SQLException
	{
		ArrayList<ParentBean> allParent = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				ParentBean bean = new ParentBean(resultSet);
				allParent.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();
		return allParent;
	}

	private ArrayList<TeacherBean> executeTeacherQuery(Connection oracleClient, String query) throws SQLException
	{
		ArrayList<TeacherBean> allTeachers = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				TeacherBean bean = new TeacherBean(resultSet);
				allTeachers.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();
		return allTeachers;
	}
}
