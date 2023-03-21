package com.journal.service;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.journal.dao.UserDAO;
import com.journal.exception.ForgotPasswordException;
import com.journal.model.User;
import com.journal.util.PasswordUtils;

public class ForgotPasswordService {

	private UserDAO userDAO = new UserDAO();
	
	// THIS IS A TEMPORARY SOLUTION
	// TODO: Do a email sending a short life page' link using token which will expire in a short period.
	public boolean sendNewPassword(String login) throws ForgotPasswordException {

		if (userDAO.existUsername(login) || userDAO.existEmail(login)) {
			User user = userDAO.findByUsernameOrEmail(login);

			try {
				String newPassword = PasswordUtils.generateNewPassword();
				String newEncryptedPassword = PasswordUtils.encryptPassword(newPassword);

				user.setPassword(newEncryptedPassword);
				userDAO.edit(user);

				// TODO: get the ServletContext to point image on email properly. Not a good
				// practice
				ServletContext context = (ServletContext) FacesContext.getCurrentInstance().getExternalContext()
						.getContext();

				MailService.sendForgotPasswordEmail(user, newPassword, context);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
				throw new ForgotPasswordException("Sorry",
						"We have a problem to re-generate your password. Please contact the System Administrator");
			}
		} else {
			throw new ForgotPasswordException("Username/Email not found", "This Username/Email wasn't found on Journal");
		}
	}

}
