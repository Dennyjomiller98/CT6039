package com.uog.miller.s1707031_ct6039.mail;

public interface IEmailer
{
	void generateMailForCalendarCreate(String recipient, String calendarEventName, String dueDate);
}
