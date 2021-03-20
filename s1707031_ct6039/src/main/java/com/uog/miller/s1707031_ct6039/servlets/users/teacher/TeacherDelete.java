package com.uog.miller.s1707031_ct6039.servlets.users.teacher;

import com.uog.miller.s1707031_ct6039.oracle.ClassConnections;
import com.uog.miller.s1707031_ct6039.oracle.TeacherConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

@WebServlet(name = "TeacherDelete")
public class TeacherDelete extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(TeacherDelete.class);
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Deleting Teacher");
		String email = (String) request.getSession(true).getAttribute("email");
		if(email != null)
		{
			//Verify we can delete class
			ClassConnections classConn = new ClassConnections();
			boolean isTutor = classConn.isTeacherForClass(email);
			if(isTutor)
			{
				//unable to delete
				request.getSession(true).removeAttribute("formSuccess");
				request.getSession(true).setAttribute("formErrors", "User is a Class Tutor, cannot delete account until Class teacher is changed.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherprofile.jsp");
				}
				catch (IOException e)
				{
					LOG.error("Unable to redirect back to after user delete failure.",e);
				}
			}
			else
			{
				//Safe to delete, won't break class
				TeacherConnections connections = new TeacherConnections();
				connections.deleteAccount(email);
				removeSessionAttributes(request);
				request.getSession(true).setAttribute("formSuccess", "User Deleted.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/index.jsp");
				}
				catch (IOException e)
				{
					LOG.error("Unable to redirect back to after user delete.",e);
				}
			}
		}
		else
		{
			//Can't find user to delete
			LOG.error("Unable to find a user without an email");
			request.getSession(true).removeAttribute("formSuccess");
			request.getSession(true).setAttribute("formErrors", "Could not delete user.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherprofile.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to after user delete failure.",e);
			}
		}
	}

	private void removeSessionAttributes(HttpServletRequest request)
	{
		request.getSession(true).removeAttribute("firstname");
		request.getSession(true).removeAttribute("surname");
		request.getSession(true).removeAttribute("email");
		request.getSession(true).removeAttribute("dob");
		request.getSession(true).removeAttribute("address");
		request.getSession(true).removeAttribute("year");
		request.getSession(true).removeAttribute("pword");
		request.getSession(true).removeAttribute("homeworkEmail");
		request.getSession(true).removeAttribute("calendarEmail");
		request.getSession(true).removeAttribute("profileEmail");
		request.getSession(true).removeAttribute("title");
		//Custom Teacher session login attribute
		request.getSession(true).removeAttribute("isTeacher");

		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).removeAttribute("formSuccess");

		request.getSession(true).removeAttribute("allYears");
		request.getSession(true).removeAttribute("allChildren");
		request.getSession(true).removeAttribute("allClasses");

		request.getSession(true).removeAttribute("allHomeworks");
		request.getSession(true).removeAttribute("allHomeworksTeacher");
		request.getSession(true).removeAttribute("allSubmissions");
		request.getSession(true).removeAttribute("retrievedSubmissions");
		request.getSession(true).removeAttribute("homeworkForGrading");
	}
}