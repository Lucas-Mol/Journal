package com.journal.controller.managedBean;

import java.io.IOException;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.journal.controller.backingBean.FormChangePasswordBB;
import com.journal.model.User;

@ManagedBean
@ViewScoped
public class ProfileMB {

	@ManagedProperty(value = "#{sessionMB.sessionUser}")
	private User user;
	
	SessionMB session = SessionMB.getInstance();
	private FormChangePasswordBB formChangePasswordBB;
	
	public void init() {
		formChangePasswordBB = new FormChangePasswordBB(session, user);
	}

	public void logout() {
		try {
			SessionMB.getInstance().finishSession();
			FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public FormChangePasswordBB getFormChangePasswordBB() {
		return formChangePasswordBB;
	}

	public void setFormChangePasswordBB(FormChangePasswordBB formChangePasswordBB) {
		this.formChangePasswordBB = formChangePasswordBB;
	}
	

}
