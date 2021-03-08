package com.uog.miller.s1707031_ct6039.servlets.childclass;

import com.uog.miller.s1707031_ct6039.beans.ClassBean;
import com.uog.miller.s1707031_ct6039.beans.ClassLinkBean;
import com.uog.miller.s1707031_ct6039.oracle.ClassConnections;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "UpdateClass")
public class UpdateClass extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(UpdateClass.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting POST class update");
		String teacherUser = (String) request.getSession(true).getAttribute("email");
		String classId = (String) request.getSession(true).getAttribute("classId");
		if(teacherUser != null && classId != null)
		{
			String className = request.getParameter("className");
			String tutorYear = request.getParameter("tutor");
			String[] childrenInClass = request.getParameterValues("childSelect[]");

			ClassConnections connections = new ClassConnections();
			ClassBean beanToUpdate = new ClassBean();
			beanToUpdate.setEventId(classId);
			beanToUpdate.setName(className);
			beanToUpdate.setYear(tutorYear);
			beanToUpdate.setTeacher(teacherUser);
			connections.updateClass(beanToUpdate);

			//Remove old Children classLinks, and repopulate
			connections.deleteClassLinks(classId);
			List<ClassLinkBean> linksList = new ArrayList<>();
			for (String childEmail : childrenInClass)
			{
				if(!childEmail.replace(",", "").trim().equals("") || (childEmail.replace(",", "").trim().length() > 0))
				{
					ClassLinkBean linksToUpdate = new ClassLinkBean();
					linksToUpdate.setEventId(classId);
					linksToUpdate.setEmail(childEmail);
					linksList.add(linksToUpdate);
				}
			}
			connections.addClassLinks(linksList);

			//Update Class information, redirect
			removeAlerts(request);
			request.getSession(true).setAttribute("classId", classId);
			request.getSession(true).setAttribute("className", className);
			request.getSession(true).setAttribute("classYear", tutorYear);
			request.getSession(true).setAttribute("classTutor", teacherUser);
			addSessionAttributesForChildLinks(request, classId);

			request.getSession(true).setAttribute("formSuccess", "Class updated Successfully.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/class/editclass.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to Class page after update.",e);
			}
		}
		else
		{
			LOG.error("Cannot update without class and teacher information");
			removeAlerts(request);
			request.getSession(true).setAttribute("formErrors", "Class could not be updated.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/class/viewclass.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to Class page after update failure.",e);
			}
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");

		request.getSession(true).removeAttribute("classId");
		request.getSession(true).removeAttribute("className");
		request.getSession(true).removeAttribute("classYear");
		request.getSession(true).removeAttribute("classTutor");
		request.getSession(true).removeAttribute("allLinkChildren");
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
