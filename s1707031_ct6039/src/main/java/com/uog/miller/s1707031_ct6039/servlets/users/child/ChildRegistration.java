package com.uog.miller.s1707031_ct6039.servlets.users.child;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Child Registration operations.
 */
@WebServlet(name = "ChildRegistration")
public class ChildRegistration extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ChildRegistration.class);

	//Child Registration
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{

	}
}
