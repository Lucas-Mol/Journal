package com.journal.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.journal.model.User;

@ManagedBean
@ViewScoped
public class HeaderMB {
	
	@ManagedProperty(value = "#{sessionMB.sessionUser}")
	private User user;
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
