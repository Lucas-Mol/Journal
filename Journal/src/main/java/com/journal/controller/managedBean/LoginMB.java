package com.journal.controller.managedBean;

import javax.faces.application.ViewExpiredException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.journal.controller.backingBean.FormForgotPasswordBB;
import com.journal.exception.LogInException;
import com.journal.model.User;
import com.journal.service.LoginService;
import com.journal.util.Constants;
import com.journal.util.GrowlUtils;

@ManagedBean
@ViewScoped
public class LoginMB {
	
	private FormForgotPasswordBB formForgotPasswordBB;
	private SessionMB session = SessionMB.getInstance();
	private LoginService loginService = new LoginService();
	
	private String username;
	private String password;
	
	public void init() {
		
		if(session.getNumPasswordResetAttempt() >= Constants.MAX_ATTEMPT_PASS_RESET_TO_ERROR) {
			throw new ViewExpiredException();
		}
		formForgotPasswordBB = new FormForgotPasswordBB(username, session);
	}
	
	public void login() {
		User user;
		User sessionUser = session.getSessionUser();
		
		if(!validateFields()) return; 
		
		try {
			user = loginService.login(username, password, sessionUser);
		} catch (LogInException e) {
			GrowlUtils.addErrorMessage(e.growlTitle, e.growlMessage);
			e.printStackTrace();
			return;
		}

		if (user != null) {
			session.setSessionUser(user);
			redirectToDashboard();
		}
	}
	
	private void redirectToDashboard() {
		try {
			FacesContext.getCurrentInstance().getExternalContext().redirect("app/dashboard.xhtml");	
		} catch (Exception e) {
			e.printStackTrace();
			GrowlUtils.addErrorMessage("Sorry", "You had a redirect problem. Please try to login through Login Page");
		}
	}

	private boolean validateFields() {
		return checkRequiredFields();
	}
		
	private boolean checkRequiredFields() {
		if(this.username == null || this.username.isEmpty()) {
			GrowlUtils.addErrorMessage("Username", "The 'username' field is required");
			return false;
		}
		if(this.password == null || this.password.isEmpty()) {
			GrowlUtils.addErrorMessage("Password", "The 'password' field is required");
			return false;
		}
		return true;
	}
	
	public void updateUsernameFormForgotPasswordBB() {
		formForgotPasswordBB.setLogin(username);
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}

	public FormForgotPasswordBB getFormForgotPasswordBB() {
		return formForgotPasswordBB;
	}

	public void setFormForgotPasswordBB(FormForgotPasswordBB formForgotPasswordBB) {
		this.formForgotPasswordBB = formForgotPasswordBB;
	}
	
	
	
}
