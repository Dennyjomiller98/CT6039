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

@WebServlet(name = "AddClass")
public class AddClass extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(AddClass.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting POST class addition");

		String teacherUser = (String) request.getSession(true).getAttribute("email");
		if(teacherUser != null)
		{
			String className = request.getParameter("className");
			String tutorYear = request.getParameter("tutor");
			String[] childrenInClass = request.getParameterValues("childSelect[]");
			try
			{
				//add Class to DB
				ClassConnections connections = new ClassConnections();
				//Populate Bean
				ClassBean classBean = new ClassBean();
				classBean.setName(className);
				classBean.setYear(tutorYear);
				classBean.setTeacher(teacherUser);
				connections.addClass(classBean);

				//Populate Class Link
				List<ClassBean> classFromTeacherEmail = connections.getClassFromTeacherEmail(teacherUser);
				ClassBean recentClass = classFromTeacherEmail.get(classFromTeacherEmail.size()-1);
				List<ClassLinkBean> linkBeans = new ArrayList<>();
				for (String childEmail : childrenInClass)
				{
					ClassLinkBean linkBean = new ClassLinkBean();
					linkBean.setEventId(recentClass.getEventId());
					linkBean.setEmail(childEmail);
					linkBeans.add(linkBean);
				}
				connections.addClassLinks(linkBeans);

				//Add to response
				removeAlerts(request);
				LOG.debug("Successfully added new class");
				request.getSession(true).setAttribute("formSuccess", "Class added successfully.");
				List<ClassBean> allClasses = connections.getClassFromTeacherEmail(teacherUser);
				request.getSession(true).setAttribute("allClasses", allClasses);

				response.sendRedirect(request.getContextPath() + "/jsp/actions/class/viewclass.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to add new class to DB", e);
			}
		}
		else
		{
			try
			{
				removeAlerts(request);
				LOG.error("Unable to add class.");
				request.getSession(true).setAttribute("formErrors", "Unable to add Class.");
				response.sendRedirect(request.getContextPath() + "/jsp/actions/class/addclass.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect after failure adding Class", e);
			}
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
		request.getSession(true).removeAttribute("allClasses");
	}
}
