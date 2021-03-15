package com.uog.miller.s1707031_ct6039.servlets.homework;

import com.uog.miller.s1707031_ct6039.beans.CalendarItemBean;
import com.uog.miller.s1707031_ct6039.beans.HomeworkBean;
import com.uog.miller.s1707031_ct6039.beans.SubmissionBean;
import com.uog.miller.s1707031_ct6039.oracle.CalendarConnections;
import com.uog.miller.s1707031_ct6039.oracle.HomeworkConnections;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import org.apache.log4j.Logger;

@WebServlet(name = "SubmitHomework")
public class SubmitHomework extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(SubmitHomework.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to Submit Homework (child upload homework) for Class");

		String email = request.getParameter("email");
		String homeworkId = request.getParameter("homeworkId");
		String dateSubmitted = request.getParameter("today");

		try
		{
			Part filePart = request.getPart("photo");
			String filename = filePart.getName();
			InputStream inputStream = filePart.getInputStream();
			HomeworkConnections connections = new HomeworkConnections();
			SubmissionBean bean = new SubmissionBean();
			bean.setEmail(email);
			bean.setEventId(homeworkId);
			bean.setSubmissionDate(dateSubmitted);
			String result = connections.submitHomeworkTask(bean, inputStream, filename);

			//Remove calendar event for child after submitting
			HomeworkBean homeworkBean = connections.getHomeworkTaskFromId(homeworkId);
			CalendarConnections calendarConnections = new CalendarConnections();
			List<CalendarItemBean> allCalendarItemsForUser = calendarConnections.getAllCalendarItemsForUser(email);
			for (CalendarItemBean calendarItemBean : allCalendarItemsForUser)
			{
				if(calendarItemBean.getEventName().equals("HW: "+homeworkBean.getName()))
				{
					calendarConnections.deleteEvent(calendarItemBean.getEventId());
				}
			}

			//Remove alerts and redirect after submitting to DB
			removeAlerts(request);
			redirectAfterSubmission(response, request, result);
		}
		catch (IOException | ServletException e)
		{
			LOG.error("Error retrieving uploaded file", e);
		}
	}

	private void redirectAfterSubmission(HttpServletResponse response, HttpServletRequest request, String result)
	{
		request.getSession(true).setAttribute("formSuccess", result);
		try
		{
			response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/uploadhomework.jsp");
		}
		catch (IOException e)
		{
			LOG.error(result,e);
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
	}
}