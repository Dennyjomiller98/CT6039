package com.uog.miller.s1707031_ct6039.servlets.users.parent;

import com.uog.miller.s1707031_ct6039.beans.ParentBean;
import com.uog.miller.s1707031_ct6039.oracle.LinkedConnections;
import com.uog.miller.s1707031_ct6039.oracle.ParentConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Parent Registration operations.
 */
@WebServlet(name = "ParentRegistration")
public class ParentRegistration extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ParentRegistration.class);

	//Parent Register form submit
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		String firstname = request.getParameter("firstname");
		String surname = request.getParameter("surname");
		String email = request.getParameter("email");
		String dob = request.getParameter("dob");
		String address = request.getParameter("address-value");
		String[] linkedChildIds = request.getParameterValues("childSelect[]");
		String pword = request.getParameter("pword");
		String pwordConfirm = request.getParameter("pwordConfirm");

		if (!pword.equals(pwordConfirm))
		{
			//Error, JS has validated these should match!
			LOG.error("Passwords should have matched!");
			//Redirect Back to Registration Page
			request.getSession(true).setAttribute("formErrors", "Passwords did not match despite passing checks. Please re-enter and try again");
			request.getSession(true).removeAttribute("formSuccess");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentregistration.jsp");
			} catch (IOException e) {
				LOG.error("Failure to redirect.", e);
			}
		}
		else
		{
			//Check user does not exist already
			boolean userExists = checkUserExists(email);

			if(userExists)
			{
				LOG.error("Registration of User: " + email + " failed, as account already exists");
				//Redirect Back to Registration Page
				request.getSession(true).setAttribute("formErrors", "There is already an account linked with:" + email);
				request.getSession(true).removeAttribute("formSuccess");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentregistration.jsp");
				} catch (IOException e) {
					LOG.error("Failure to redirect.", e);
				}
			}
			else
			{
				//Populate Bean for Registration
				ParentBean bean = new ParentBean();
				bean.setFirstname(firstname);
				bean.setSurname(surname);
				bean.setEmail(email.toLowerCase());
				bean.setDOB(dob);
				bean.setAddress(address);
				bean.setPword(pword);
				//Get 'Default' account settings for new user
				bean.setEmailForHomework(true);
				bean.setEmailForCalendar(true);
				bean.setEmailForProfile(true);
				if(linkedChildIds != null && linkedChildIds.length > 0)
				{
					attemptParentRegistration(bean, String.join(", ", linkedChildIds));
				}
				//Use ParentBean to create an account and add to DB
				ParentConnections parentConnections = new ParentConnections();
				boolean registerSuccess = parentConnections.registerParent(bean);
				if(registerSuccess)
				{
					//Redirect happy path, remove errors (if any)
					request.getSession(true).removeAttribute("formErrors");
					request.getSession(true).setAttribute("formSuccess", "Account created successfully.");
					try
					{
						response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentlogin.jsp");
					} catch (IOException e) {
						LOG.error("Failure to redirect.", e);
					}
				}
				else
				{
					LOG.error("Unknown error occurred whilst attempting to create new User: " + bean.getEmail());
					//Redirect Back to Registration Page
					request.getSession(true).setAttribute("formErrors", "Unknown Error. The account was not created.");
					request.getSession(true).removeAttribute("formSuccess");
					try
					{
						response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentregistration.jsp");
					} catch (IOException e) {
						LOG.error("Failure to redirect.", e);
					}
				}
			}
		}
	}

	private void attemptParentRegistration(ParentBean bean, String linkedChildIds)
	{
		LOG.debug("Account sane to be created, attempting to register to DB");
		//Create parent-child link
		LinkedConnections connections = new LinkedConnections();
		connections.linkChildren(bean.getEmail(), linkedChildIds);
		//Get links, add to Parent account
		String allLinkedIds = connections.getAllLinks(bean.getEmail());
		if(allLinkedIds != null)
		{
			bean.setLinkedChildIds(allLinkedIds);
		}
	}

	private boolean checkUserExists(String email)
	{
		//Check DB for account linked to email
		ParentConnections parentConnections = new ParentConnections();
		return parentConnections.checkUserExists(email);
	}
}
