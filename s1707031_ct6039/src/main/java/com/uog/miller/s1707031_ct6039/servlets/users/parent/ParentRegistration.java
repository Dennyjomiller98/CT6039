package com.uog.miller.s1707031_ct6039.servlets.users.parent;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Parent Registration operations.
 */
@WebServlet(name = "ParentRegistration")
public class ParentRegistration extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ParentRegistration.class);

	//Parent Register
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{

	}
}
