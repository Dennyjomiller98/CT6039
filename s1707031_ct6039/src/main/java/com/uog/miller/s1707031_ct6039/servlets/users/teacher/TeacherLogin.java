package com.uog.miller.s1707031_ct6039.servlets.users.teacher;

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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{

	}
}