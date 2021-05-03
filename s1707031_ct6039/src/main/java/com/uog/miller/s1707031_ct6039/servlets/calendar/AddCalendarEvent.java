package com.uog.miller.s1707031_ct6039.servlets.calendar;

import com.uog.miller.s1707031_ct6039.oracle.CalendarConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "AddCalendarEvent")
public class AddCalendarEvent extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(AddCalendarEvent.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		String currentUser = request.getParameter("currentUser");
		String eventDate = request.getParameter("eventDate");
		String eventName = request.getParameter("eventName");
		String dateForUpdate = request.getParameter("dateForUpdate");
		if(currentUser != null && eventDate != null && eventName != null && dateForUpdate != null)
		{
			try
			{
				//add Event to DB
				CalendarConnections connections = new CalendarConnections();
				String newEventId = connections.addCalendarItemForUser(currentUser, eventName, eventDate, dateForUpdate);
				//Add to response
				response.getWriter().write(newEventId);
				removeAlerts(request);
				LOG.debug("Successfully added new event");
				request.getSession(true).setAttribute("formSuccess", "Event added successfully.");
				request.getSession(true).setAttribute("newlyAddedEvent", newEventId);

				response.sendRedirect(request.getContextPath() + "/jsp/actions/calendar/viewcalendar.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to add new event to DB", e);
			}
		}
		else
		{
			//Redirect back to page.
			redirectToCalendarFail(request, response);
		}
	}

	private void redirectToCalendarFail(HttpServletRequest request, HttpServletResponse response)
	{
		try
		{
			removeAlerts(request);
			LOG.error("Unable to add event to calendar.");
			request.getSession(true).setAttribute("formErrors", "Unable to add event to calendar.");
			response.sendRedirect(request.getContextPath() + "/jsp/actions/calendar/viewcalendar.jsp");
		}
		catch (IOException e)
		{
			LOG.error("Unable to redirect to calendar after failure adding event", e);
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