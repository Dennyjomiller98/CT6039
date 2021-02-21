package com.uog.miller.s1707031_ct6039.servlets.users.teacher;

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

	//Teacher Registration
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{

	}
}