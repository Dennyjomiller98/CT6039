package com.uog.miller.s1707031_ct6039.servlets.homework;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "ViewSubmissions")
public class ViewSubmissions extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ViewSubmissions.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to ViewSubmissions for Class");
	}
}