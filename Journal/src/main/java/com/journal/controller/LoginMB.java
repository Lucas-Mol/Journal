package com.journal.controller;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.journal.dao.UserDAO;
import com.journal.model.User;
import com.journal.util.GrowlUtils;
import com.journal.util.PasswordUtils;

@ManagedBean
@ViewScoped
public class LoginMB {
	
	private FormForgotPasswordBB formForgotPasswordBB;
	
	private String username;
	private String password;
	private UserDAO userDAO = new UserDAO();
	
	public void init() {
		formForgotPasswordBB = new FormForgotPasswordBB(username);
	}
	
	public void login() {
		boolean validatedFields = validateFields();
		
		if(!validatedFields) return;
		
		User user = userDAO.findByUsernameOrEmail(username);
		 
		try {
			if(user != null && validateUserWithFields(user, username, password)) {
						
					User sessionUser = SessionMB.getInstance().getSessionUser();
						
					if (sessionUser != null) {
							
						if (validateSessionUserWithLoggingUser(sessionUser, user)) {
							FacesContext.getCurrentInstance().getExternalContext().redirect("app/dashboard.xhtml");
						}
							
						SessionMB.getInstance().finishSession();
						FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
					}
					
					//Login and redirect to dashboard
					SessionMB.getInstance().setSessionUser(user);
					FacesContext.getCurrentInstance().getExternalContext().redirect("app/dashboard.xhtml");
				}  else {
				GrowlUtils.addErrorMessage("Incorrect", "Incorrect username or password!");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private boolean validateUserWithFields(User user, String login, String password) {		
		return (user.getUsername().equals(login) || user.getEmail().equals(login)) 
				&& user.getPassword().equals(PasswordUtils.encryptPassword(password));
	}
	
	private boolean validateSessionUserWithLoggingUser(User sessionUser, User loggingUser) {
		return sessionUser.getEmail().equals(loggingUser.getEmail()) 
				&& sessionUser.getUsername().equals(loggingUser.getUsername());
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
