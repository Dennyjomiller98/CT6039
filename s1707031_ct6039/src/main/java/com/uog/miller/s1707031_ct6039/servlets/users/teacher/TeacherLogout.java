package com.uog.miller.s1707031_ct6039.servlets.users.teacher;

import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Teacher Logout operations.
 */
@WebServlet(name = "TeacherLogout")
public class TeacherLogout extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(TeacherLogout.class);

	//Teacher Logout
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
		request.getSession(true).removeAttribute("title");
		//Custom Child session login attribute
		request.getSession(true).removeAttribute("isTeacher");

		request.getSession(true).removeAttribute("formErrors");

		request.getSession(true).removeAttribute("allYears");
		request.getSession(true).removeAttribute("allChildren");
		request.getSession(true).removeAttribute("allClasses");

		request.getSession(true).removeAttribute("allHomeworks");
		request.getSession(true).removeAttribute("allHomeworksTeacher");
		request.getSession(true).removeAttribute("allSubmissions");
	}
}