package com.journal.service;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.journal.dao.UserDAO;
import com.journal.exception.ChangePasswordException;
import com.journal.exception.MailException;
import com.journal.model.User;
import com.journal.util.PasswordUtils;

public class ChangePasswordService {

	private UserDAO userDAO = new UserDAO();
	
	public boolean changePassword(User user, String newPassword) throws ChangePasswordException, MailException {
		boolean success = false;

		if (user != null) {
			try {
				String newEncryptedPassword = PasswordUtils.encryptPassword(newPassword);

				user.setPassword(newEncryptedPassword);
				userDAO.edit(user);
				
				success = true;
			} catch (Exception e) {
				e.printStackTrace();
				throw new ChangePasswordException("Sorry",
						"We have a problem to change your password. Please contact the System Administrator");
			}
			
			try {
				// TODO: get the ServletContext to point image on email properly. Not a good
				// practice
				ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
						.getContext();

				MailService.sendChangePasswordEmail(user, context);
			} catch (Exception e) {
				e.printStackTrace();
				throw new MailException("Sorry",
						"Your password was changed successfully, although we have a unexpected issue to send you mail.");
			}
		}
		return success;
	}
	
	public boolean validateCurrentPassword(User user, String currentPassword) {
		User updatedUser = userDAO.find(user);
		return (updatedUser != null) ? 
				updatedUser.getPassword().equals(PasswordUtils.encryptPassword(currentPassword)) 
				: false;
	}
	
	public boolean isEqualPasswords(String password, String confirmPassword) {
		return PasswordUtils.isEqualPasswords(password, confirmPassword);
	}

	public boolean validatePassword(String password) {
		return PasswordUtils.validatePassword(password);
	}
}
