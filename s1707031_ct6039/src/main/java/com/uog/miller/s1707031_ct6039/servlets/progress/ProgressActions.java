package com.uog.miller.s1707031_ct6039.servlets.progress;

import com.uog.miller.s1707031_ct6039.beans.HomeworkBean;
import com.uog.miller.s1707031_ct6039.beans.ProgressBean;
import com.uog.miller.s1707031_ct6039.beans.SubmissionBean;
import com.uog.miller.s1707031_ct6039.oracle.HomeworkConnections;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "ProgressActions")
public class ProgressActions extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ProgressActions.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to retrieve progress of child for parents view");
		//Retrieve Progress info on child
		String childEmail = request.getParameter("childEmail");
		if(childEmail != null)
		{
			ProgressBean progressBean = new ProgressBean();
			progressBean.setChild(childEmail);
			HomeworkConnections connections = new HomeworkConnections();
			List<SubmissionBean> allHomework = connections.getAllHomeworkSubmissionsForChild(childEmail);
			if(!allHomework.isEmpty())
			{
				int totalGreen = 0;
				int totalAmber = 0;
				int totalRed = 0;
				int totalHomeworks = 0;
				int onTimeHandins = 0;
				int overdueHandins = 0;
				int notSubmitted = 0;
				for (SubmissionBean bean : allHomework)
				{
					String submissionId = bean.getSubmissionId();

					String eventId = bean.getEventId();
					HomeworkBean homeworkBean = connections.getHomeworkTaskFromId(eventId);

					if(homeworkBean.getDueDate() != null)
					{
						String dateDue = homeworkBean.getDueDate();
						//Date of submission (if submitted)
						String dateSubmitted = bean.getSubmissionDate();
						checkSubmissionStatus(dateDue, dateSubmitted, onTimeHandins, overdueHandins, notSubmitted, progressBean, submissionId);
					}

					//Submission Information
					String grade = bean.getGrade();
					if(grade != null)
					{
						switch (grade)
						{
							case "green":
								totalGreen++;
								totalHomeworks++;
								break;
							case "amber":
								totalAmber++;
								totalHomeworks++;
								break;
							case "red":
								totalRed++;
								totalHomeworks++;
								break;
							default:
								LOG.error("Unknown or null value in DB for Grade. If null, no submission, else error in DB value");
						}
					}

					//Total hand-ins
					progressBean.setTotalHomeworks(totalHomeworks);
					progressBean.setTotalGreen(totalGreen);
					progressBean.setTotalAmber(totalAmber);
					progressBean.setTotalRed(totalRed);
				}
			}

			//Remove alerts and redirect
			//Set progressBean as sessionAttribute
			request.getSession(true).setAttribute("childProgress", progressBean);
			request.getSession(true).removeAttribute("formSuccess");
			request.getSession(true).removeAttribute("formErrors");
			try
			{
				request.getSession(true).setAttribute("formSuccess", "Progress retrieved Successfully");
				response.sendRedirect(request.getContextPath() + "/jsp/users/parent/viewmychildren.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to my children page after progress retrieval.",e);
			}
		}
		else
		{
			LOG.error("Cannot find progress information if email is null");
			//Remove alerts and redirect
			request.getSession(true).removeAttribute("formSuccess");
			request.getSession(true).removeAttribute("formErrors");
			request.getSession(true).removeAttribute("childProgress");
			try
			{
				request.getSession(true).setAttribute("formErrors", "Failed to retrieve progress");
				response.sendRedirect(request.getContextPath() + "/jsp/actions/users/parent/viewmychildren.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to my children page after progress retrieval failure.",e);
			}
		}
	}

	private void checkSubmissionStatus(String dateDue, String dateSubmitted, int onTimeHandins, int overdueHandins, int notSubmitted, ProgressBean progressBean, String submissionId)
	{
		String[] dueSplit = null;
		String[] submittedSplit = null;
		if(dateDue != null)
		{
			dueSplit = dateDue.split("-");
		}
		if(dateSubmitted != null)
		{
			submittedSplit = dateSubmitted.split("-");
		}

		if(submissionId == null)
		{
			//No submission, check due date.
			Date todaysDate = new Date();
			Date due = new Date(Integer.parseInt(dueSplit[0]), Integer.parseInt(dueSplit[1]), Integer.parseInt(dueSplit[2]));
			if(due.before(todaysDate))
			{
				notSubmitted++;
				progressBean.setNotSubmitted(notSubmitted);
			}
		}
		else
		{
			//Check submission date is before due date
			Date set = new Date(Integer.parseInt(submittedSplit[0]), Integer.parseInt(submittedSplit[1]), Integer.parseInt(submittedSplit[2]));
			Date due = new Date(Integer.parseInt(dueSplit[0]), Integer.parseInt(dueSplit[1]), Integer.parseInt(dueSplit[2]));
			if(due.before(set))
			{
				onTimeHandins++;
				progressBean.setOnTimeHandins(onTimeHandins);
			}
			else if(due.after(set))
			{
				overdueHandins++;
				progressBean.setOverdueHandins(overdueHandins);
			}
		}

	}
}