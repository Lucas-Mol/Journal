package com.journal.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.journal.dao.UserDAO;
import com.journal.exception.MailException;
import com.journal.exception.SignUpException;
import com.journal.model.User;
import com.journal.util.PasswordUtils;
import com.journal.util.StringUtils;

public class SignupService {

	private UserDAO userDAO = new UserDAO();
	
	public User signUp(String username, String email, String password) throws SignUpException, MailException {
		
		String encryptedPassword = PasswordUtils.encryptPassword(password);
		
		if(encryptedPassword == null || encryptedPassword == "") {
			throw new SignUpException();
		}
		
		User newUser = new User();
		newUser.setUsername(username);
		newUser.setEmail(email);
		newUser.setPassword(encryptedPassword);
		
		try {
			userDAO.insert(newUser);
		} catch(Exception e) {
			e.printStackTrace();
			throw new SignUpException();
		}
			
		try {
			//TODO: get the ServletContext to point image on email properly. Not a good practice
			ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();
			
			//Send email async
			ExecutorService asyncExe = Executors.newSingleThreadExecutor();
			asyncExe.submit(() -> {
				MailService.sendSignupEmail(newUser, context);
			});
			asyncExe.shutdown();
		} catch (Exception e) {
			e.printStackTrace();
			throw new MailException();
		}
		
		//getting persisted user with id
		User persistedUser = userDAO.findByUsernameOrEmail(newUser.getUsername());
	    return persistedUser;
	}
	
	public boolean validateUsername(String username) {
		return !userDAO.existUsername(username.trim()); //if it DOES exist then username's not allowed
	}
	
	public boolean validateEmail(String email) {
		return StringUtils.validateEmail(email) && !userDAO.existEmail(email.trim()); //if it DOES exist then email's not allowed
	}
	
	public boolean isEqualPasswords(String password, String confirmPassword) {
		return PasswordUtils.isEqualPasswords(password, confirmPassword);
	}
	
	public boolean validatePassword(String password) {
		return PasswordUtils.validatePassword(password);
	}
	
}
