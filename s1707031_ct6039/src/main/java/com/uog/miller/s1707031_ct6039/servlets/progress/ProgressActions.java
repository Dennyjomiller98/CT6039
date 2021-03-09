package com.uog.miller.s1707031_ct6039.servlets.progress;

import com.uog.miller.s1707031_ct6039.oracle.ProgressConnections;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "ProgressActions")
public class ProgressActions extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ProgressActions.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to retrieve progress of child for parents view");
		//Retrieve Progress info on child
		String childEmail = request.getParameter("childEmail");
		if(childEmail != null)
		{
			ProgressConnections progressConnections = new ProgressConnections();
			progressConnections.getProgressForChild(childEmail);
		}
	}
}