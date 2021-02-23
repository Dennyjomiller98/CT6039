package com.uog.miller.s1707031_ct6039.servlets.users.parent;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Parent Logout operations.
 */
@WebServlet(name = "ParentLogout")
public class ParentLogout extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ParentLogout.class);

	//Parent Logout
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
		request.getSession(true).removeAttribute("linkedChildId");
		request.getSession(true).removeAttribute("pword");
		request.getSession(true).removeAttribute("homeworkEmail");
		request.getSession(true).removeAttribute("calenderEmail");
		request.getSession(true).removeAttribute("profileEmail");
		//Custom Child session login attribute
		request.getSession(true).removeAttribute("isParent");

		request.getSession(true).removeAttribute("formErrors");

	}
}