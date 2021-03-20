package com.uog.miller.s1707031_ct6039.servlets.childclass;

import com.uog.miller.s1707031_ct6039.beans.ClassBean;
import com.uog.miller.s1707031_ct6039.beans.HomeworkBean;
import com.uog.miller.s1707031_ct6039.beans.SubmissionBean;
import com.uog.miller.s1707031_ct6039.oracle.ClassConnections;
import com.uog.miller.s1707031_ct6039.oracle.HomeworkConnections;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "DeleteClass")
public class DeleteClass extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(DeleteClass.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting GET class delete");
		String classId = request.getParameter("classId");
		if(classId != null)
		{
			ClassConnections connections = new ClassConnections();
			//Remove class DB
			connections.deleteClass(classId);
			//Also remove classLinks, as children assigned to class are no longer needed
			connections.deleteClassLinks(classId);

			//Must delete HW information when class is deleted
			HomeworkConnections homeworkConnections = new HomeworkConnections();
			List<HomeworkBean> allHomeworkForClass = homeworkConnections.getAllHomeworkForClass(classId);
			for (HomeworkBean homeworkForClass : allHomeworkForClass)
			{
				//Get submissions
				List<SubmissionBean> allSubmissionsForHomeworkTask = homeworkConnections.getAllSubmissionsForHomeworkTask(homeworkForClass.getEventId());
				for (SubmissionBean submissionBean : allSubmissionsForHomeworkTask)
				{
					//Delete submission File
					if(submissionBean.getSubmissionId() != null)
					{
						homeworkConnections.deleteSubmissionFile(submissionBean.getSubmissionId());
					}

					//Delete Submission
					homeworkConnections.deleteSubmission(submissionBean.getEventId(), submissionBean.getEmail());
				}
				//Then delete Homework
				homeworkConnections.deleteHomework(homeworkForClass.getEventId());
			}

			removeAlerts(request);
			//Repopulate all Classes
			request.getSession(true).removeAttribute("allClasses");
			addSessionAttributesForClass(request);
			request.getSession(true).setAttribute("formSuccess", "ClassDeleted.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/class/viewclass.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to Class page after db update failure.",e);
			}
		}
		else
		{
			//Can't find event without ID
			LOG.error("Unable to find a class without an ID");
			removeAlerts(request);
			request.getSession(true).setAttribute("formErrors", "Could not find Class to delete.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/class/editclass.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to Class edit page.",e);
			}
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
	}

	private void addSessionAttributesForClass(HttpServletRequest request)
	{
		String email = (String) request.getSession(true).getAttribute("email");
		if(email != null)
		{
			ClassConnections connections = new ClassConnections();
			List<ClassBean> classFromTeacherEmail = connections.getClassFromTeacherEmail(email);
			request.getSession(true).setAttribute("allClasses", classFromTeacherEmail);
		}
	}
}