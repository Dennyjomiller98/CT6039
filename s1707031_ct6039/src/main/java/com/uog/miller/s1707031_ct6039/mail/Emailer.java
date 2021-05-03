package com.uog.miller.s1707031_ct6039.mail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
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

	public void generateMailForCalendarCreate(String recipient, String calendarEventName, String dueDate)
	{
		String subject = "An event has been created";
		String message = "A new Calendar event (Event Name: '" + calendarEventName + "') has been created. \r\n This event deadline is: " + dueDate + ".";
		generateMail(subject, message, recipient);
	}

	public void generateMailForHomeworkGrade()
	{

	}

	public void generateMailForHomeworkSubmit()
	{

	}

	public void generateMailForProfileUpdate(String pin, String recipient)
	{
		String subject = "Your Profile has been updated";
		String message = "Test";
		generateMail(subject, message, recipient);
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
			message.setText(messageContent);

			Transport.send(message);
			LOG.debug("Sent message successfully");
		} catch (MessagingException e) {
			LOG.error(e);
		}
	}
}

