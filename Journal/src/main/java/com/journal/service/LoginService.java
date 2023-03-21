package com.journal.service;

import com.journal.dao.UserDAO;
import com.journal.exception.LogInException;
import com.journal.model.User;
import com.journal.util.PasswordUtils;

public class LoginService {

	private UserDAO userDAO = new UserDAO();

	public User login(String username, String password, User sessionUser) throws LogInException {

		User user = userDAO.findByUsernameOrEmail(username);

		try {
			if (user != null && validateUserWithFields(user, username, password)) {

				if (sessionUser != null) {

					if (validateSessionUserWithLoggingUser(sessionUser, user)) {
						return user;
					}

					throw new LogInException("Error",
							"Already session was found! You've been log out, please try to log in again.");
				}
				return user;
			} else {
				throw new LogInException("Incorrect", "Incorrect username or password!");
			}
		} catch (LogInException le) {
			throw new LogInException(le.growlTitle, le.growlMessage);
		}catch (Exception e) {
			e.printStackTrace();
			throw new LogInException("Failed!", "Something went wrong. Please contact the System Administrator.");
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

}
