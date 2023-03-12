package com.journal.controller;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.journal.model.User;

@ManagedBean
@ViewScoped
public class DashboardMB {

	@ManagedProperty(value = "#{sessionMB.sessionUser}")
	private User user;

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	public void logout() {
		try {
			SessionMB.getInstance().finishSession();
			FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
