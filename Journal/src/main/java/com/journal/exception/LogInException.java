package com.journal.exception;

public class LogInException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public String growlTitle;
	
	public String growlMessage;
	
	public LogInException() {
		
	}
	
	public LogInException( String growlMessage) {
		this.growlTitle = "";
		this.growlMessage = growlMessage;
	}
	
	public LogInException(String growlTitle, String growlMessage) {
		this.growlTitle = growlTitle;
		this.growlMessage = growlMessage;
	}

}
