package com.journal.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

@ManagedBean
@ViewScoped
public class FormChangePasswordBB {
	
	private String userPassword;
	private String newPassword;
	private String confirmNewPassword;
	
	private SessionMB session;
	
	public FormChangePasswordBB () {
	}
	
	public FormChangePasswordBB (SessionMB session) {
		this.session = session;
	}
	
	

	public String getUserPassword() {
		return userPassword;
	}

	public void setUserPassword(String userPassword) {
		this.userPassword = userPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmNewPassword() {
		return confirmNewPassword;
	}

	public void setConfirmNewPassword(String confirmNewPassword) {
		this.confirmNewPassword = confirmNewPassword;
	}

	public SessionMB getSession() {
		return session;
	}

	public void setSession(SessionMB session) {
		this.session = session;
	}
	
	
}
