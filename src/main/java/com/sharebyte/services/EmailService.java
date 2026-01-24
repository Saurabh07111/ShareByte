package com.sharebyte.services;

public interface EmailService {
	void sendMail(String to, String subject, String body);
}
