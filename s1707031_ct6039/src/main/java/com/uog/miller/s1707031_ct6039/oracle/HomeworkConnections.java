package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.HomeworkBean;
import com.uog.miller.s1707031_ct6039.beans.SubmissionBean;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class HomeworkConnections extends AbstractOracleConnections
{
	//Used for Connecting to Homework Tables
	public HomeworkConnections()
	{
		//Empty Constructor
	}

	//Creates homework task with an ID, which is used to set homework for children
	//Returns the ID of the newly created task
	public String addHomeworkTask(HomeworkBean beanToAdd)
	{
		String ret;
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{

				//Add Homework to DB
				String values = "'" +beanToAdd.getName().replace("'", "''").trim()
						+ "','" + beanToAdd.getSetDate().replace("'", "''").trim()
						+ "','" + beanToAdd.getDueDate().replace("'", "''").trim()
						+ "','" + beanToAdd.getDescription().replace("'", "''").trim()
						+ "','" + beanToAdd.getClassId().replace("'", "''").trim()
						+ "','" + beanToAdd.getTeacher().replace("'", "''").trim() + "'";
				String query = "INSERT INTO " + HOMEWORKS_COLLECTION +
						"(Name, Date_Set, Date_Due, Description, Class_Id, Tutor_Assigned)" + " VALUES (" + values + ")";
				//Execute query
				executeAdditionQuery(oracleClient, query);
				oracleClient.close();
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to add homework to Oracle DB", e);
		}

		ret = getLatestHomeworkTask(beanToAdd);
		return ret;
	}

	//Gets ID of recently created HW, used to populate child submissions
	private String getLatestHomeworkTask(HomeworkBean beanToAdd)
	{
		String ret = null;
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + HOMEWORKS_COLLECTION + " WHERE Name='" + beanToAdd.getName().replace("'", "''").trim() +"'"
						+ " AND Date_Set='"+ beanToAdd.getSetDate().replace("'", "''").trim() +"'"
						+ " AND Date_Due='"+ beanToAdd.getDueDate().replace("'", "''").trim() +"'"
						+ " AND Description='"+ beanToAdd.getDescription().replace("'", "''").trim() +"'"
						+ " AND Class_Id='"+ beanToAdd.getClassId().replace("'", "''").trim() +"'"
						+ " AND Tutor_Assigned='"+ beanToAdd.getTeacher().replace("'", "''").trim() +"'";

				//Execute query
				String foundHomework = executeRetrieveLatestAdditionQuery(oracleClient, query);
				if(foundHomework != null)
				{
					ret = foundHomework;
				}
				else
				{
					LOG.debug("No Homework Tasks found, cannot retrieve ID.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to find existing Homework Tasks", e);
		}

		return ret;
	}

	//Use addHomeworkTask to get ID, and use this method when looping each child in the class, create new homework data
	public void addHomeworkForChildren(String homeworkId, String childEmail)
	{
		if(homeworkId != null && childEmail != null)
		{
			//add child, set "Submission" to null/empty, don't set.
			setOracleDriver();
			try
			{
				AbstractOracleConnections conn = new AbstractOracleConnections();
				Connection oracleClient = conn.getOracleClient();
				if(oracleClient != null)
				{

					//Add Homework to DB
					String values = "'" +homeworkId.replace("'", "''").trim()
							+ "','" + childEmail.replace("'", "''").trim() + "'";
					String query = "INSERT INTO " + HOMEWORK_SUBMISSIONS_COLLECTION +
							"(Event_Id, Child_Email)" + " VALUES (" + values + ")";
					//Execute query
					executeAdditionQuery(oracleClient, query);
					oracleClient.close();
				}
				else
				{
					LOG.error("connection failure");
				}
			}
			catch(Exception e)
			{
				LOG.error("Unable to add homework to Oracle DB", e);
			}
		}
	}

	private void executeAdditionQuery(Connection oracleClient, String query)
	{
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			preparedStatement.executeUpdate(query);
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
	}

	private String executeRetrieveLatestAdditionQuery(Connection oracleClient, String query) throws SQLException
	{
		String ret = null;
		ArrayList<HomeworkBean> allEvents = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				HomeworkBean bean = new HomeworkBean(resultSet);
				//Add bean to list
				allEvents.add(bean);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();

		//Check beans, return ID if found
		if(!allEvents.isEmpty())
		{
			ret = allEvents.get(0).getEventId();
		}

		return ret;
	}







	//Updates homework submissions to contain SubmissionID for homework_files, returns response of submitting HW file.
	public String submitHomeworkTask(SubmissionBean bean, InputStream inputStream, String filename)
	{
		String result = null;
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				String fileId = null;

				//Upload Submission file
				if (inputStream != null)
				{
					fileId = addHomeworkFileToDB(inputStream, filename, oracleClient);
				}

				//Add Submission to DB
				if(fileId != null)
				{
					AbstractOracleConnections conn2 = new AbstractOracleConnections();
					Connection oracleClient2 = conn2.getOracleClient();
					bean.setSubmissionId(fileId);
					String sql = "INSERT INTO "+HOMEWORK_SUBMISSIONS_COLLECTION+" (Date_Submitted, Submission_Id) values (?, ?) WHERE Child_Email='"+bean.getEmail()+"' AND Event_Id='"+bean.getEventId()+"'";
					result = addChildSubmissionToDB(oracleClient2, sql, bean);
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to add submission to Oracle DB", e);
		}

		return result;
	}

	private String addChildSubmissionToDB(Connection oracleClient, String sql, SubmissionBean bean) throws SQLException
	{
		String ret = null;
		try(PreparedStatement statement = oracleClient.prepareStatement(sql))
		{
			statement.setString(1, bean.getSubmissionDate());
			statement.setString(2, bean.getSubmissionId());

			// sends the statement to the database server
			int row = statement.executeUpdate();
			if (row > 0)
			{
				ret = "File uploaded and saved into database";
			}
			else
			{
				ret = "File not uploaded correctly.";
			}
		}
		catch (SQLException e)
		{
			LOG.error("Error submitting HW File", e);
		}
		finally
		{
			oracleClient.close();
		}
		return ret;
	}

	private String addHomeworkFileToDB(InputStream inputStream, String filename, Connection oracleClient) throws SQLException
	{
		String fileId = null;
		String sql = "INSERT INTO "+HOMEWORK_FILES_COLLECTION+" (File_Data, Filename) values (?, ?)";
		try(PreparedStatement statement = oracleClient.prepareStatement(sql))
		{
			statement.setBlob(1, inputStream);
			statement.setString(1, filename);
			statement.executeUpdate();

			//Get ID of uploaded file
			fileId = getLatestHomeworkSubmission();
		}
		catch (SQLException e)
		{
			LOG.error("Error submitting HW File", e);
		}
		finally
		{
			oracleClient.close();
		}
		return fileId;
	}

	//Gets latest HW file upload record
	private String getLatestHomeworkSubmission()
	{
		String fileId = null;
		AbstractOracleConnections conn = new AbstractOracleConnections();
		String sql = "SELECT * FROM "+ HOMEWORK_FILES_COLLECTION + " WHERE Event_Id = ( SELECT MAX(Event_Id) FROM " +HOMEWORK_FILES_COLLECTION+ " )";
		//SELECT * FROM HOMEWORK_FILES_COLLECTION WHERE Event_Id = ( SELECT MAX(Event_Id) FROM HOMEWORK_FILES_COLLECTION )
		try (Connection oracleClient = conn.getOracleClient(); PreparedStatement statement = oracleClient.prepareStatement(sql))
		{
			ResultSet resultSet = statement.executeQuery();
			//Get ID of uploaded file
			while (resultSet.next())
			{
				fileId = resultSet.getString("Submission_Id");
			}
		}
		catch (SQLException e)
		{
			LOG.error("Error submitting HW File", e);
		}
		return fileId;
	}












	public Object getHomeworkUpload(SubmissionBean bean)
	{
		String submissionId = bean.getSubmissionId();
		//Search homework_files DB for submission ID, return data here

		//This is the childs submission (file/jsp/etc)
		List<HomeworkBean> ret = new ArrayList<>();
		return ret;
	}

	public List<HomeworkBean> getAllHomeworkForChild(String childEmail)
	{
		List<HomeworkBean> ret = new ArrayList<>();
		return ret;
	}

	public List<HomeworkBean> getAllOutstandingHomeworkForChild(String childEmail)
	{
		List<HomeworkBean> ret = new ArrayList<>();
		return ret;
	}

	public List<HomeworkBean> getAllSubmittedHomeworkForChild(String childEmail)
	{
		List<HomeworkBean> ret = new ArrayList<>();
		return ret;
	}
}
