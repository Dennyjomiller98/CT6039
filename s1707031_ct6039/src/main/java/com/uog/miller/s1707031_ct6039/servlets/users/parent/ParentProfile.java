package com.uog.miller.s1707031_ct6039.servlets.users.parent;

import com.uog.miller.s1707031_ct6039.beans.LinkBean;
import com.uog.miller.s1707031_ct6039.beans.ParentBean;
import com.uog.miller.s1707031_ct6039.oracle.LinkedConnections;
import com.uog.miller.s1707031_ct6039.oracle.ParentConnections;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;

/**
 *	Action Servlet for Parent Profile operations, such as Edit/Delete.
 */
@WebServlet(name = "ParentProfile")
public class ParentProfile extends HttpServlet
{
	static final Logger LOG = Logger.getLogger(ParentProfile.class);

	//Parent Profile
	@Override
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	{
		LOG.debug("Attempting to update account");

		String email = (String) request.getSession(true).getAttribute("email");
		String firstname = request.getParameter("firstname");
		String surname = request.getParameter("surname");
		String dob = request.getParameter("dob");
		String address = request.getParameter("address-value");
		String[] linkedChildIds = request.getParameterValues("childSelect[]");
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
			//Create bean to use for update
			ParentConnections connections = new ParentConnections();

			ParentBean parentBean = connections.validateCredentials(email, pword);
			if(parentBean != null)
			{
				validatePwords(pword, newPword, pwordConfirm, parentBean);

				//Populate bean with form params
				parentBean.setFirstname(firstname);
				parentBean.setSurname(surname);
				parentBean.setDOB(dob);
				parentBean.setAddress(address);
				parentBean.setEmailForHomework(homeworkEmail);
				parentBean.setEmailForProfile(profileEmail);
				parentBean.setEmailForCalendar(calendarEmail);

				//Handle LinkedChildren, remove links no longer here and add new links
				handleParentChildLinks(linkedChildIds, parentBean);
				updateAccount(request, response, connections, parentBean);
			}
			else
			{
				//Can't find user to update
				LOG.error("Unable to find user using email: " + email);
				request.getSession(true).removeAttribute("formSuccess");
				request.getSession(true).setAttribute("formErrors", "Could not update account details.");
				try
				{
					response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentprofile.jsp");
				}
				catch (IOException e)
				{
					LOG.error("Unable to redirect back to after user update failure.",e);
				}
			}
		}
		else
		{
			//No email to search DB with
			LOG.error("Unable to find user without email");
			request.getSession(true).removeAttribute("formSuccess");
			request.getSession(true).setAttribute("formErrors", "Could not update account details.");
			try
			{
				response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentprofile.jsp");
			}
			catch (IOException e)
			{
				LOG.error("Unable to redirect back to after user update failure.",e);
			}
		}
	}

	private void handleParentChildLinks(String[] linkedChildIds, ParentBean parentBean)
	{
		LinkedConnections linkedConnections = new LinkedConnections();
		List<LinkBean> allChildLinksForParent = linkedConnections.findAllChildLinksForParent(parentBean.getEmail());

		ArrayList<LinkBean> existingLinkBeans = new ArrayList<>();
		//Remove old unused Links, loop existing links
		for (LinkBean linkBean : allChildLinksForParent)
		{
			boolean shouldKeep = false;
			for (String linkedChildId : linkedChildIds)
			{
				if(linkBean.getChildEmail().trim().equals(linkedChildId.trim()))
				{
					shouldKeep = true;
					existingLinkBeans.add(linkBean);
				}
			}

			if(!shouldKeep)
			{
				//Remove from DB
				linkedConnections.removeLink(linkBean.getId());
			}
		}

		//Check for any new Emails provided in linkedChildIds that are not currently linked
		addNewParentChildLinks(linkedChildIds, parentBean, linkedConnections, existingLinkBeans);

		//Finally, get all link IDs to set into updated Profile row
		String allLinks = linkedConnections.getAllLinks(parentBean.getEmail());
		parentBean.setLinkedChildIds(allLinks);
	}

	//Add new links, but don't duplicate add existing links
	private void addNewParentChildLinks(String[] linkedChildIds, ParentBean parentBean, LinkedConnections linkedConnections, ArrayList<LinkBean> existingLinkBeans)
	{
		for (String linkedChildEmail : linkedChildIds)
		{
			if(existingLinkBeans.isEmpty())
			{
				//New Email, add parent-child link to DB
				linkedConnections.linkChildren(parentBean.getEmail(), linkedChildEmail.trim());
			}
			else
			{
				for (LinkBean existingLinkBean : existingLinkBeans)
				{
					if(!existingLinkBean.getChildEmail().trim().equals(linkedChildEmail.trim()))
					{
						//New Email, add parent-child link to DB
						linkedConnections.linkChildren(parentBean.getEmail(), linkedChildEmail.trim());
					}
				}
			}

		}
	}

	private void updateAccount(HttpServletRequest request, HttpServletResponse response, ParentConnections connections, ParentBean parentBean)
	{
		//attempt DB update
		connections.updateAccount(parentBean);

		ParentBean validatedBean = connections.validateCredentials(parentBean.getEmail(), parentBean.getPword());
		updateSession(validatedBean, request);
		//Update parent-child links, to view on dropdown
		addSessionAttributesForLinks(request, parentBean.getEmail());
		//Redirect
		request.getSession(true).removeAttribute("formErrors");
		request.getSession(true).setAttribute("formSuccess", "Account details updated.");
		try
		{
			response.sendRedirect(request.getContextPath() + "/jsp/users/parent/parentprofile.jsp");
		}
		catch (IOException e)
		{
			LOG.error("Unable to redirect back to after user update failure.",e);
		}
	}

	private void validatePwords(String pword, String newPword, String pwordConfirm, ParentBean parentBean)
	{
		//Validate New pword if exist
		if(newPword != null && (!newPword.equals("") && !pwordConfirm.equals("")) && newPword.equals(pwordConfirm))
		{
			parentBean.setPword(newPword);
		}
		else if((newPword == null && pwordConfirm == null))
		{
			parentBean.setPword(pword);
		}
		else
		{
			parentBean.setPword(pword);
		}
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

	private void updateSession(ParentBean bean, HttpServletRequest request)
	{
		//Populate session for Parent
		request.getSession(true).setAttribute("firstname", bean.getFirstname());
		request.getSession(true).setAttribute("surname", bean.getSurname());
		request.getSession(true).setAttribute("email", bean.getEmail());
		request.getSession(true).setAttribute("dob", bean.getDOB());
		request.getSession(true).setAttribute("address", bean.getAddress());
		request.getSession(true).setAttribute("linkedChildId", bean.getLinkedChildIds());
		request.getSession(true).setAttribute("pword", bean.getPword());
		request.getSession(true).setAttribute("homeworkEmail", bean.getEmailForHomework());
		request.getSession(true).setAttribute("calendarEmail", bean.getEmailForCalendar());
		request.getSession(true).setAttribute("profileEmail", bean.getEmailForProfile());
		//Custom Parent session login attribute
		request.getSession(true).setAttribute("isParent", "true");
	}

	//Allows Registration forms/etc to populate Year select dropdown
	private void addSessionAttributesForLinks(HttpServletRequest request, String parentEmail)
	{
		Map<String, String> allChildren;
		LinkedConnections connections = new LinkedConnections();
		List<LinkBean> allChildLinksForParent = connections.findAllChildLinksForParent(parentEmail);
		if(allChildLinksForParent != null)
		{
			request.getSession(true).setAttribute("allLinkBeans", allChildLinksForParent);
		}
		allChildren = connections.getAllChildren();
		if(allChildren != null)
		{
			request.getSession(true).setAttribute("allChildren", allChildren);
		}
	}
}