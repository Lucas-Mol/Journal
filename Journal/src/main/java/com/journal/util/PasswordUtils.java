package com.journal.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class PasswordUtils {

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
	
	public static String generateNewPassword() {
		String allowedCharacters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789#_!@%$";
		int passwordLenght = 19;
		String newPassword = "abcd";
		
		try {
			SecureRandom sRandom = new SecureRandom();
			newPassword =  IntStream.range(0, passwordLenght)
			            .mapToObj(i -> allowedCharacters.charAt(sRandom.nextInt(allowedCharacters.length())))
			            .map(Object::toString)
			            .collect(Collectors.joining());
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return newPassword;
	}

}
