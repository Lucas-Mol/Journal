package com.journal.service;

import com.journal.dao.UserDAO;
import com.journal.exception.AuthenticatorException;
import com.journal.exception.LogInException;
import com.journal.model.User;

public class LoginService {

	private UserDAO userDAO = new UserDAO();
	private UserAuthenticatorService userAuthService = new UserAuthenticatorService();

	public User login(String username, String password, User sessionUser) throws LogInException {

		User user = userDAO.findByUsernameOrEmail(username);

		try {
			userAuthService.authenticate(user, username, password);
				
			if (sessionUser == null || validateSessionUserWithLoggingUser(sessionUser, user)) {
				return user;
			}

			throw new LogInException("Error",
					"Already session was found! You've been log out, please try to log in again.");
		} catch (AuthenticatorException ae) {
			throw new LogInException(ae.growlTitle, ae.growlMessage);
		} catch (LogInException le) {
			throw new LogInException(le.growlTitle, le.growlMessage);
		}catch (Exception e) {
			e.printStackTrace();
			throw new LogInException("Failed!", "Something went wrong. Please contact the System Administrator.");
		}
	}

	private boolean validateSessionUserWithLoggingUser(User sessionUser, User loggingUser) {
			return sessionUser.getEmail().equals(loggingUser.getEmail())
					&& sessionUser.getUsername().toLowerCase().equals(loggingUser.getUsername().toLowerCase());
	}

}
