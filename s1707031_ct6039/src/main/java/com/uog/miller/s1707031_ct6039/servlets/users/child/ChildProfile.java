package com.uog.miller.s1707031_ct6039.servlets.users.child;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Child Profile operations (Update/Delete Profile).
 */
@WebServlet(name = "ChildProfile")
public class ChildProfile extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ChildProfile.class);

	//Child ChildProfile
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{

	}
}
