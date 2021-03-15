package com.uog.miller.s1707031_ct6039.servlets.users.child;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Child Logout operations.
 */
@WebServlet(name = "ChildLogout")
public class ChildLogout extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ChildLogout.class);

	//Child Logout, remove session attributes
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		removeSessionAttributes(request);
		try
		{
			request.getSession(true).setAttribute("formSuccess", "Successfully logged out.");
			response.sendRedirect(request.getContextPath() + "/index.jsp");
		} catch (IOException e) {
			LOG.error("Failure to redirect.", e);
		}
	}

	private void removeSessionAttributes(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("firstname");
		request.getSession(true).removeAttribute("surname");
		request.getSession(true).removeAttribute("email");
		request.getSession(true).removeAttribute("dob");
		request.getSession(true).removeAttribute("address");
		request.getSession(true).removeAttribute("year");
		request.getSession(true).removeAttribute("pword");
		request.getSession(true).removeAttribute("homeworkEmail");
		request.getSession(true).removeAttribute("calendarEmail");
		request.getSession(true).removeAttribute("profileEmail");
		//Custom Child session login attribute
		request.getSession(true).removeAttribute("isChild");

		request.getSession(true).removeAttribute("formErrors");

		request.getSession(true).removeAttribute("allYears");
		request.getSession(true).removeAttribute("allChildren");

		request.getSession(true).removeAttribute("allHomeworks");
		request.getSession(true).removeAttribute("allSubmissions");
	}
}