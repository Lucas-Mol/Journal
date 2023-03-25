package com.journal.controller.managedBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.journal.exception.LogInException;
import com.journal.exception.MailException;
import com.journal.exception.SignUpException;
import com.journal.model.User;
import com.journal.service.SignupService;
import com.journal.util.GrowlUtils;

@ManagedBean
@ViewScoped
public class SignupMB {
	
	private SignupService signupService = new SignupService();
	
	private String username;
	private String email;
	private String password;
	private String confirmPassword;
	
	public void signUp() {
	
		if(!validateFields()) return;
		
		User newUser = null;
		
		try {
			newUser = signupService.signUp(username, email, password);
		} catch (SignUpException e) {
			GrowlUtils.addErrorMessage("Failed", "Something went wrong. Please contact the System Administrator.");		
		} catch (MailException e) {
			System.out.println("LOG: Failed to send mail to: "+ email);
		}
	    
		if(newUser != null) {
			try {
				loginNewUser(newUser);
				GrowlUtils.addInfoMessage("Success", "You've been signed up successfully. Welcome to Journal " + newUser.getUsername());
				redirectToDashboard();		
			} catch (LogInException le) {
				le.printStackTrace();
				GrowlUtils.addErrorMessage("Sorry", "We have a login problem after sign up. Please try to login through Login Page");
			}
		}
	}
	
	private boolean validateFields() {
		return checkRequiredFields() && checkContent();
	}
	
	private boolean checkRequiredFields() {
		if(this.username == null || this.username.isEmpty()) {
			GrowlUtils.addErrorMessage("Username", "The 'username' field is required");
			return false;
		}
		if(this.email == null || this.email.isEmpty()) {
			GrowlUtils.addErrorMessage("Email", "The 'email' field is required");
			return false;
		}
		if(this.password == null || this.password.isEmpty()) {
			GrowlUtils.addErrorMessage("Password", "The 'password' field is required");
			return false;
		}
		if(this.confirmPassword == null || this.confirmPassword.isEmpty()) {
			GrowlUtils.addErrorMessage("Confirm Password", "The 'confirm password' field is required");
			return false;
		}
		return true;
	}
 	
	private boolean checkContent() {
		boolean isAllowedUsername = signupService.validateUsername(username);
		boolean isAllowedEmail = signupService.validateEmail(email);
		boolean isEqualPasswords = signupService.isEqualPasswords(password, confirmPassword);
		boolean isAllowedPassword = signupService.validatePassword(password);
		
		if(!isAllowedUsername) GrowlUtils.addWarningMessage("Username", "Username already used!");
		if(!isAllowedEmail) GrowlUtils.addWarningMessage("Email", "Email already used or invalid!");
		if(!isEqualPasswords) GrowlUtils.addWarningMessage("Passwords", "Passwords are not matching!");
		if(!isAllowedPassword) GrowlUtils.addWarningMessage("Password", "Invalid password!");
		
		return isAllowedUsername && isAllowedEmail && isEqualPasswords && isAllowedPassword;
	}
	
	private void loginNewUser(User newUser) throws LogInException{
		try {
			SessionMB.getInstance().setSessionUser(newUser);
		} catch (Exception e) {
			throw new LogInException();
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
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
