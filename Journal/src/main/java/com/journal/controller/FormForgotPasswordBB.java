package com.journal.controller;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.journal.dao.UserDAO;
import com.journal.model.User;
import com.journal.services.MailService;
import com.journal.util.GrowlUtils;
import com.journal.util.PasswordUtils;

@ManagedBean
@ViewScoped
public class FormForgotPasswordBB {
	
	private String login;
	private final int MAX_ATTEMPTS = 3;
	
	public FormForgotPasswordBB (String login) {
		this.login = login;
	}
	
	// THIS IS A TEMPORARY SOLUTION
	// TODO: Do a email sending a short life page' link using token which will expire in a short period.
	public void sendEmail() {
		
		if(exceededPasswordResetAttempt()) {
			exceededAttemptsDaemon();
			return;
		}
		
		if(login == null || login.isEmpty()) {
			GrowlUtils.addWarningMessage("Missing Username/Email", "Please enter your Username/Email");
			return;
		}
		
		UserDAO userDAO = new UserDAO();
		
		if(userDAO.existUsername(login) || userDAO.existEmail(login)) {
			User user = userDAO.findByUsernameOrEmail(login);
			
			try {
				String newPassword = PasswordUtils.generateNewPassword();
				String newEncryptedPassword = PasswordUtils.encryptPassword(newPassword);
				
				user.setPassword(newEncryptedPassword);
				userDAO.edit(user);
				
				//TODO: get the ServletContext to point image on email properly. Not a good practice
				ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

				MailService.sendForgotPasswordEmail(user, newPassword, context);
				GrowlUtils.addInfoMessage("Success", "You received a password reset email");
					
			} catch (Exception e) {
				GrowlUtils.addErrorMessage("Sorry", "We have a problem to re-generate your password. Please contact the System Administrator");
				e.printStackTrace();
			}
		} else {
			GrowlUtils.addWarningMessage("Username/Email not found", "This Username/Email wasn't found on Journal");
			return;
		}
	}
	
	private boolean exceededPasswordResetAttempt() {
		SessionMB.setNumPasswordResetAttempt(SessionMB.getNumPasswordResetAttempt() + 1);
		System.out.println(SessionMB.getNumPasswordResetAttempt());

		return SessionMB.getNumPasswordResetAttempt() > MAX_ATTEMPTS;
	}
	
	private void exceededAttemptsDaemon() {
		if(SessionMB.getNumPasswordResetAttempt() == MAX_ATTEMPTS + 1) {
			GrowlUtils.addWarningMessage("Exceeded Attempt", 
					"You've exceeded attempt to reset passwords. \n\r It's security approach to keep your data safe.");
			SessionMB.cleanAttempts();
		}
		
		if(SessionMB.getNumPasswordResetAttempt() == MAX_ATTEMPTS + 3) {
			GrowlUtils.addWarningMessage("Exceeded Attempt", 
					"We're seeing that you keep trying this. \n\r We'll let you try to reset password in about 30 minutes.");
			
		}
		
		if(SessionMB.getNumPasswordResetAttempt() == MAX_ATTEMPTS + 5) {
			GrowlUtils.addWarningMessage("Exceeded Attempt", 
					"You know it's not being efficient anymore, don't you?");
		}
		
		if(SessionMB.getNumPasswordResetAttempt() == MAX_ATTEMPTS + 7) {
			GrowlUtils.addWarningMessage("Exceeded Attempt", 
					"Hmm.. It's starting to get a little bit awkward!");
		}
		
		if(SessionMB.getNumPasswordResetAttempt() == MAX_ATTEMPTS + 12) {
			GrowlUtils.addErrorMessage("Violation", 
					"We're seeing it as Violation/Attack attempt. We're banning you!");
			try {
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				externalContext.redirect(externalContext.getRequestContextPath() + "/login.xhtml");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
}
