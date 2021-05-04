package com.uog.miller.s1707031_ct6039.beans;

import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public class SubmissionBean
{
	static final Logger LOG = Logger.getLogger(SubmissionBean.class);
	private String fEventId;
	private String fEmail;
	private String fSubmissionDate;
	private String fSubmissionId;
	private String fGrade;
	private String fFeedbackComment;

	public SubmissionBean()
	{
		//Empty No-Args
	}

	public SubmissionBean(ResultSet resultSet)
	{
		try
		{
			fEventId = resultSet.getString("Event_Id");
			fEmail = resultSet.getString("Child_Email");
			fSubmissionDate = resultSet.getString("Date_Submitted");
			fGrade = resultSet.getString("Grade");
			fSubmissionId = resultSet.getString("Submission_Id");
			fFeedbackComment = resultSet.getString("Feedback");
		}
		catch (SQLException e)
		{
			LOG.error("Unable to create Bean from ResultSet", e);
		}
	}

	public String getEventId()
	{
		return fEventId;
	}
	public void setEventId(String eventId)
	{
		fEventId = eventId;
	}

	public String getEmail()
	{
		return fEmail;
	}
	public void setEmail(String email)
	{
		fEmail = email;
	}

	public String getSubmissionDate()
	{
		return fSubmissionDate;
	}
	public void setSubmissionDate(String submissionDate)
	{
		fSubmissionDate = submissionDate;
	}

	public String getGrade()
	{
		return fGrade;
	}
	public void setGrade(String grade)
	{
		fGrade = grade;
	}

	public String getSubmissionId()
	{
		return fSubmissionId;
	}
	public void setSubmissionId(String subId)
	{
		fSubmissionId = subId;
	}

	public String getFeedback()
	{
		return fFeedbackComment;
	}
	public void setFeedback(String feedbackComment)
	{
		fFeedbackComment = feedbackComment;
	}
}