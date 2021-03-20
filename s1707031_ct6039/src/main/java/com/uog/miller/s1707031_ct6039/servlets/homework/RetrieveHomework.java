package com.uog.miller.s1707031_ct6039.servlets.homework;

import com.uog.miller.s1707031_ct6039.beans.SubmissionBean;
import com.uog.miller.s1707031_ct6039.oracle.HomeworkConnections;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "RetrieveHomework")
public class RetrieveHomework extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(RetrieveHomework.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to retrieve Homework for Class");
		String email = (String) request.getSession(true).getAttribute("email");
		if(email != null)
		{
			removeAlerts(request);
			String homeworkId = request.getParameter("homeworkId");

			HomeworkConnections connections = new HomeworkConnections();
			List<SubmissionBean> allSubmissions = connections.getAllSubmissionsForHomeworkTask(homeworkId);

			request.getSession(true).setAttribute("retrievedSubmissions", allSubmissions);
			request.getSession(true).setAttribute("homeworkId", homeworkId);
			request.getSession(true).setAttribute("formSuccess", "Submission information retrieved successfully");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/viewhomework.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to homework after submission retrieval.",e);
			}
		}
		else
		{
			LOG.error("Unable to retrieve homework without logged in user.");
			removeAlerts(request);
			request.getSession(true).setAttribute("formErrors", "Unable to retrieve Submission information");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/viewhomework.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to homework after submission retrieval.",e);
			}
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
		request.getSession(true).removeAttribute("retrievedSubmissions");
	}
}