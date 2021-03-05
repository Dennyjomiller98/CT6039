package com.uog.miller.s1707031_ct6039.servlets.users.teacher;

import com.uog.miller.s1707031_ct6039.beans.TeacherBean;
import com.uog.miller.s1707031_ct6039.oracle.TeacherConnections;
import java.io.IOException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Teacher Profile operations, such as Edit.
 */
@WebServlet(name = "TeacherProfile")
public class TeacherProfile extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(TeacherProfile.class);

	//Teacher Profile
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to update Account");
		//Get form params
		String email = (String) request.getSession(true).getAttribute("email");
		String title = request.getParameter("title");
		String titleVal = request.getParameter("title-value");
		String firstname = request.getParameter("firstname");
		String surname = request.getParameter("surname");
		String dob = request.getParameter("dob");
		String year = request.getParameter("tutor");
		String address = request.getParameter("address-value");
		String pword = request.getParameter("pword");
		String newPword = request.getParameter("newPword");
		String pwordConfirm = request.getParameter("pwordConfirm");

		String calEmail = request.getParameter("calendarEmail");
		String profEmail = request.getParameter("profileEmail");
		String hwEmail = request.getParameter("homeworkEmail");

		boolean calendarEmail = validateEmailSettings(calEmail);
		boolean profileEmail = validateEmailSettings(profEmail);
		boolean homeworkEmail = validateEmailSettings(hwEmail);

		if(email != null)
		{
			//Create bean and pass to DB
			TeacherConnections connections = new TeacherConnections();
			//Validate User
			TeacherBean teacherBean = connections.validateCredentials(email, pword);
			if(teacherBean != null)
			{
				validatePwords(pword, newPword, pwordConfirm, teacherBean);

				String userTitle = validateTitle(title, titleVal);
				//Populate bean with form params
				teacherBean.setTitle(userTitle);
				teacherBean.setFirstname(firstname);
				teacherBean.setSurname(surname);
				teacherBean.setDOB(dob);
				teacherBean.setAddress(address);
				teacherBean.setYear(year);
				teacherBean.setEmailForHomework(homeworkEmail);
				teacherBean.setEmailForProfile(profileEmail);
				teacherBean.setEmailForCalendar(calendarEmail);

				updateAccount(request, response, connections, teacherBean);
			}
			else
			{
				//Can't find user to update
				LOG.error("Unable to find user using email: " + email);
				request.getSession(true).removeAttribute("formSuccess");
				request.getSession(true).setAttribute("formErrors", "Could not update account details.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherprofile.jsp");
				}
				catch (IOException e)
				{
					LOG.error("Unable to redirect back to after user update failure.",e);
				}
			}
		}
		else
		{
			//Cannot update DB without user email
			LOG.error("Unable to find user without email");
			request.getSession(true).removeAttribute("formSuccess");
			request.getSession(true).setAttribute("formErrors", "Could not update account details.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherprofile.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to after user update failure.",e);
			}
		}
	}

	private void updateAccount(HttpServletRequest request, HttpServletResponse response, TeacherConnections connections, TeacherBean teacherBean)
	{
		connections.updateAccount(teacherBean);

		TeacherBean bean = connections.validateCredentials(teacherBean.getEmail(), teacherBean.getPword());
		updateSession(bean, request);
		//Redirect
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).setAttribute("formSuccess", "Account details updated.");
		try
		{
			response.sendRedirect(request.getContextPath() + "/jsp/users/teacher/teacherprofile.jsp");
		}
		catch (IOException e)
		{
			LOG.error("Unable to redirect back to after user update failure.",e);
		}
	}

	private String validateTitle(String title, String titleVal)
	{
		String ret;
		if(title.equals("Other") && (titleVal != null && !titleVal.equals("")) )
		{
			ret = titleVal;
		}
		else
		{
			ret = title;
		}
		return ret;
	}

	private boolean validateEmailSettings(String checkBoxVal)
	{
		boolean shouldEmail;
		if (checkBoxVal == null)
		{
			shouldEmail = false;
		}
		else
		{
			shouldEmail = checkBoxVal.equals("on");
		}
		return shouldEmail;
	}

	private void validatePwords(String pword, String newPword, String pwordConfirm, TeacherBean bean)
	{
		//Validate New pword if exist
		if(newPword != null && (!newPword.equals("") && !pwordConfirm.equals("")) && newPword.equals(pwordConfirm))
		{
			bean.setPword(newPword);
		}
		else if((newPword == null && pwordConfirm == null))
		{
			bean.setPword(pword);
		}
		else
		{
			bean.setPword(pword);
		}
	}

	private void updateSession(TeacherBean bean, HttpServletRequest request)
	{
		//Populate session for Teacher
		request.getSession(true).setAttribute("firstname", bean.getFirstname());
		request.getSession(true).setAttribute("surname", bean.getSurname());
		request.getSession(true).setAttribute("email", bean.getEmail());
		request.getSession(true).setAttribute("dob", bean.getDOB());
		request.getSession(true).setAttribute("address", bean.getAddress());
		request.getSession(true).setAttribute("year", bean.getYear());
		request.getSession(true).setAttribute("pword", bean.getPword());
		request.getSession(true).setAttribute("title", bean.getTitle());
		request.getSession(true).setAttribute("homeworkEmail", bean.getEmailForHomework());
		request.getSession(true).setAttribute("calendarEmail", bean.getEmailForCalendar());
		request.getSession(true).setAttribute("profileEmail", bean.getEmailForProfile());
		//Custom Teacher session login attribute
		request.getSession(true).setAttribute("isTeacher", "true");
	}
}