package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.CalendarItemBean;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CalendarConnections extends AbstractOracleConnections
{
	//Used for requests to Calendar, such as Viewing or adding to calendar
	public CalendarConnections()
	{
		//Empty Constructor
	}

	public String addCalendarItemForUser(String user, String name, String date, String dateForUpdate)
	{
		//Make method add Event, and return the ID of new created event
		String ret=null;
		LOG.debug("Connecting to DB to add event");
		setOracleDriver();
		try
		{
			//Replace date values in the event of "2", not "02" (which is needed for HTML Form values)
			dateForUpdate = formMonthAndDayReplacement(dateForUpdate);

			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Add Event to DB
				String values = "'" +user.replace("'", "''")
						+ "','" + name.replace("'", "''")
						+ "','" + date.replace("'", "''")
						+ "','" + dateForUpdate + "'";
				String query = "INSERT INTO " + CALENDARS_COLLECTION +
						"(User_Email, Event_Name, Event_date, Event_Update_Date)" + " VALUES (" + values + ")";
				//Execute query
				executeAdditionQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to add Event to Oracle DB", e);
		}

		//Check event is created
		String eventId = checkEventExists(user.replace("'", "''"), name.replace("'", "''"), dateForUpdate.replace("'", "''"));
		if(eventId != null)
		{
			ret = eventId;
		}
		return ret;
	}

	private String formMonthAndDayReplacement(String dateForUpdate)
	{
		String retDate;
		String[] split = dateForUpdate.split("-");
		String month = split[1];
		String day = split[2];
		//Replace Day values
		day = replaceSingleInt(day);
		//Replace Month Values
		month = replaceSingleInt(month);
		//Rebuild date
		retDate = split[0]+"-"+month+"-"+day;
		return retDate;
	}

	private String replaceSingleInt(String val)
	{
		switch (val)
		{
			case "0":
				val = "00";
				break;
			case "1":
				val = "01";
				break;
			case "2":
				val = "02";
				break;
			case "3":
				val = "03";
				break;
			case "4":
				val = "04";
				break;
			case "5":
				val = "05";
				break;
			case "6":
				val = "06";
				break;
			case "7":
				val = "07";
				break;
			case "8":
				val = "08";
				break;
			case "9":
				val = "09";
				break;
			default:
		}
		return val;
	}

	public List<CalendarItemBean> getAllCalendarItemsForUser(String user)
	{
		return findAllEvents(user);
	}

	public CalendarItemBean getCalendarItemForUser(String user, String eventId)
	{
		return findEventWithId(user, eventId);
	}

	private CalendarItemBean findEventWithId(String user, String eventId)
	{
		CalendarItemBean ret = new CalendarItemBean();
		//Connect to DB, check if event is present
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + CALENDARS_COLLECTION + " WHERE User_Email='" + user +"' AND Event_Id='"+eventId+"'";

				//Execute query
				ArrayList<CalendarItemBean> allEvents = executeQuery(oracleClient, query);
				if(!allEvents.isEmpty())
				{
					ret = allEvents.get(0);
				}
				else
				{
					LOG.debug("No Event Exists, cannot retrieve event.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to see if event exists", e);
		}
		return ret;
	}

	private ArrayList<CalendarItemBean> findAllEvents(String user)
	{
		ArrayList<CalendarItemBean> ret = new ArrayList<>();
		//Connect to DB, check if event is present
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + CALENDARS_COLLECTION + " WHERE User_Email='" + user +"'";

				//Execute query
				ArrayList<CalendarItemBean> allEvents = executeQuery(oracleClient, query);
				if(!allEvents.isEmpty())
				{
					ret = allEvents;
				}
				else
				{
					LOG.debug("No Event Exists, cannot retrieve event.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to see if event exists", e);
		}
		return ret;
	}

	public String checkEventExists(String user, String name, String date)
	{
		String ret = null;
		//Connect to DB, check if event is present
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + CALENDARS_COLLECTION + " WHERE User_Email='" + user +"' AND Event_Name='"+name+"' AND Event_Update_Date='"+date+"'";

				//Execute query
				ArrayList<CalendarItemBean> allEvents = executeQuery(oracleClient, query);
				if(!allEvents.isEmpty())
				{
					ret = allEvents.get(0).getEventId();
				}
				else
				{
					LOG.debug("No Event Exists, cannot retrieve event.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to see if event exists", e);
		}
		return ret;
	}

	private ArrayList<CalendarItemBean> executeQuery(Connection oracleClient, String query) throws SQLException
	{
		//Executes SQL Query, any Events found will populate the ArrayList.
		ArrayList<CalendarItemBean> allEvents = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				CalendarItemBean bean = new CalendarItemBean(resultSet);
				//Add bean to list of students
				allEvents.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		return allEvents;
	}

	private void executeAdditionQuery(Connection oracleClient, String query) throws SQLException
	{
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			preparedStatement.executeUpdate(query);
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();
	}

	public void updateEvent(CalendarItemBean originalBean, CalendarItemBean updatedValues)
	{
		updateBean(originalBean, updatedValues);
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "UPDATE " + CALENDARS_COLLECTION + " SET Event_Name ='" + originalBean.getEventName().replace("'", "''")
						+"', Event_Update_Date='"+ originalBean.getEventDate().replace("'", "''")
						+"', Event_Date='"+ originalBean.getDateForUpdate().replace("'", "''")
						+"' WHERE Event_Id='"+ originalBean.getEventId() +"'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to update event information");
		}
	}

	private void executeUpdateQuery(Connection oracleClient, String query) throws SQLException
	{
		try (Statement statement = oracleClient.createStatement())
		{
			statement.executeUpdate(query);
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();
	}

	private void updateBean(CalendarItemBean originalBean, CalendarItemBean updatedValues)
	{
		if (updatedValues.getEventName() != null)
		{
			originalBean.setEventName(updatedValues.getEventName());
		}
		if (updatedValues.getEventDate() != null)
		{
			originalBean.setEventDate(updatedValues.getEventDate());
		}
		if (updatedValues.getDateForUpdate() != null)
		{
			originalBean.setDateForUpdate(updatedValues.getDateForUpdate());
		}
	}

	public void deleteEvent(String eventId)
	{
		LOG.debug("Attempting to delete event");
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "DELETE FROM " + CALENDARS_COLLECTION
						+ " WHERE Event_Id='" + eventId + "'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to delete event in oracle DB");
		}
	}
}
