package com.journal.service;

import com.journal.exception.AuthenticatorException;
import com.journal.model.User;
import com.journal.util.PasswordUtils;

public class UserAuthenticatorService {

	public boolean authenticate(User user, String username, String password) throws AuthenticatorException{

		if (user != null) {
			return validateUserWithFields(user, username, password);
		} else {
			throw new AuthenticatorException("Incorrect", "Incorrect username or password!");
		}
	}

	private boolean validateUserWithFields(User user, String login, String password) throws AuthenticatorException {
		if((user.getUsername().equals(login) || user.getEmail().equals(login))
				&& user.getPassword().equals(PasswordUtils.encryptPassword(password))){
			return true;
		} else {
			throw new AuthenticatorException("Incorrect", "Incorrect username or password!");
		}
	}

}
