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

@WebServlet(name = "GradeHomework")
public class GradeHomework extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(GradeHomework.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		String email = (String) request.getSession(true).getAttribute("email");
		if(email != null)
		{
			//Attempt homework submission grading
			LOG.debug("Attempting homework submission grading");
			String grade = request.getParameter("grade");
			String childEmail = request.getParameter("childEmail");
			String submissionId = request.getParameter("submissionId");
			String homeworkId = request.getParameter("homeworkId");
			String feedback = request.getParameter("feedback");
			HomeworkConnections connections = new HomeworkConnections();
			connections.gradeHomeworkSubmission(submissionId, childEmail, grade, feedback);

			//Re-populate submissions and redirect
			removeAlerts(request);
			List<SubmissionBean> allSubmissions = connections.getAllSubmissionsForHomeworkTask(homeworkId);
			request.getSession(true).setAttribute("retrievedSubmissions", allSubmissions);
			request.getSession(true).setAttribute("formSuccess", "Grade added Successfully.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/viewhomework.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to homework after submission grade.",e);
			}
		}
		else
		{
			//Error
			LOG.error("Unable to retrieve homework without logged in user.");
			removeAlerts(request);
			request.getSession(true).setAttribute("formErrors", "Unable to Mark Homework Submission");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/viewhomework.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to homework after submission grade.",e);
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
