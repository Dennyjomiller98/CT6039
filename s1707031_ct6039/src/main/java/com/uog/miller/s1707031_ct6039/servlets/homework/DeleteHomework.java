package com.uog.miller.s1707031_ct6039.servlets.homework;

import com.uog.miller.s1707031_ct6039.beans.CalendarItemBean;
import com.uog.miller.s1707031_ct6039.beans.HomeworkBean;
import com.uog.miller.s1707031_ct6039.beans.SubmissionBean;
import com.uog.miller.s1707031_ct6039.oracle.CalendarConnections;
import com.uog.miller.s1707031_ct6039.oracle.HomeworkConnections;
import java.io.IOException;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "DeleteHomework")
public class DeleteHomework extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(DeleteHomework.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to Delete Homework for Class");
		String email = (String) request.getSession(true).getAttribute("email");
		String homeworkId = request.getParameter("homeworkId");

		if (email != null && homeworkId != null)
		{
			//Delete Homework, Homework Submissions for the homework being deleted, and any submission files with each submission
			//For each child, must delete calendar event too!
			HomeworkConnections connections = new HomeworkConnections();
			HomeworkBean homeworkTaskFromId = connections.getHomeworkTaskFromId(homeworkId);
			List<SubmissionBean> allSubmissionsForHomeworkTask = connections.getAllSubmissionsForHomeworkTask(homeworkId);
			for (SubmissionBean submissionBean : allSubmissionsForHomeworkTask)
			{
				removeChildHomeworks(connections, homeworkTaskFromId, submissionBean);
			}

			//Finally, delete the HW event now submissions/files are removed
			connections.deleteHomework(homeworkId);

			LOG.debug("End of homework deletion, redirecting");
			removeAlerts(request);
			repopulateHomeworks(request);
			request.getSession(true).setAttribute("formSuccess", "Homework Deleted Successfully.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/viewhomework.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unknown error",e);
			}
		}
		else
		{
			LOG.error("Cannot delete homework without given user and Homework ID to delete");
			removeAlerts(request);
			request.getSession(true).setAttribute("formError", "Could not delete Homework.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/viewhomework.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unknown error",e);
			}
		}
	}

	private void repopulateHomeworks(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("allHomeworksTeacher");

		String teacherEmail = (String) request.getSession(true).getAttribute("email");
		if(teacherEmail != null)
		{
			HomeworkConnections connections = new HomeworkConnections();
			List<HomeworkBean> allHomeworks = connections.getAllHomeworkForTeacher(teacherEmail);
			if(!allHomeworks.isEmpty())
			{
				request.getSession(true).setAttribute("allHomeworksTeacher", allHomeworks);
			}
		}
	}

	private void removeChildHomeworks(HomeworkConnections connections, HomeworkBean homeworkTaskFromId, SubmissionBean submissionBean)
	{
		//Child Email
		String childEmail = submissionBean.getEmail();
		//If child has submitted, delete file
		if(submissionBean.getSubmissionId() != null)
		{
			connections.deleteSubmissionFile(submissionBean.getSubmissionId());
		}

		//Then delete actual child submission regardless
		connections.deleteSubmission(submissionBean.getEventId(), childEmail);

		//Remove Calendar Event for each child
		CalendarConnections calendarConnections = new CalendarConnections();
		List<CalendarItemBean> allCalendarItemsForUser = calendarConnections.getAllCalendarItemsForUser(childEmail);
		for (CalendarItemBean calendarItemBean : allCalendarItemsForUser)
		{
			if(calendarItemBean.getEventName().equals("HW: "+ homeworkTaskFromId.getName()))
			{
				calendarConnections.deleteEvent(calendarItemBean.getEventId());
			}
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
	}
}