package com.journal.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
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
	
	public FormForgotPasswordBB (String login) {
		this.login = login;
	}
	
	// THIS IS A TEMPORARY SOLUTION
	// TODO: Do a email sending a short life page' link using token which will expire in a short period.
	public void sendEmail() {
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
	
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
}
