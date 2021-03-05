package com.uog.miller.s1707031_ct6039.servlets.users.teacher;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "TeacherDelete")
public class TeacherDelete extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(TeacherDelete.class);
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Deleting Teacher");
	}
}