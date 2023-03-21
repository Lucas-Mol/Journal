package com.journal.exception;

public class ChangePasswordException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public String growlTitle;
	
	public String growlMessage;
	
	public ChangePasswordException() {
		
	}
	
	public ChangePasswordException( String growlMessage) {
		this.growlTitle = "";
		this.growlMessage = growlMessage;
	}
	
	public ChangePasswordException(String growlTitle, String growlMessage) {
		this.growlTitle = growlTitle;
		this.growlMessage = growlMessage;
	}

}
