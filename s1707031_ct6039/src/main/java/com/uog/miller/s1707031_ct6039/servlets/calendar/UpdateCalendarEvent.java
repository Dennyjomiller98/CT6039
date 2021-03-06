package com.uog.miller.s1707031_ct6039.servlets.calendar;

import com.uog.miller.s1707031_ct6039.beans.CalendarItemBean;
import com.uog.miller.s1707031_ct6039.oracle.CalendarConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "UpdateCalendarEvent")
public class UpdateCalendarEvent extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(UpdateCalendarEvent.class);
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to update calendar event");
		String newEventId = request.getParameter("eventUpdateId");
		String newEventDate = request.getParameter("eventUpdateDate");
		String newEventName = request.getParameter("eventUpdateName");
		String newEventUpdateDate = request.getParameter("newEventUpdateDate").replace("+", " ");
		if(newEventId != null && newEventDate != null && newEventName != null)
		{
			//Attempt to update values, create bean for old values and new form values
			String eventId = (String) request.getSession(true).getAttribute("eventId");
			String eventName = (String) request.getSession(true).getAttribute("eventName");
			String eventUser = (String) request.getSession(true).getAttribute("eventUser");
			String eventDate = (String) request.getSession(true).getAttribute("eventDate");
			String eventUpdateDate = (String) request.getSession(true).getAttribute("eventUpdateDate");

			//Original Bean
			CalendarItemBean originalBean = new CalendarItemBean();
			originalBean.setEventId(eventId);
			originalBean.setEventName(eventName);
			originalBean.setUser(eventUser);
			originalBean.setEventDate(eventDate);
			originalBean.setDateForUpdate(eventUpdateDate);

			//Updated Bean
			CalendarItemBean updatedValuesBean = new CalendarItemBean();
			updatedValuesBean.setEventId(newEventId);
			updatedValuesBean.setEventName(newEventName);
			updatedValuesBean.setEventDate(newEventDate);
			updatedValuesBean.setDateForUpdate(newEventUpdateDate);

			//Connect to DB
			CalendarConnections connections = new CalendarConnections();
			CalendarItemBean calendarItemForUser = connections.getCalendarItemForUser(originalBean.getUser(), originalBean.getEventId());
			if (calendarItemForUser != null)
			{
				updateMonthForCalendar(updatedValuesBean);
				connections.updateEvent(originalBean, updatedValuesBean);
				removeAlerts(request);
				request.getSession(true).setAttribute("formSuccess", "Event updated successfully.");
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
				LOG.error("No event found in DB, cannot update");
				removeAlerts(request);
				request.getSession(true).setAttribute("formErrors", "Event could not be updated.");
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
	}

	private void updateMonthForCalendar(CalendarItemBean updatedValuesBean)
	{
		String eventDate = updatedValuesBean.getEventDate();
		String[] split = eventDate.split("-");
		String month = split[1];
		String newMonth;
		switch (month)
		{
			case "01":
				newMonth = "00";
				break;
			case "02":
				newMonth = "01";
				break;
			case "03":
				newMonth = "02";
				break;
			case "04":
				newMonth = "03";
				break;
			case "05":
				newMonth = "04";
				break;
			case "06":
				newMonth = "05";
				break;
			case "07":
				newMonth = "06";
				break;
			case "08":
				newMonth = "07";
				break;
			case "09":
				newMonth = "08";
				break;
			case "10":
				newMonth = "09";
				break;
			case "11":
				newMonth = "10";
				break;
			case "12":
				newMonth = "11";
				break;
			default:
				newMonth = month;
		}
		String newDate = split[0] + "-" + newMonth + "-" + split[2];
		updatedValuesBean.setEventDate(newDate);
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