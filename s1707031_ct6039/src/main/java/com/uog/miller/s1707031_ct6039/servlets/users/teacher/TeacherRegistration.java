package com.uog.miller.s1707031_ct6039.servlets.users.teacher;

import com.uog.miller.s1707031_ct6039.beans.TeacherBean;
import com.uog.miller.s1707031_ct6039.oracle.TeacherConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Teacher Registration operations.
 */
@WebServlet(name = "TeacherRegistration")
public class TeacherRegistration extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(TeacherRegistration.class);

	//Teacher Registration form submit
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		String title = request.getParameter("title");
		String firstname = request.getParameter("firstname");
		String surname = request.getParameter("surname");
		String email = request.getParameter("email");
		String dob = request.getParameter("dob");
		String address = request.getParameter("address-value");
		String year = request.getParameter("year");
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
				response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherregistration.jsp");
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
					response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherregistration.jsp");
				} catch (IOException e) {
					LOG.error("Failure to redirect.", e);
				}
			}
			else
			{
				//Populate Bean for Registration
				TeacherBean bean = new TeacherBean();
				bean.setTitle(title);
				bean.setFirstname(firstname);
				bean.setSurname(surname);
				bean.setEmail(email.toLowerCase());
				bean.setDOB(dob);
				bean.setAddress(address);
				bean.setYear(year);
				bean.setPword(pword);
				//Get 'Default' account settings for new user
				bean.setEmailForHomework(true);
				bean.setEmailForCalender(true);
				bean.setEmailForProfile(true);
				attemptTeacherRegistration(request, response, bean);
			}
		}
	}

	private void attemptTeacherRegistration(HttpServletRequest request, HttpServletResponse response, TeacherBean bean)
	{
		LOG.debug("Account sane to be created, attempting to register to DB");
		//Use ChildBean to create an account and add to DB
		TeacherConnections teacherConnections = new TeacherConnections();
		boolean registerSuccess = teacherConnections.registerTeacher(bean);
		if(registerSuccess)
		{
			//Redirect happy path, remove errors (if any)
			request.getSession(true).removeAttribute("formErrors");
			request.getSession(true).setAttribute("formSuccess", "Account created successfully.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherlogin.jsp");
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
				response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherregistration.jsp");
			} catch (IOException e) {
				LOG.error("Failure to redirect.", e);
			}
		}
	}

	private boolean checkUserExists(String email)
	{
		//Check DB for account linked to email
		TeacherConnections teacherConnections = new TeacherConnections();
		return teacherConnections.checkUserExists(email);
	}
}