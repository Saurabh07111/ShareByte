package com.sharebyte.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import com.sharebyte.controllers.AuthController;
import jakarta.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService{

    private final AuthController authController;
	
	@Autowired
	private JavaMailSender mailSender;

    EmailServiceImpl(AuthController authController) {
        this.authController = authController;
    }

	@Override
	public void sendMail(String to, String subject, String body) {
		MimeMessage message =   mailSender.createMimeMessage();
		try {
			MimeMessageHelper helper = new MimeMessageHelper(message, true);
			
			
			helper.setTo(to);
			
			helper.setSubject(subject);
			
			helper.setText(body, true);
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println(e);
		}
		
		mailSender.send(message);
		
	}
	
	
	

}
