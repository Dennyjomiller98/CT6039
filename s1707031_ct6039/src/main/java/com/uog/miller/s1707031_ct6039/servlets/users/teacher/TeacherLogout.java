package com.uog.miller.s1707031_ct6039.servlets.users.teacher;

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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{

	}
}