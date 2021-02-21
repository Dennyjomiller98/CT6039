package com.uog.miller.s1707031_ct6039.servlets.users.parent;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Parent Profile operations, such as Edit/Delete.
 */
@WebServlet(name = "ParentProfile")
public class ParentProfile extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ParentProfile.class);

	//Parent Profile
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{

	}
}