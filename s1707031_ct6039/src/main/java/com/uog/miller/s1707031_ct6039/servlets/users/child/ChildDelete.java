package com.uog.miller.s1707031_ct6039.servlets.users.child;

import com.uog.miller.s1707031_ct6039.oracle.ChildConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "ChildDelete")
public class ChildDelete extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ChildDelete.class);

	//Delete profile from DB
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		//Delete User
		LOG.debug("Deleting Child");
		String email = (String) request.getSession(true).getAttribute("email");
		if(email != null)
		{
			ChildConnections connections = new ChildConnections();
			connections.deleteAccount(email);
			removeSessionAttributes(request);
			request.getSession(true).setAttribute("formSuccess", "User Deleted.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/index.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to after user delete failure.",e);
			}
		}
		else
		{
			//Can't find user to delete
			LOG.error("Unable to find a user without an email");
			request.getSession(true).removeAttribute("formSuccess");
			request.getSession(true).setAttribute("formErrors", "Could not delete user.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/users/child/childprofile.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to after user delete failure.",e);
			}
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
		request.getSession(true).removeAttribute("formSuccess");
	}
}
