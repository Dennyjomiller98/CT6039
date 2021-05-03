package com.uog.miller.s1707031_ct6039.servlets.homework;

import com.uog.miller.s1707031_ct6039.beans.ClassLinkBean;
import com.uog.miller.s1707031_ct6039.beans.HomeworkBean;
import com.uog.miller.s1707031_ct6039.oracle.CalendarConnections;
import com.uog.miller.s1707031_ct6039.oracle.ClassConnections;
import com.uog.miller.s1707031_ct6039.oracle.HomeworkConnections;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "AddHomework")
public class AddHomework extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(AddHomework.class);

	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to Add Homework for Class");
		//Get form variables, add to Bean and attempt to store in DB
		String teacherEmail = (String) request.getSession(true).getAttribute("email");
		if(teacherEmail != null)
		{
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			String setDate = request.getParameter("setDate");
			String dueDate = request.getParameter("dueDate");
			String fullDate = request.getParameter("fullDate");
			String selectedClass = request.getParameter("classSelect");

			//Check set date is before due date
			boolean dateSanity = checkSetAndDueDate(setDate, dueDate);
			if(dateSanity)
			{
				HomeworkBean beanToAdd = new HomeworkBean();
				beanToAdd.setName(name);
				beanToAdd.setDescription(description);
				beanToAdd.setTeacher(teacherEmail);
				beanToAdd.setSetDate(setDate);
				beanToAdd.setDueDate(dueDate);
				beanToAdd.setClassId(selectedClass);

				createHomeworkItems(request, response, fullDate, beanToAdd);
			}
			else
			{
				//Alert user, Due date must be after todays date
				removeAlerts(request);
				request.getSession(true).setAttribute("formErrors", "Due Date must be after today!");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/addhomework.jsp");
				}
				catch (IOException e)
				{
					LOG.error("Unable to redirect back to Homework view page after class hw creation failure.",e);
				}
			}
		}
		else
		{
			//If not a teacher, cannot assign homework
			removeAlerts(request);
			request.getSession(true).setAttribute("formErrors", "Teacher Email not found, try logging in again");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/addhomework.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to Homework view page after class hw creation failure.",e);
			}
		}
	}

	private void createHomeworkItems(HttpServletRequest request, HttpServletResponse response, String fullDate, HomeworkBean beanToAdd)
	{
		//Add to DB
		HomeworkConnections connections = new HomeworkConnections();
		String newTaskId = connections.addHomeworkTask(beanToAdd);

		//Get all children in class
		ClassConnections classConnections = new ClassConnections();
		List<ClassLinkBean> childrenFromClassId = classConnections.getChildrenFromClassId(beanToAdd.getClassId());
		for (ClassLinkBean classLinkBean : childrenFromClassId)
		{
			//Use the newTaskId to add submission attempts for each child in the class
			connections.addHomeworkForChildren(newTaskId, classLinkBean.getEmail());

			//Create Calendar entries for every child in class HW assigned to
			CalendarConnections calendarConnections = new CalendarConnections();
			String addCalEventDate = alterDateForCalendar(beanToAdd.getDueDate());
			calendarConnections.addCalendarItemForUser(classLinkBean.getEmail(), "HW: "+ beanToAdd.getName(), fullDate, addCalEventDate);
		}

		//Redirect
		removeAlerts(request);
		addAttributesForTeacherHomeworks(request);
		request.getSession(true).setAttribute("formSuccess", "Homework Assigned to Class");
		try
		{
			response.sendRedirect(request.getContextPath() + "/jsp/actions/homework/viewhomework.jsp");
		}
		catch (IOException e)
		{
			LOG.error("Unable to redirect back to Homework view page after class hw creation.",e);
		}
	}

	private void addAttributesForTeacherHomeworks(HttpServletRequest request)
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

	private String alterDateForCalendar(String dueDate)
	{
		String ret;
		String[] split = dueDate.split("-");
		String month = split[1];
		String newMonth;
		switch (month)
		{
			case "01":
				newMonth = "00";
				break;
			case "02":
				newMonth = "01";
				break;
			case "03":
				newMonth = "02";
				break;
			case "04":
				newMonth = "03";
				break;
			case "05":
				newMonth = "04";
				break;
			case "06":
				newMonth = "05";
				break;
			case "07":
				newMonth = "06";
				break;
			case "08":
				newMonth = "07";
				break;
			case "09":
				newMonth = "08";
				break;
			case "10":
				newMonth = "09";
				break;
			case "11":
				newMonth = "10";
				break;
			case "12":
				newMonth = "11";
				break;
			default:
				newMonth = month;
		}
		ret = split[0] + "-" + newMonth + "-" + split[2];
		return ret;
	}

	//Returns true if due date is after set date
	private boolean checkSetAndDueDate(String setDate, String dueDate)
	{
		boolean ret = false;
		String[] setSplit = setDate.split("-");
		String[] dueSplit = dueDate.split("-");
		Date set = new Date(Integer.parseInt(setSplit[0]), Integer.parseInt(setSplit[1]), Integer.parseInt(setSplit[2]));
		Date due = new Date(Integer.parseInt(dueSplit[0]), Integer.parseInt(dueSplit[1]), Integer.parseInt(dueSplit[2]));
		if(due.after(set))
		{
			ret = true;
		}
		return ret;
	}

	private void removeAlerts(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");
	}
}
