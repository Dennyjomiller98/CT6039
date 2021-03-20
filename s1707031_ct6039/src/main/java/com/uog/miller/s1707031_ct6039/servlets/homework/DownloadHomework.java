package com.uog.miller.s1707031_ct6039.servlets.homework;

import com.uog.miller.s1707031_ct6039.oracle.HomeworkConnections;
import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "DownloadHomework")
public class DownloadHomework extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(DownloadHomework.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to Download Homework for Class");
		HomeworkConnections homeworkConnections = new HomeworkConnections();
		String submissionId = request.getParameter("submissionId");
		if (submissionId != null)
		{
			try
			{
				PrintWriter out = response.getWriter();
				response.setContentType("APPLICATION/OCTET-STREAM");

				homeworkConnections.downloadHomework(submissionId, out, response);
			}
			catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}