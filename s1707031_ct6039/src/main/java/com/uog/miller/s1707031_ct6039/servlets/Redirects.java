package com.uog.miller.s1707031_ct6039.servlets;

import com.uog.miller.s1707031_ct6039.beans.ChildBean;
import com.uog.miller.s1707031_ct6039.beans.ClassBean;
import com.uog.miller.s1707031_ct6039.beans.HomeworkBean;
import com.uog.miller.s1707031_ct6039.beans.SubmissionBean;
import com.uog.miller.s1707031_ct6039.oracle.ClassConnections;
import com.uog.miller.s1707031_ct6039.oracle.HomeworkConnections;
import com.uog.miller.s1707031_ct6039.oracle.LinkedConnections;
import com.uog.miller.s1707031_ct6039.oracle.YearConnections;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Redirect operations (Mainly Navbar uses).
 */
@WebServlet(name = "Redirects")
public class Redirects extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(Redirects.class);

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		//When user clicks on an operation to redirect to new page, GET request called
		String location = request.getParameter("location");
		if(location != null)
		{
			//Select redirect based on location
			LOG.debug("Found location: " + location + ", attempting to redirect.");
			try
			{
				removeAlerts(request);
				String redirect = switchFindLocation(location, request);
				response.sendRedirect(request.getContextPath() + redirect);
			}
			catch(IOException e)
			{
				LOG.error("Unable to redirect using location:" + location);
			}
		}
		else
		{
			//Error, redirect to error page.
			try
			{
				LOG.error("No location specified, returning to homepage");
				response.sendRedirect(request.getContextPath() + "/index.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Error redirecting, unable to redirect", e);
			}
		}
	}

	private void removeAlerts(HttpServletRequest request)
	{
		//Otherwise (For example) you log in, browse pages, "Logged in Successfully" will retain. This way the alert is used for the relevant pages.
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
		request.getSession(true).removeAttribute("eventId");
		request.getSession(true).removeAttribute("eventName");
		request.getSession(true).removeAttribute("eventUser");
		request.getSession(true).removeAttribute("eventDate");
		request.getSession(true).removeAttribute("eventUpdateDate");
	}

	private String switchFindLocation(String location, HttpServletRequest request)
	{
		//Switch case uses request param to redirect user to correct page. If no location is specified, return to index homepage
		//(A lot cleaner than multiple servlet mappings for every redirect)
		String ret;
		switch (location)
		{
			case "child-login":
				ret = "/jsp/users/child/childlogin.jsp";
				break;
			case "child-register":
				addSessionAttributesForYear(request);
				ret = "/jsp/users/child/childregistration.jsp";
				break;
			case "child-profile":
				addSessionAttributesForYear(request);
				ret = "/jsp/users/child/childprofile.jsp";
				break;

			case "parent-login":
				ret = "/jsp/users/parent/parentlogin.jsp";
				break;
			case "parent-register":
				addSessionAttributesForLinks(request);
				ret = "/jsp/users/parent/parentregistration.jsp";
				break;
			case "parent-profile":
				addSessionAttributesForLinks(request);
				ret = "/jsp/users/parent/parentprofile.jsp";
				break;

			case "teacher-login":
				ret = "/jsp/users/teacher/teacherlogin.jsp";
				break;
			case "teacher-register":
				addSessionAttributesForYear(request);
				ret = "/jsp/users/teacher/teacherregistration.jsp";
				break;
			case "teacher-profile":
				addSessionAttributesForYear(request);
				ret = "/jsp/users/teacher/teacherprofile.jsp";
				break;

			case "calendar":
				ret = "/jsp/actions/calendar/viewcalendar.jsp";
				break;

			case "progress-request":
				ret = "/jsp/actions/progress/requestprogress.jsp";
				break;
			case "progress-submit":
				ret = "/jsp/actions/progress/submitprogress.jsp";
				break;
			case "progress-view":
				ret = "/jsp/actions/progress/viewprogress.jsp";
				break;

			case "homework-view":
				addSessionAttributesForHomeworksAndSubmissions(request);
				ret = "/jsp/actions/homework/viewhomework.jsp";
				break;
			case "homework-assign":
				getSelectedClass(request);
				ret = "/jsp/actions/homework/addhomework.jsp";
				break;
			case "homework-upload":
				addSessionAttributesForHomeworksAndSubmissions(request);
				ret = "/jsp/actions/homework/uploadhomework.jsp";
				break;
				case "homework-grade":
				addSessionAttributesForGrading(request);
				ret = "/jsp/actions/homework/gradehomework.jsp";
				break;

			case "class-view":
				addSessionAttributesForClass(request);
				ret = "/jsp/actions/class/viewclass.jsp";
				break;
			case "class-edit":
				ret = "/jsp/actions/class/editclass.jsp";
				break;
			case "class-add":
				addSessionAttributesForYear(request);
				addSessionAttributesForLinks(request);
				ret = "/jsp/actions/class/addclass.jsp";
				break;

			case "view-child":
				addSessionAttributesForParentViewingChildren(request);
				ret = "/jsp/users/parent/viewmychildren.jsp";
				break;

			case "login":
				ret = "/jsp/users/loginindex.jsp";
				break;
			case "register":
				ret = "/jsp/users/registerindex.jsp";
				break;

			case "home":
			default:
				ret = "/index.jsp";
				break;
		}
		return ret;
	}

	private void addSessionAttributesForGrading(HttpServletRequest request)
	{
		String homeworkId = request.getParameter("homeworkId");
		String child = request.getParameter("child");
		if (homeworkId != null)
		{
			HomeworkConnections connections = new HomeworkConnections();
			List<SubmissionBean> allSubmissionsForHomeworkTask = connections.getAllSubmissionsForHomeworkTask(homeworkId);
			for (SubmissionBean submissionBean : allSubmissionsForHomeworkTask)
			{
				if(submissionBean.getEventId().equals(homeworkId) && submissionBean.getEmail().equals(child))
				{
					request.getSession(true).setAttribute("homeworkForGrading", submissionBean);
					request.getSession(true).setAttribute("childForGrading", child);
				}
			}
		}
	}

	private void getSelectedClass(HttpServletRequest request)
	{
		String selectedClass = request.getParameter("class");
		if(selectedClass != null)
		{
			request.getSession(true).setAttribute("selectedClass", selectedClass);
		}
	}

	private void addSessionAttributesForParentViewingChildren(HttpServletRequest request)
	{
		String parentEmail = (String) request.getSession(true).getAttribute("email");
		if(parentEmail != null)
		{
			LinkedConnections connections = new LinkedConnections();
			List<ChildBean> allChildrenFromLinks = connections.getAllChildrenFromLinks(parentEmail);
			if(!allChildrenFromLinks.isEmpty())
			{
				request.getSession(true).setAttribute("myChildrenBeans", allChildrenFromLinks);
			}
		}
	}

	private void addSessionAttributesForClass(HttpServletRequest request)
	{
		String email = (String) request.getSession(true).getAttribute("email");
		if(email != null)
		{
			ClassConnections connections = new ClassConnections();
			List<ClassBean> classFromTeacherEmail = connections.getClassFromTeacherEmail(email);
			request.getSession(true).setAttribute("allClasses", classFromTeacherEmail);
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

	private void addSessionAttributesForHomeworksAndSubmissions(HttpServletRequest request)
	{
		String isChild = (String) request.getSession(true).getAttribute("isChild");
		String isTeacher = (String) request.getSession(true).getAttribute("isTeacher");

		if(isChild != null)
		{
			String childEmail = (String) request.getSession(true).getAttribute("email");
			if(childEmail != null)
			{
				retrieveChildHomeworkForSession(request, childEmail);
			}
		}
		else if (isTeacher != null)
		{
			String teacherEmail = (String) request.getSession(true).getAttribute("email");
			if(teacherEmail != null)
			{
				HomeworkConnections connections = new HomeworkConnections();
				List<HomeworkBean> allHomeworks = connections.getAllHomeworkForTeacher(teacherEmail);
				if(!allHomeworks.isEmpty())
				{
					request.getSession(true).setAttribute("allHomeworksTeacher", allHomeworks);
				}
			}
		}
	}

	private void retrieveChildHomeworkForSession(HttpServletRequest request, String childEmail)
	{
		HomeworkConnections connections = new HomeworkConnections();
		List<HomeworkBean> allHomeworkForChild = connections.getAllHomeworkForChild(childEmail);
		List<SubmissionBean> allHomeworkSubmissionsForChild = connections.getAllHomeworkSubmissionsForChild(childEmail);
		if(!allHomeworkForChild.isEmpty())
		{
			request.getSession(true).setAttribute("allHomeworks", allHomeworkForChild);
		}
		if(!allHomeworkSubmissionsForChild.isEmpty())
		{
			request.getSession(true).setAttribute("allSubmissions", allHomeworkSubmissionsForChild);
		}

		//Check for Specific HW
		String homeworkId = request.getParameter("homeworkId");
		if(homeworkId != null)
		{
			HomeworkConnections homeworkConnections = new HomeworkConnections();
			HomeworkBean homeworkTaskFromId = homeworkConnections.getHomeworkTaskFromId(homeworkId);
			request.getSession(true).setAttribute("homeworkName", homeworkTaskFromId.getName());
			request.getSession(true).setAttribute("homeworkSetDate", homeworkTaskFromId.getSetDate());
			request.getSession(true).setAttribute("homeworkDueDate", homeworkTaskFromId.getDueDate());
			request.getSession(true).setAttribute("homeworkTeacher", homeworkTaskFromId.getTeacher());
			request.getSession(true).setAttribute("homeworkIdUpload", homeworkId);
		}
	}
}
