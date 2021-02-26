package com.uog.miller.s1707031_ct6039.servlets.users.child;

import com.uog.miller.s1707031_ct6039.beans.ChildBean;
import com.uog.miller.s1707031_ct6039.oracle.ChildConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Child Login operations.
 */
@WebServlet(name = "ChildLogin")
public class ChildLogin extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ChildLogin.class);

	//Child Login
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		String email = request.getParameter("email");
		String pword = request.getParameter("pword");
		if(email == null && pword == null)
		{
			email = request.getParameter("childEmail");
			pword = request.getParameter("childPword");
		}

		//Ensure values supplied not null
		if(email != null && pword != null)
		{
			ChildConnections childConnections = new ChildConnections();
			ChildBean loggedInChildBean;
			//Check DB for matching user
			loggedInChildBean = childConnections.login(email, pword);
			if (loggedInChildBean != null)
			{
				//Matching user, log in and set session
				populateSession(loggedInChildBean, request);
				//Invalid Credentials
				LOG.debug("Login Success for user: " + email);
				//Redirect to Profile Account Page
				request.getSession(true).removeAttribute("formErrors");
				request.getSession(true).setAttribute("formSuccess", "Logged in successfully.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/users/child/childprofile.jsp");
				} catch (IOException e) {
					LOG.error("Failure to redirect.", e);
				}
			}
			else
			{
				//Invalid Credentials
				LOG.error("Login failed. Email and Password do not match.");
				//Redirect Back to Login Page
				request.getSession(true).setAttribute("formErrors", "Invalid credentials. Please try again.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/users/child/childlogin.jsp");
				} catch (IOException e) {
					LOG.error("Failure to redirect.", e);
				}
			}
		}
		else
		{
			LOG.error("Login failed. Both Email and password were not supplied");
			//Redirect Back to Login Page
			request.getSession(true).setAttribute("formErrors", "Please supply email and password");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/users/child/childlogin.jsp");
			} catch (IOException e) {
				LOG.error("Failure to redirect.", e);
			}
		}
	}

	private void populateSession(ChildBean loggedInChildBean, HttpServletRequest request)
	{
		//Populate session for Child
		request.getSession(true).setAttribute("firstname", loggedInChildBean.getFirstname());
		request.getSession(true).setAttribute("surname", loggedInChildBean.getSurname());
		request.getSession(true).setAttribute("email", loggedInChildBean.getEmail());
		request.getSession(true).setAttribute("dob", loggedInChildBean.getDOB());
		request.getSession(true).setAttribute("address", loggedInChildBean.getAddress());
		request.getSession(true).setAttribute("year", loggedInChildBean.getYear());
		request.getSession(true).setAttribute("pword", loggedInChildBean.getPword());
		request.getSession(true).setAttribute("homeworkEmail", loggedInChildBean.getEmailForHomework());
		request.getSession(true).setAttribute("calendarEmail", loggedInChildBean.getEmailForCalendar());
		request.getSession(true).setAttribute("profileEmail", loggedInChildBean.getEmailForProfile());
		//Custom Child session login attribute
		request.getSession(true).setAttribute("isChild", "true");
	}
}
