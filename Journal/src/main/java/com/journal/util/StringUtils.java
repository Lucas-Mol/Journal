package com.journal.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Pattern;

import com.journal.dao.UserDAO;

public class StringUtils {
	
	
	public static boolean validateUsername (String username) {
		UserDAO userDAO = new UserDAO();
		
		//if it DOES exist a user with this username then return false
		return !userDAO.existUsername(username.trim());
	}
	
	public static boolean validateEmail (String email) {
		UserDAO userDAO = new UserDAO();
		String emailPattern = "^(.+)@(\\S+)$";
		boolean emailMatchPattern = false;
		
		try {
			emailMatchPattern = Pattern.compile(emailPattern)
										.matcher(email.trim())
										.matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		//if it DOES exist a user with this email or wrong pattern then return false
		return emailMatchPattern && !userDAO.existEmail(email.trim());
	}

	public static boolean isEqualPasswords(String password, String confirmPassword) {
		
		return password.equals(confirmPassword);
	}

	public static boolean validatePassword(String password) {
		Integer minLength = 6;
		Integer maxLength = 20;
		String acceptableRegex = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[#_!@%$])[a-zA-Z\\d#_!@%&$]+$";
		boolean result = false;
		
		try {
			result = password.trim().length() > minLength && password.trim().length() < maxLength
					&& password.trim().matches(acceptableRegex);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return result;
	}
	
	public static String encryptPassword(String password) {
		String encryptedPassword = "";
		try {
			MessageDigest algorithm = MessageDigest.getInstance("SHA-256");
			byte messageDigestSenhaAdmin[] = algorithm.digest(password.getBytes("UTF-8"));

			StringBuilder hexPassword = new StringBuilder();
			for (byte b : messageDigestSenhaAdmin) {
				hexPassword.append(String.format("%02X", 0xFF & b));
			}

			encryptedPassword = hexPassword.toString();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return encryptedPassword;
	}

}
