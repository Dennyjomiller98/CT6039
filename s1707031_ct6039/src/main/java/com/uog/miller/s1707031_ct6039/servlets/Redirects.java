package com.uog.miller.s1707031_ct6039.servlets;

import com.uog.miller.s1707031_ct6039.oracle.YearConnections;
import java.io.IOException;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Redirect operations (Mainly Navbar uses).
 */
@WebServlet(name = "Redirects")
public class Redirects extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(Redirects.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		//When user clicks on an operation to redirect to new page, GET request called
		String location = request.getParameter("location");
		if(location != null)
		{
			//Select redirect based on location
			LOG.debug("Found location: " + location + ", attempting to redirect.");
			try
			{
				removeAlerts(request);
				String redirect = switchFindLocation(location, request);
				response.sendRedirect(request.getContextPath() + redirect);
			}
			catch(IOException e)
			{
				LOG.error("Unable to redirect using location:" + location);
			}
		}
		else
		{
			//Error, redirect to error page.
			try
			{
				LOG.error("No location specified, returning to homepage");
				response.sendRedirect(request.getContextPath() + "/index.jsp");
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}

		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		//Otherwise (For example) you log in, browse pages, "Logged in Successfully" will retain. This way the alert is used for the relevant pages.
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
	}

	private String switchFindLocation(String location, HttpServletRequest request)
	{
		//Switch case uses request param to redirect user to correct page. If no location is specified, return to index homepage
		//(A lot cleaner than multiple servlet mappings for every redirect)
		String ret;
		switch (location)
		{
			case "child-login":
				ret = "/jsp/users/child/childlogin.jsp";
				break;
			case "child-register":
				addSessionAttributesForYear(request);
				ret = "/jsp/users/child/childregistration.jsp";
				break;
			case "child-profile":
				addSessionAttributesForYear(request);
				ret = "/jsp/users/child/childprofile.jsp";
				break;

			case "parent-login":
				ret = "/jsp/users/parent/parentlogin.jsp";
				break;
			case "parent-register":
				ret = "/jsp/users/parent/parentregistration.jsp";
				break;
			case "parent-profile":
				ret = "/jsp/users/parent/parentprofile.jsp";
				break;

			case "teacher-login":
				ret = "/jsp/users/teacher/teacherlogin.jsp";
				break;
			case "teacher-register":
				addSessionAttributesForYear(request);
				ret = "/jsp/users/teacher/teacherregistration.jsp";
				break;
			case "teacher-profile":
				addSessionAttributesForYear(request);
				ret = "/jsp/users/teacher/teacherprofile.jsp";
				break;

			case "calendar":
				ret = "/jsp/actions/calendar/viewcalendar.jsp";
				break;

			case "lessons":
				ret = "/jsp/actions/lessons/join.jsp";
				break;

			case "progress-request":
				ret = "/jsp/actions/progress/requestprogress.jsp";
				break;
			case "progress-submit":
				ret = "/jsp/actions/progress/submitprogress.jsp";
				break;
			case "progress-view":
				ret = "/jsp/actions/progress/viewprogress.jsp";
				break;

			case "homework-view":
				ret = "/jsp/actions/homework/viewhomework.jsp";
				break;

			case "login":
				ret = "/jsp/users/loginindex.jsp";
				break;
			case "register":
				ret = "/jsp/users/registerindex.jsp";
				break;

			case "home":
			default:
				ret = "/index.jsp";
				break;
		}
		return ret;
	}

	//Allows Registration forms/etc to populate Year select dropdown
	private void addSessionAttributesForYear(HttpServletRequest request)
	{
		Map<String, String> allYears;
		YearConnections yearConnections = new YearConnections();
		allYears = yearConnections.getAllClassYears();
		if(allYears != null)
		{
			request.getSession(true).setAttribute("allYears", allYears);
		}
	}
}
