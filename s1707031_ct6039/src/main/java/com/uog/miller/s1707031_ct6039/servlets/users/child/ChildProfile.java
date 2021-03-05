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
 *	Action Servlet for Child Profile operations (Update Profile).
 */
@WebServlet(name = "ChildProfile")
public class ChildProfile extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ChildProfile.class);

	//Child update Profile
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to update account");
		String email = (String) request.getSession(true).getAttribute("email");
		String firstname = request.getParameter("firstname");
		String surname = request.getParameter("surname");
		String dob = request.getParameter("dob");
		String address = request.getParameter("address-value");
		String pword = request.getParameter("pword");
		String newPword = request.getParameter("newPword");
		String pwordConfirm = request.getParameter("pwordConfirm");

		String calEmail = request.getParameter("calendarEmail");
		String profEmail = request.getParameter("profileEmail");
		String hwEmail = request.getParameter("homeworkEmail");

		boolean calendarEmail = validateEmailSettings(calEmail);
		boolean profileEmail = validateEmailSettings(profEmail);
		boolean homeworkEmail = validateEmailSettings(hwEmail);

		if(email != null)
		{
			//Create bean, then connect to DB
			ChildConnections connections = new ChildConnections();
			//Validate user
			ChildBean bean = connections.validateCredentials(email, pword);
			if(bean != null)
			{
				validatePwords(pword, newPword, pwordConfirm, bean);

				//Populate bean with form params
				bean.setFirstname(firstname);
				bean.setSurname(surname);
				bean.setDOB(dob);
				bean.setAddress(address);
				bean.setEmailForHomework(homeworkEmail);
				bean.setEmailForProfile(profileEmail);
				bean.setEmailForCalendar(calendarEmail);

				updateAccount(request, response, connections, bean);
			}
			else
			{
				//Can't find user to update
				LOG.error("Unable to find user using email: " + email);
				request.getSession(true).removeAttribute("formSuccess");
				request.getSession(true).setAttribute("formErrors", "Could not update account details.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/users/child/childprofile.jsp");
				}
				catch (IOException e)
				{
					LOG.error("Unable to redirect back to after user update failure.",e);
				}
			}
		}
		else
		{
			//Can't find user to update
			LOG.error("Unable to find user without email");
			request.getSession(true).removeAttribute("formSuccess");
			request.getSession(true).setAttribute("formErrors", "Could not update account details.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/users/child/childprofile.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to after user update failure.",e);
			}
		}
	}

	private boolean validateEmailSettings(String checkBoxVal)
	{
		boolean shouldEmail;
		if (checkBoxVal == null)
		{
			shouldEmail = false;
		}
		else
		{
			shouldEmail = checkBoxVal.equals("on");
		}
		return shouldEmail;
	}

	private void validatePwords(String pword, String newPword, String pwordConfirm, ChildBean bean)
	{
		//Validate New pword if exist
		if(newPword != null && (!newPword.equals("") && !pwordConfirm.equals("")) && newPword.equals(pwordConfirm))
		{
			bean.setPword(newPword);
		}
		else if((newPword == null && pwordConfirm == null))
		{
			bean.setPword(pword);
		}
		else
		{
			bean.setPword(pword);
		}
	}

	private void updateAccount(HttpServletRequest request, HttpServletResponse response, ChildConnections connections, ChildBean bean)
	{
		//attempt DB update
		connections.updateAccount(bean);

		ChildBean childBean = connections.validateCredentials(bean.getEmail(), bean.getPword());
		updateSession(childBean, request);
		//Redirect
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).setAttribute("formSuccess", "Account details updated.");
		try
		{
			response.sendRedirect(request.getContextPath() + "/jsp/users/child/childprofile.jsp");
		}
		catch (IOException e)
		{
			LOG.error("Unable to redirect back to after user update failure.",e);
		}
	}

	private void updateSession(ChildBean childBean, HttpServletRequest request)
	{
		//Populate session for Child
		request.getSession(true).setAttribute("firstname", childBean.getFirstname());
		request.getSession(true).setAttribute("surname", childBean.getSurname());
		request.getSession(true).setAttribute("email", childBean.getEmail());
		request.getSession(true).setAttribute("dob", childBean.getDOB());
		request.getSession(true).setAttribute("address", childBean.getAddress());
		request.getSession(true).setAttribute("year", childBean.getYear());
		request.getSession(true).setAttribute("pword", childBean.getPword());
		request.getSession(true).setAttribute("homeworkEmail", childBean.getEmailForHomework());
		request.getSession(true).setAttribute("calendarEmail", childBean.getEmailForCalendar());
		request.getSession(true).setAttribute("profileEmail", childBean.getEmailForProfile());
		//Custom Child session login attribute
		request.getSession(true).setAttribute("isChild", "true");
	}
}
