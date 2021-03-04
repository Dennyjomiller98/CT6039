package com.uog.miller.s1707031_ct6039.servlets.calendar;

import com.uog.miller.s1707031_ct6039.oracle.CalendarConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "DeleteCalendarEvent")
public class DeleteCalendarEvent extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(DeleteCalendarEvent.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		//Delete event
		String eventId = (String) request.getSession(true).getAttribute("eventId");
		if(eventId != null)
		{
			CalendarConnections connections = new CalendarConnections();
			connections.deleteEvent(eventId);
			removeAlerts(request);
			request.getSession(true).setAttribute("formSuccess", "Event Deleted.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/calendar/viewcalendar.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to Calendar page after event update failure.",e);
			}
		}
		else
		{
			//Can't find event without ID
			LOG.error("Unable to find an event without an ID");
			removeAlerts(request);
			request.getSession(true).setAttribute("formErrors", "Could not find event to delete.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/calendar/viewcalendar.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to Calendar page after event update failure.",e);
			}
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
		request.getSession(true).removeAttribute("eventId");
		request.getSession(true).removeAttribute("eventName");
		request.getSession(true).removeAttribute("eventUser");
		request.getSession(true).removeAttribute("eventDate");
		request.getSession(true).removeAttribute("eventUpdateDate");
		request.getSession(true).removeAttribute("newlyAddedEvent");
	}
}