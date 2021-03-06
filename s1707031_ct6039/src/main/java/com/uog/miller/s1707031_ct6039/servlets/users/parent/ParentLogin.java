package com.uog.miller.s1707031_ct6039.servlets.users.parent;

import com.uog.miller.s1707031_ct6039.beans.LinkBean;
import com.uog.miller.s1707031_ct6039.beans.ParentBean;
import com.uog.miller.s1707031_ct6039.oracle.LinkedConnections;
import com.uog.miller.s1707031_ct6039.oracle.ParentConnections;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Parent Login operations, such as Edit/Delete.
 */
@WebServlet(name = "ParentLogin")
public class ParentLogin extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ParentLogin.class);

	//Parent Login
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		String email = request.getParameter("email");
		String pword = request.getParameter("pword");
		if(email == null && pword == null)
		{
			email = request.getParameter("parentEmail");
			pword = request.getParameter("parentPword");
		}

		//Ensure values supplied not null
		if(email != null && pword != null)
		{
			ParentConnections parentConnections = new ParentConnections();
			ParentBean loggedInParentBean;
			//Check DB for matching user
			loggedInParentBean = parentConnections.login(email, pword);
			if (loggedInParentBean != null)
			{
				loginSession(request, response, email, loggedInParentBean);
			}
			else
			{
				//Invalid Credentials
				LOG.error("Login failed. Email and Password do not match.");
				//Redirect Back to Login Page
				request.getSession(true).setAttribute("formErrors", "Invalid credentials. Please try again.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentlogin.jsp");
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
				response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentlogin.jsp");
			} catch (IOException e) {
				LOG.error("Failure to redirect.", e);
			}
		}
	}

	private void loginSession(HttpServletRequest request, HttpServletResponse response, String email, ParentBean loggedInParentBean)
	{
		//Matching user, log in and set session
		populateSession(loggedInParentBean, request);
		addSessionAttributesForLinks(request, loggedInParentBean.getEmail());
		LOG.debug("Login Success for user: " + email);
		//Redirect to Profile Account Page
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).setAttribute("formSuccess", "Logged in successfully.");
		try
		{
			response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentprofile.jsp");
		} catch (IOException e) {
			LOG.error("Failure to redirect.", e);
		}
	}

	private void populateSession(ParentBean loggedInParentBean, HttpServletRequest request)
	{
		//Populate session for Teacher
		request.getSession(true).setAttribute("firstname", loggedInParentBean.getFirstname());
		request.getSession(true).setAttribute("surname", loggedInParentBean.getSurname());
		request.getSession(true).setAttribute("email", loggedInParentBean.getEmail());
		request.getSession(true).setAttribute("dob", loggedInParentBean.getDOB());
		request.getSession(true).setAttribute("address", loggedInParentBean.getAddress());
		request.getSession(true).setAttribute("linkedChildId", loggedInParentBean.getLinkedChildIds());
		request.getSession(true).setAttribute("pword", loggedInParentBean.getPword());
		request.getSession(true).setAttribute("homeworkEmail", loggedInParentBean.getEmailForHomework());
		request.getSession(true).setAttribute("calendarEmail", loggedInParentBean.getEmailForCalendar());
		request.getSession(true).setAttribute("profileEmail", loggedInParentBean.getEmailForProfile());
		//Custom Teacher session login attribute
		request.getSession(true).setAttribute("isParent", "true");
	}

	//Allows Registration forms/etc to populate Year select dropdown
	private void addSessionAttributesForLinks(HttpServletRequest request, String parentEmail)
	{
		Map<String, String> allChildren;
		LinkedConnections connections = new LinkedConnections();
		List<LinkBean> allChildLinksForParent = connections.findAllChildLinksForParent(parentEmail);
		if(allChildLinksForParent != null)
		{
			request.getSession(true).setAttribute("allLinkBeans", allChildLinksForParent);
		}
		allChildren = connections.getAllChildren();
		if(allChildren != null)
		{
			request.getSession(true).setAttribute("allChildren", allChildren);
		}
	}
}