package com.uog.miller.s1707031_ct6039.servlets.calendar;

import com.uog.miller.s1707031_ct6039.beans.CalendarItemBean;
import com.uog.miller.s1707031_ct6039.oracle.CalendarConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "CalendarActions")
public class CalendarActions extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(CalendarActions.class);

	//Get Event from DB to show info
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		String eventID = request.getParameter("eventId");
		String user = (String) request.getSession(true).getAttribute("email");
		if(eventID != null)
		{
			//Select redirect based on location
			LOG.debug("Found eventID: " + eventID + ", attempting to retrieve info.");
			removeAlerts(request);
			removeFormAlerts(request);
			CalendarConnections connections = new CalendarConnections();
			CalendarItemBean bean = connections.getCalendarItemForUser(user, eventID);

			if(bean.getEventId() != null)
			{
				//Event found, return information.
				addEventSession(request, bean);
				redirectToCalendarSuccess(request, response, eventID);
			}
			else
			{
				//Event not found, alert user.
				redirectToCalendarFail(request, response);
			}
		}
		else
		{
			//Error, redirect.
			redirectToCalendarFail(request, response);
		}
	}

	private void addEventSession(HttpServletRequest request, CalendarItemBean bean)
	{
		request.getSession(true).setAttribute("eventId", bean.getEventId());
		request.getSession(true).setAttribute("eventName", bean.getEventName());
		request.getSession(true).setAttribute("eventUser", bean.getUser());
		request.getSession(true).setAttribute("eventDate", bean.getEventDate());
		request.getSession(true).setAttribute("eventUpdateDate", bean.getDateForUpdate());
	}

	private void redirectToCalendarFail(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			removeFormAlerts(request);
			removeAlerts(request);
			LOG.error("No eventID specified, returning to calendar.");
			request.getSession(true).setAttribute("formErrors", "Unable to retrieve information for the selected event.");
			response.sendRedirect(request.getContextPath() + "/jsp/actions/calendar/viewcalendar.jsp");
		}
		catch (IOException e)
		{
			LOG.error("Unable to redirect to calendar page", e);
		}
	}

	private void redirectToCalendarSuccess(HttpServletRequest request, HttpServletResponse response, String eventId)
	{
		try
		{
			removeFormAlerts(request);
			LOG.debug("Successfully found event using ID: " + eventId );
			request.getSession(true).setAttribute("formSuccess", "Retrieved information for the selected event.");
			response.sendRedirect(request.getContextPath() + "/jsp/actions/calendar/viewcalendar.jsp");
		}
		catch (IOException e)
		{
			LOG.error("Unable to redirect to calendar page", e);
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("eventId");
		request.getSession(true).removeAttribute("eventName");
		request.getSession(true).removeAttribute("eventUser");
		request.getSession(true).removeAttribute("eventDate");
		request.getSession(true).removeAttribute("eventUpdateDate");
	}

	private void removeFormAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
	}
}
