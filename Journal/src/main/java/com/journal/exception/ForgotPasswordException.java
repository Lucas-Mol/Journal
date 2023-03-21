package com.journal.exception;

public class ForgotPasswordException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public String growlTitle;
	
	public String growlMessage;
	
	public ForgotPasswordException() {
		
	}
	
	public ForgotPasswordException( String growlMessage) {
		this.growlTitle = "";
		this.growlMessage = growlMessage;
	}
	
	public ForgotPasswordException(String growlTitle, String growlMessage) {
		this.growlTitle = growlTitle;
		this.growlMessage = growlMessage;
	}

}
