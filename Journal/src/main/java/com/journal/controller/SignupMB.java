package com.journal.controller;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.journal.dao.UserDAO;
import com.journal.model.User;
import com.journal.services.MailService;
import com.journal.util.GrowlUtils;
import com.journal.util.PasswordUtils;
import com.journal.util.StringUtils;

@ManagedBean
@ViewScoped
public class SignupMB {
	
	private String username;
	private String email;
	private String password;
	private String confirmPassword;
	private UserDAO userDAO = new UserDAO();
	
	public void singup() {
		boolean validatedFields = validateFields();
		
		if(!validatedFields) return;
		
		String encryptedPassword = PasswordUtils.encryptPassword(password);
		
		if(encryptedPassword == null || encryptedPassword == "") {
			GrowlUtils.addErrorMessage("Failed", "Something went wrong. Please contact the System Administrator.");
			return;
		}
		
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setEmail(email);
		newUser.setPassword(encryptedPassword);
		
		try {
			userDAO.insert(newUser);
		} catch(Exception e) {
			e.printStackTrace();
			GrowlUtils.addErrorMessage("Failed", "Something went wrong. Please contact the System Administrator.");
		}
		
		try {
			User loggedUser = userDAO.findByUsernameOrEmail(newUser.getEmail());
			SessionMB.getInstance().setSessionUser(loggedUser);
		} catch (Exception e) {
			e.printStackTrace();
			GrowlUtils.addErrorMessage("Sorry", "You had a login problem after sign up. Please try to login through Login Page");
		}
		
		
		ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
		
		ExecutorService asyncExe = Executors.newSingleThreadExecutor();
		asyncExe.submit(() -> {
	    	MailService.sendSignupEmail(newUser, context);
	    });
		asyncExe.shutdown();
	    
		GrowlUtils.addInfoMessage("Success", "You've been signed up successfully. Welcome to Journal");
		redirectToDashboard();
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
		boolean isAllowedUsername = !userDAO.existUsername(username.trim()); //if it DOES exist then username's not allowed
		boolean isAllowedEmail = StringUtils.validateEmail(email) && !userDAO.existEmail(email.trim());
		boolean isEqualPasswords = PasswordUtils.isEqualPasswords(password, confirmPassword);
		boolean isAllowedPassword = PasswordUtils.validatePassword(password);
		
		if(!isAllowedUsername) GrowlUtils.addWarningMessage("Username", "Username already used!");
		if(!isAllowedEmail) GrowlUtils.addWarningMessage("Email", "Email already used or invalid!");
		if(!isEqualPasswords) GrowlUtils.addWarningMessage("Passwords", "Passwords are not matching!");
		if(!isAllowedPassword) GrowlUtils.addWarningMessage("Password", "Invalid password!");
		
		return isAllowedUsername && isAllowedEmail && isEqualPasswords && isAllowedPassword;
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
