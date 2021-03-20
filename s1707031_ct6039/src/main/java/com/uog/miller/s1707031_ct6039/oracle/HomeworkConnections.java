package com.uog.miller.s1707031_ct6039.oracle;

import com.uog.miller.s1707031_ct6039.beans.HomeworkBean;
import com.uog.miller.s1707031_ct6039.beans.SubmissionBean;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.http.HttpServletResponse;

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
					result = addChildSubmissionToDB(oracleClient2, bean);
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

	private String addChildSubmissionToDB(Connection oracleClient, SubmissionBean bean) throws SQLException
	{
		String ret = null;
		String sql = "UPDATE "+HOMEWORK_SUBMISSIONS_COLLECTION+" SET Date_Submitted='"+bean.getSubmissionDate()+"', Submission_Id='"+bean.getSubmissionId()+"' WHERE Child_Email='"+bean.getEmail()+"' AND Event_Id='"+bean.getEventId()+"'";
		try(PreparedStatement statement = oracleClient.prepareStatement(sql))
		{
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
			statement.setString(2, filename);
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
		String sql = "SELECT * FROM "+ HOMEWORK_FILES_COLLECTION + " WHERE Submission_Id = ( SELECT MAX(Submission_Id) FROM " +HOMEWORK_FILES_COLLECTION+ " )";
		//SELECT * FROM HOMEWORK_FILES_COLLECTION WHERE Event_Id = ( SELECT MAX(Event_Id) FROM HOMEWORK_FILES_COLLECTION )
		try (Connection oracleClient = conn.getOracleClient();
			 PreparedStatement statement = oracleClient.prepareStatement(sql))
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

	//Retrieves List<HomeworkBean>
	public List<HomeworkBean> getAllHomeworkForChild(String childEmail)
	{
		//Get HomeworkSubmissions to find HW task ID
		List<SubmissionBean> allHomeworkSubmissionsForChild = getAllHomeworkSubmissionsForChild(childEmail);
		StringBuilder whereClause = createWhereClause(allHomeworkSubmissionsForChild);

		//Now get Homework Tasks set
		List<HomeworkBean> ret = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query;
				if(whereClause != null)
				{
					query = "SELECT * FROM " + HOMEWORKS_COLLECTION + " " + whereClause.toString();
				}
				else
				{
					query = "SELECT * FROM " + HOMEWORKS_COLLECTION;
				}

				//Execute query
				List<HomeworkBean> allHomeworks = executeHomeworkQuery(oracleClient, query);
				if(!allHomeworks.isEmpty())
				{
					ret.addAll(allHomeworks);
				}
				else
				{
					LOG.debug("Cannot retrieve HW.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve Homework Events", e);
		}
		return ret;
	}

	public List<HomeworkBean> getAllHomeworkForClass(String classId)
	{
		ArrayList<HomeworkBean> ret = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + HOMEWORKS_COLLECTION + " WHERE Class_Id='" + classId +"'";

				//Execute query
				ArrayList<HomeworkBean> allLinks = executeHomeworkQuery(oracleClient, query);
				if(!allLinks.isEmpty())
				{
					ret = allLinks;
				}
				else
				{
					LOG.debug("No Links Exists, cannot retrieve event.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to find existing homeworks", e);
		}
		return ret;	}

	//Retrieves List<SubmissionBean> allowing user info on if each HW task is submitted or not
	public List<SubmissionBean> getAllHomeworkSubmissionsForChild(String childEmail)
	{
		List<SubmissionBean> ret = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + HOMEWORK_SUBMISSIONS_COLLECTION + " WHERE Child_Email='"+childEmail+"'";

				//Execute query
				List<SubmissionBean> allSubmissions = executeHomeworkSubmissionQuery(oracleClient, query);
				if(!allSubmissions.isEmpty())
				{
					ret.addAll(allSubmissions);
				}
				else
				{
					LOG.debug("Cannot retrieve HW.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve Homework Events", e);
		}
		return ret;
	}

	private StringBuilder createWhereClause(List<SubmissionBean> allHomeworkSubmissionsForChild)
	{
		StringBuilder whereClause = null;
		if(!allHomeworkSubmissionsForChild.isEmpty())
		{
			if(allHomeworkSubmissionsForChild.size() == 1)
			{
				whereClause = new StringBuilder("WHERE Event_Id='").append(allHomeworkSubmissionsForChild.get(0).getEventId()).append("'");
			}
			else
			{
				whereClause = multiValWhereClause(allHomeworkSubmissionsForChild, whereClause);
			}
		}
		return whereClause;
	}

	private StringBuilder multiValWhereClause(List<SubmissionBean> allHomeworkSubmissionsForChild, StringBuilder whereClause)
	{
		for (int i = 0; i < allHomeworkSubmissionsForChild.size(); i++)
		{
			if(whereClause == null)
			{
				whereClause = new StringBuilder("WHERE Event_Id='").append(allHomeworkSubmissionsForChild.get(i).getEventId());
			}
			else
			{
				if(i == allHomeworkSubmissionsForChild.size() - 1)
				{
					whereClause.append(allHomeworkSubmissionsForChild.get(i).getEventId()).append("'");
				}
				else
				{
					whereClause.append(allHomeworkSubmissionsForChild.get(i).getEventId()).append("' OR Event_Id='");
				}
			}
		}
		return whereClause;
	}

	private ArrayList<HomeworkBean> executeHomeworkQuery(Connection oracleClient, String query) throws SQLException
	{
		//Executes SQL Query, any Events found will populate the ArrayList.
		ArrayList<HomeworkBean> allEvents = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				HomeworkBean bean = new HomeworkBean(resultSet);
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

	private ArrayList<SubmissionBean> executeHomeworkSubmissionQuery(Connection oracleClient, String query) throws SQLException
	{
		//Executes SQL Query, any Events found will populate the ArrayList.
		ArrayList<SubmissionBean> allEvents = new ArrayList<>();
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			ResultSet resultSet = preparedStatement.executeQuery(query);
			while (resultSet.next())
			{
				SubmissionBean bean = new SubmissionBean(resultSet);
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

	//Returns bean of Homework Task, using Homework Submission ID. Allows name retrieval for Calendar event deletion after HW submission
	public HomeworkBean getHomeworkTaskFromId(String id)
	{
		HomeworkBean beanToReturn = new HomeworkBean();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + HOMEWORKS_COLLECTION + " WHERE Event_Id='" + id +"'";

				//Execute query
				ArrayList<HomeworkBean> allBeans = executeHomeworkQuery(oracleClient, query);
				if(!allBeans.isEmpty())
				{
					beanToReturn = allBeans.get(0);
				}
				else
				{
					LOG.debug("No Links Exists, cannot retrieve event.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to find existing class links", e);
		}

		return beanToReturn;
	}

	//Retrieves List<HomeworkBean>
	public List<HomeworkBean> getAllHomeworkForTeacher(String teacherEmail)
	{
		//Get HomeworkSubmissions to find HW task ID

		//Now get Homework Tasks set
		List<HomeworkBean> ret = new ArrayList<>();
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if(oracleClient != null)
			{
				//Select all Query
				String query = "SELECT * FROM " + HOMEWORKS_COLLECTION + " WHERE Tutor_Assigned='"+teacherEmail+"'";

				//Execute query
				List<HomeworkBean> allHomeworks = executeHomeworkQuery(oracleClient, query);
				if(!allHomeworks.isEmpty())
				{
					ret.addAll(allHomeworks);
				}
				else
				{
					LOG.debug("Cannot retrieve HW.");
				}
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to retrieve Homework Events", e);
		}
		return ret;
	}



	public void deleteHomework(String homeworkId)
	{
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "DELETE FROM " + HOMEWORKS_COLLECTION
						+ " WHERE Event_Id='" + homeworkId + "'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to delete homework in oracle DB");
		}
	}

	public void deleteSubmission(String submissionId, String childEmail)
	{
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "DELETE FROM " + HOMEWORK_SUBMISSIONS_COLLECTION
						+ " WHERE Event_Id='" + submissionId + "' AND Child_Email='"+childEmail+"'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to delete homework in oracle DB");
		}
	}

	public void deleteSubmissionFile(String submissionId)
	{
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "DELETE FROM " + HOMEWORK_FILES_COLLECTION
						+ " WHERE Submission_Id='" + submissionId + "'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to delete homework in oracle DB");
		}
	}

	//Used for teachers, click on a task to see submissions, table view of childrens submissions
	public List<SubmissionBean> getAllSubmissionsForHomeworkTask(String homeworkId)
	{
		List<SubmissionBean> allSubmissions = new ArrayList<>();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "SELECT * FROM " + HOMEWORK_SUBMISSIONS_COLLECTION + " WHERE Event_Id='" + homeworkId +"'";
				ArrayList<SubmissionBean> submissionBeans = executeHomeworkSubmissionQuery(oracleClient, query);
				allSubmissions.addAll(submissionBeans);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to update class information");
		}
		return allSubmissions;
	}

	public void downloadHomework(String submissionId, PrintWriter out, HttpServletResponse response)
	{
		//Search homework_files DB for submission ID, return data here
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "SELECT * FROM " + HOMEWORK_FILES_COLLECTION
						+ " WHERE Submission_Id='" + submissionId + "'";
				executeFileQuery(oracleClient, query, out, response);

			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to delete homework in oracle DB");
		}
	}

	private void executeFileQuery(Connection oracleClient, String query, PrintWriter out, HttpServletResponse response) throws SQLException
	{
		//Executes SQL Query, any Children found will populate the ArrayList.
		try (PreparedStatement preparedStatement = oracleClient.prepareStatement(query))
		{
			String filename = null;
			ResultSet resultSet = preparedStatement.executeQuery(query);
			InputStream is = null;
			while (resultSet.next())
			{
				//Get data
				filename = resultSet.getString("Filename");

				is = resultSet.getBinaryStream("File_Data");
			}

			if(filename != null && is != null)
			{
				response.setHeader("Content-Disposition", "attachment; filename=\""
						+ filename + "\"");
				readFileForDownload(out, is);
			}
		}
		catch(Exception e)
		{
			LOG.error("Query failure, using query: " + query, e);
		}
		oracleClient.close();
	}

	private void readFileForDownload(PrintWriter out, InputStream is) throws IOException
	{
		try
		{
			int i;
			while((i = is.read()) != -1)
			{
				out.write(i);
			}

		}
		catch(IOException e)
		{
			LOG.error("Error retrieving File from database", e);
		}
		finally
		{
			is.close();
			out.close();
		}
	}

	//Updates DB value of HW submission with grade value (Traffic lights green/amber/red)
	public void gradeHomeworkSubmission(String submissionId, String childEmail, String grade)
	{
		setOracleDriver();
		try
		{
			AbstractOracleConnections conn = new AbstractOracleConnections();
			Connection oracleClient = conn.getOracleClient();
			if (oracleClient != null)
			{
				String query = "UPDATE " + HOMEWORK_SUBMISSIONS_COLLECTION + " SET Grade ='" + grade
						+"' WHERE Submission_Id='"+ submissionId +"' AND Child_Email='"+childEmail+"'";
				executeUpdateQuery(oracleClient, query);
			}
			else
			{
				LOG.error("connection failure");
			}
		}
		catch(Exception e)
		{
			LOG.error("Unable to update homework submission grade");
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
}
