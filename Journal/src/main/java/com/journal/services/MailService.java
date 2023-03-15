package com.journal.services;

import java.io.File;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.ServletContext;

import com.journal.model.User;
import com.journal.util.Constants;

public class MailService {

	public static boolean sendEmail(String receiver, String subject, MimeMultipart bodyPart) {

		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.office365.com");
		props.put("mail.smtp.port", "587");
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.timeout", "1000000");
		
		Session session = Session.getInstance(props, new Authenticator() {
	                protected PasswordAuthentication getPasswordAuthentication() {
	                    return new PasswordAuthentication(Constants.MAIL_FROM, Constants.MAIL_FROM_PASSWORD);}
	            });
		
		try {
			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(Constants.MAIL_FROM));
			message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(receiver));
			message.setSubject(subject);
			message.setContent(bodyPart);
	
			Transport.send(message);
	
			System.out.println("Mail sent to: " + receiver);
			return true;
		}catch(MessagingException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static boolean sendSignupEmail(User user) {
		return sendSignupEmail(user, null);
	}
	
	public static boolean sendSignupEmail(User user, ServletContext context) {
		MimeMultipart multipart = new MimeMultipart();
		String htmlBody = 	
				"<h1 style='display: flex; justify-content: center; color: #1E9AFF'>Welcome to Journal</h1>" + 
				"<img style='display: flex; justify-content: center;' src=\"cid:logo-img\">" +
				"<br style='display: flex; justify-content: center;'/>" +
				"<p style='display: flex; justify-content: center;'>Hello, " + user.getUsername() + "! Welcome to Journal. This website is a Web Development Project made by Lucas Mol.</p>" +
				"<br style='display: flex; justify-content: center;'/>" +
				"<p style='display: flex; justify-content: center;'>This project aims to increase my technical skills in Web Development using several Java APIs, from the Basics to the Enterprise environment.</p>" +
				"<br style='display: flex; justify-content: center;'/>" +
				"<p style='display: flex; justify-content: center;'>Enjoy the Journal and contact me in case of Interest, Bug or any Issue</p>" +
				"<img style='display: flex; justify-content: center;' src=\"cid:fun-img\">" +
				"<br style='display: flex; justify-content: center;'/>" +
				"<a style='display: flex; justify-content: center;' href=\"mailto:"+ Constants.PROFESSINAL_MAIL +"\">"+ Constants.PROFESSINAL_MAIL +"</a>";
		try {
			//TODO not a good practice
			String logoPath = (context != null)? context.getRealPath("/resources/images/full-logo.png") : "src/main/webapp/resources/images/full-logo.png";
			MimeBodyPart logoPart = new MimeBodyPart();
			logoPart.attachFile(new File(logoPath));
			logoPart.setContentID("<logo-img>");
			
			//TODO not a good practice
			String funPath = (context != null)? context.getRealPath("/resources/images/welcome-fun.png") : "src/main/webapp/resources/images/welcome-fun.png";
			MimeBodyPart funPart = new MimeBodyPart();
			funPart.attachFile(new File(funPath));
			funPart.setContentID("<fun-img>");
			
			MimeBodyPart htmlPart = new MimeBodyPart();
			htmlPart.setContent(htmlBody, "text/html");

			multipart.addBodyPart(htmlPart);
			multipart.addBodyPart(logoPart);
			multipart.addBodyPart(funPart);
			
			sendEmail(user.getEmail(), "Welcome to Journal", multipart);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
	}
	
}
