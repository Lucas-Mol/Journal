package com.journal.util;

import java.util.regex.Pattern;

public class StringUtils {
	
	public static boolean validateEmail (String email) {
		String emailPattern = "^(.+)@(\\S+)$";
		boolean emailMatchPattern = false;
		
		try {
			emailMatchPattern = Pattern.compile(emailPattern)
										.matcher(email.trim())
										.matches();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return emailMatchPattern;
	}

}
