package com.uog.miller.s1707031_ct6039.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;

public class Emailer
{
	static final Logger LOG = Logger.getLogger(Emailer.class);
	private String username;
	private String password;

	public Emailer()
	{
		Properties props = new Properties();
		try
		{
			InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("/config/config.properties");
			if(resourceAsStream != null)
			{
				props.load(resourceAsStream);
				username = props.getProperty("mailsender");
				password = props.getProperty("mailpword");
			}
			else
			{
				throw new FileNotFoundException("Could not find properties file for configuring smtp mail");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	public Emailer(HttpServletRequest request)
	{
		Properties props = new Properties();
		try
		{
			InputStream resourceAsStream = getClass().getClassLoader().getResourceAsStream("/config/config.properties");
			if(resourceAsStream != null)
			{
				props.load(resourceAsStream);
				username = props.getProperty("mailsender");
				password = props.getProperty("mailpword");
				request.getSession(true).setAttribute("shouldNotifyErr", "We have props:" + props + props.getProperty("mailsender"));
			}
			else
			{
				Class<? extends Emailer> aClass = getClass();
				ClassLoader classLoader = aClass.getClassLoader();
				InputStream res = classLoader.getResourceAsStream("config/config.properties");
				request.getSession(true).setAttribute("shouldNotifyErr", "We have no props: (Class/Classloader/resource):" + aClass + classLoader + res);
				throw new FileNotFoundException("Could not find properties file for configuring smtp mail");
			}
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}

	}

	public void generateMailForCalendarCreate(String recipient, String calendarEventName, String dueDate, HttpServletRequest request)
	{
		String subject = "An event has been created";
		String message = "A new Calendar event (Event Name: " + calendarEventName + ") has been created. \r\n" + "This event deadline is: " + dueDate + ".";
		request.getSession(true).setAttribute("shouldNotify", "UserShouldBeNotified - email info: '" + username + "' and '" + password + "'");
		generateMail(subject, message, recipient);
	}

	public void generateMailForProfileCreate(String recipient, String firstname)
	{
		String subject = "New Account Creation";
		String message = "Hello " + firstname + ". Your email has been used to create a new account on our system. \r\n" + "If this was not done by you, please get in contact to resolve this.";
		generateMail(subject, message, recipient);
	}

	public void generateMailForProfileUpdate(String recipient, String firstname)
	{
		String subject = "Your Profile has been updated";
		String message = firstname + ", this is an automated notification alerting you that your account Profile has recently been updated. \r\n" + "If this was not done by you, please get in contact to resolve this.";
		generateMail(subject, message, recipient);
	}

	public void generateMailForHomeworkGrade(String childRecipient, String grade)
	{
		//Grade is under traffic light "Green/Amber/Red" system. (Follow the "Superb/Good/Keep Trying" convention used previously)
		String value;
		if(grade.equalsIgnoreCase("green"))
		{
			value = "Superb!";
		}
		else if(grade.equalsIgnoreCase("amber"))
		{
			value = "Good!";
		}
		else if(grade.equalsIgnoreCase("red"))
		{
			value = "Keep Trying!";
		}
		else
		{
			value = "Unknown - Please check your Homework page for results.";
		}
		String subject = "Your Teacher has provided Homework Grade";
		String message = "This is an automated notification alerting you that your Teacher has recently marked your submitted homework. \r\n"
				+ "Your Grade was: '" + value + "' \r\n"
				+ "If this was not done by you, please get in contact to resolve this.";
		generateMail(subject, message, childRecipient);
	}

	public void generateMailForHomeworkSubmit(String email, String filename)
	{
		String subject = "Homework Submission Confirmation";
		String message = "This is an automated response confirming your homework submission (File:" + filename + "has been submitted. \r\n" + "If this was not done by you, please get in contact to resolve this.";
		generateMail(subject, message, email);
	}

	//Generates Mail with pre-defined content
	private void generateMail(String subject, String messageContent, String recipientAddress)
	{
		//Generate plain/text mail (no need for HTML)
		String from = "dennyjomiller98@gmail.com";
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.ssl.trust", "smtp.gmail.com");
		Session session = Session.getInstance(props,
				new javax.mail.Authenticator() {
					@Override
					protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(username, password);
					}
				});
		try {
			// Create a default MimeMessage object.
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(from));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientAddress));
			message.setSubject(subject);
			messageContent = messageContent + "\r\n \r\n " + "This mail was sent automatically as part of a University of Gloucestershire Dissertation Project, by Denny-Jo Miller (s1707031). Any personal information provided will not be retained after testing has concluded, and will be purged after the testing and feedback period.";
			message.setText(messageContent);

			Transport.send(message);
			LOG.debug("Sent message successfully");
		} catch (MessagingException e) {
			LOG.error(e);
		}
	}
}

