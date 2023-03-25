package com.journal.controller.backingBean;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

import org.primefaces.PrimeFaces;

import com.journal.controller.managedBean.SessionMB;
import com.journal.exception.ForgotPasswordException;
import com.journal.service.ForgotPasswordService;
import com.journal.util.Constants;
import com.journal.util.GrowlUtils;

@ManagedBean
@ViewScoped
public class FormForgotPasswordBB {
	
	private String login;
	private SessionMB session;
	private ForgotPasswordService forgotPasswordService = new ForgotPasswordService();
	
	private final int MAX_ATTEMPTS = 3;
	
	public FormForgotPasswordBB (String login, SessionMB session) {
		this.login = login;
		this.session = session;
	}
	
	public void sendEmail() {
		
		if(exceededPasswordResetAttempt()) {
			exceededAttemptsAlerts();
			return;
		}
		
		if(login == null || login.isEmpty()) {
			GrowlUtils.addWarningMessage("Missing Username/Email", "Please enter your Username/Email");
			return;
		}
		
		boolean success = false;
		
		try {
			success = forgotPasswordService.sendNewPassword(login);
		} catch (ForgotPasswordException fpe) {
			fpe.printStackTrace();
			GrowlUtils.addErrorMessage(fpe.growlTitle, fpe.growlMessage);
			return;
		}
		
		if(success) {
			GrowlUtils.addInfoMessage("Success", "You received a password reset email");
			PrimeFaces.current().executeScript("PF('ForgotPasswordDlg').hide();");
		}
	}
	
	private boolean exceededPasswordResetAttempt() {
		session.setNumPasswordResetAttempt(session.getNumPasswordResetAttempt() + 1);

		return session.getNumPasswordResetAttempt() > MAX_ATTEMPTS;
	}
	
	private void exceededAttemptsAlerts() {
		if(session.getNumPasswordResetAttempt() == MAX_ATTEMPTS + 1) {
			GrowlUtils.addWarningMessage("Exceeded Attempt", 
					"You've exceeded attempt to reset passwords. \n\r It's security approach to keep your data safe.");
			session.cleanAttempts();
		}
		
		if(session.getNumPasswordResetAttempt() == MAX_ATTEMPTS + 3) {
			GrowlUtils.addWarningMessage("Exceeded Attempt", 
					"We're seeing that you keep trying this. \n\r We'll let you try to reset password in about 30 minutes.");
			
		}
		
		if(session.getNumPasswordResetAttempt() == MAX_ATTEMPTS + 5) {
			GrowlUtils.addWarningMessage("Exceeded Attempt", 
					"You know it's not being efficient anymore, don't you?");
		}
		
		if(session.getNumPasswordResetAttempt() == MAX_ATTEMPTS + 7) {
			GrowlUtils.addWarningMessage("Exceeded Attempt", 
					"Hmm.. It's starting to get a little bit awkward!");
		}
		
		if(session.getNumPasswordResetAttempt() == Constants.MAX_ATTEMPT_PASS_RESET_TO_ERROR) {
			GrowlUtils.addErrorMessage("Violation", 
					"We're seeing it as Violation/Attack attempt. We're banning you!");
			try {
				ExternalContext externalContext = FacesContext.getCurrentInstance().getExternalContext();
				externalContext.redirect(externalContext.getRequestContextPath() + "/pages/login.xhtml");
			} catch (IOException e) {
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
