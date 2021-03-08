package com.uog.miller.s1707031_ct6039.servlets.childclass;

import com.uog.miller.s1707031_ct6039.beans.ClassBean;
import com.uog.miller.s1707031_ct6039.beans.ClassLinkBean;
import com.uog.miller.s1707031_ct6039.oracle.ClassConnections;
import com.uog.miller.s1707031_ct6039.oracle.LinkedConnections;
import com.uog.miller.s1707031_ct6039.oracle.YearConnections;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "RetrieveClass")
public class RetrieveClass extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(RetrieveClass.class);

	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting class retrieval for popup");
		String email = (String) request.getSession(true).getAttribute("email");
		String classId = request.getParameter("classId");
		if(email != null)
		{
			ClassConnections connections = new ClassConnections();
			ClassBean classFromId = connections.getClassFromId(classId);
			if(classFromId != null)
			{
				removeAlerts(request);
				request.getSession(true).setAttribute("formSuccess", "Class retrieved successfully.");
				request.getSession(true).setAttribute("classId", classFromId.getEventId());
				request.getSession(true).setAttribute("classYear", classFromId.getYear());
				request.getSession(true).setAttribute("className", classFromId.getName());
				request.getSession(true).setAttribute("classTutor", classFromId.getTeacher());

				addSessionAttributesForLinks(request);
				addSessionAttributesForYear(request);
				addSessionAttributesForChildLinks(request, classId);

				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/actions/class/editclass.jsp");
				}
				catch (IOException e)
				{
					LOG.error("Unable to redirect back to Calendar page after event update failure.",e);
				}
			}
			else
			{
				removeAlerts(request);
				request.getSession(true).setAttribute("formErrors", "Unable to retrieve Class.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/actions/class/viewclass.jsp");
				}
				catch (IOException e)
				{
					LOG.error("Unable to redirect back to Class page after event update failure.",e);
				}
			}
		}
		else
		{
			LOG.error("Unable to retrieve class");
			removeAlerts(request);
			request.getSession(true).setAttribute("formSuccess", "Unable to retrieve Class");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/class/viewclass.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to Class page after event update failure.",e);
			}
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
	}

	//Allows Registration forms/etc to populate Year select dropdown
	private void addSessionAttributesForLinks(HttpServletRequest request)
	{
		Map<String, String> allChildren;
		LinkedConnections connections = new LinkedConnections();
		allChildren = connections.getAllChildren();
		if(allChildren != null)
		{
			request.getSession(true).setAttribute("allChildren", allChildren);
		}
	}

	//Allows Registration forms/etc to populate Year select dropdown
	private void addSessionAttributesForYear(HttpServletRequest request)
	{
		Map<String, String> allYears;
		YearConnections yearConnections = new YearConnections();
		allYears = yearConnections.getAllClassYears();
		if(allYears != null)
		{
			request.getSession(true).setAttribute("allYears", allYears);
		}
	}

	private void addSessionAttributesForChildLinks(HttpServletRequest request, String classId)
	{
		ClassConnections connections = new ClassConnections();
		List<ClassLinkBean> childrenFromClassId = connections.getChildrenFromClassId(classId);
		if(childrenFromClassId != null)
		{
			request.getSession(true).setAttribute("allLinkChildren", childrenFromClassId);
		}
	}
}
