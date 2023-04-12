package com.journal.services;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.junit.jupiter.api.Test;

import com.journal.model.User;
import com.journal.service.MailService;
import com.journal.util.Constants;

public class MailServiceTest {

	@Test
	public void testDefaultSendEmail() {	
		MimeMultipart multipart = new MimeMultipart();
		String htmlBody = "<h1>Mail test</h1>" +
                "<p>This is a email test form Journal while running Junit tests</p>" +
                "<img style='height=100px; width=100px' src=\"cid:image\">";
		
		try {
			MimeBodyPart imagePart = new MimeBodyPart();
			imagePart.attachFile(new File("src/main/webapp/resources/images/welcome-fun.png"));
			imagePart.setContentID("<image>");
			
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");

			multipart.addBodyPart(htmlPart);
			multipart.addBodyPart(imagePart);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		boolean success = MailService.sendEmail(Constants.MAIL_TO_TEST, "Journal Test", multipart);
		
		assertTrue(success);
	}
	
	@Test
	public void testSignupSendEmail() {	
		
		User newUser = new User();
		
		newUser.setUsername("Mol");
		newUser.setEmail(Constants.MAIL_TO_TEST);
		
		boolean success = MailService.sendSignupEmail(newUser);
		
		assertTrue(success);
	}

}
