package com.uog.miller.s1707031_ct6039.servlets.calendar;

import com.google.gson.Gson;
import com.uog.miller.s1707031_ct6039.beans.CalendarItemBean;
import com.uog.miller.s1707031_ct6039.oracle.CalendarConnections;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "RetrieveCalendarEvent")
public class RetrieveCalendarEvent extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(RetrieveCalendarEvent.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		//Hit DB, retrieve all events for User
		String user = (String) request.getSession(true).getAttribute("email");
		CalendarConnections connections = new CalendarConnections();
		List<CalendarItemBean> allCalendarItemsForUser = connections.getAllCalendarItemsForUser(user);

		if (allCalendarItemsForUser != null)
		{
			PrintWriter out;
			try
			{
				Gson gson = new Gson();
				out = response.getWriter();
				out.print(gson.toJson(allCalendarItemsForUser));
				out.flush();
				out.close();
			}
			catch (IOException e)
			{
				LOG.error("Unable to retrieve events", e);
			}
		}
		else
		{
			LOG.error("Unable to retrieve events, could not find any.");
		}
	}

}
