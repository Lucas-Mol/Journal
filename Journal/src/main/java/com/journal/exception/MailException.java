package com.journal.exception;

public class MailException extends Exception{

	private static final long serialVersionUID = 1L;
	
	public String growlTitle;
	
	public String growlMessage;
	
	public MailException() {
		
	}
	
	public MailException( String growlMessage) {
		this.growlTitle = "";
		this.growlMessage = growlMessage;
	}
	
	public MailException(String growlTitle, String growlMessage) {
		this.growlTitle = growlTitle;
		this.growlMessage = growlMessage;
	}

}
