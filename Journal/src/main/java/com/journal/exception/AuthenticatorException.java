package com.journal.exception;

public class AuthenticatorException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public String growlTitle;
	
	public String growlMessage;
	
	public AuthenticatorException() {
		
	}
	
	public AuthenticatorException( String growlMessage) {
		this.growlTitle = "";
		this.growlMessage = growlMessage;
	}
	
	public AuthenticatorException(String growlTitle, String growlMessage) {
		this.growlTitle = growlTitle;
		this.growlMessage = growlMessage;
	}

}
