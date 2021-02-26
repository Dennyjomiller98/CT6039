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
 *	Action Servlet for Teacher Login operations.
 */
@WebServlet(name = "TeacherLogin")
public class TeacherLogin extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(TeacherLogin.class);

	//Teacher Login
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		String email = request.getParameter("email");
		String pword = request.getParameter("pword");
		if(email == null && pword == null)
		{
			email = request.getParameter("teacherEmail");
			pword = request.getParameter("teacherPword");
		}

		//Ensure values supplied not null
		if(email != null && pword != null)
		{
			TeacherConnections teacherConnections = new TeacherConnections();
			TeacherBean loggedInTeacherBean;
			//Check DB for matching user
			loggedInTeacherBean = teacherConnections.login(email, pword);
			if (loggedInTeacherBean != null)
			{
				//Matching user, log in and set session
				populateSession(loggedInTeacherBean, request);
				//Invalid Credentials
				LOG.debug("Login Success for user: " + email);
				//Redirect to Profile Account Page
				request.getSession(true).removeAttribute("formErrors");
				request.getSession(true).setAttribute("formSuccess", "Logged in successfully.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherprofile.jsp");
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
					response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherlogin.jsp");
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
				response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherlogin.jsp");
			} catch (IOException e) {
				LOG.error("Failure to redirect.", e);
			}
		}
	}

	private void populateSession(TeacherBean loggedInTeacherBean, HttpServletRequest request)
	{
		//Populate session for Teacher
		request.getSession(true).setAttribute("firstname", loggedInTeacherBean.getFirstname());
		request.getSession(true).setAttribute("surname", loggedInTeacherBean.getSurname());
		request.getSession(true).setAttribute("email", loggedInTeacherBean.getEmail());
		request.getSession(true).setAttribute("dob", loggedInTeacherBean.getDOB());
		request.getSession(true).setAttribute("address", loggedInTeacherBean.getAddress());
		request.getSession(true).setAttribute("year", loggedInTeacherBean.getYear());
		request.getSession(true).setAttribute("pword", loggedInTeacherBean.getPword());
		request.getSession(true).setAttribute("homeworkEmail", loggedInTeacherBean.getEmailForHomework());
		request.getSession(true).setAttribute("calendarEmail", loggedInTeacherBean.getEmailForCalendar());
		request.getSession(true).setAttribute("profileEmail", loggedInTeacherBean.getEmailForProfile());
		request.getSession(true).setAttribute("title", loggedInTeacherBean.getTitle());
		//Custom Teacher session login attribute
		request.getSession(true).setAttribute("isTeacher", "true");
	}
}