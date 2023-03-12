package com.journal.controller;

import java.io.IOException;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;

import com.journal.dao.UserDAO;
import com.journal.model.User;

@ManagedBean
@ViewScoped
public class LoginMB {
	
	private String username;
	private String password;
	private UserDAO userDAO = new UserDAO();	
	
	
	public void login() {
		if(username == null || password == null) {
			return;
		}
		
		List<User> lUsers = userDAO.findByUsernameOrEmail(username);
		 
		try {
			if(lUsers != null && !lUsers.isEmpty()) {
				for(User user : lUsers) {
					if((user.getUsername().equals(username) || user.getEmail().equals(username)) && user.getPassword().equals(password)) {
						
						User sessionUser = SessionMB.getInstance().getSessionUser();
						
						if (sessionUser != null) {
							
							if (sessionUser.getEmail().equals(user.getEmail()) 
									&& sessionUser.getUsername().equals(user.getUsername())) {
								FacesContext.getCurrentInstance().getExternalContext().redirect("app/dashboard.xhtml");
							}
							
							SessionMB.getInstance().finishSession();
							FacesContext.getCurrentInstance().getExternalContext().redirect("login.xhtml");
						}
						
						SessionMB.getInstance().setSessionUser(user);
						System.out.println("User logged in: User: " + user.getUsername());
						FacesContext.getCurrentInstance().getExternalContext().redirect("app/dashboard.xhtml");
					} else {
						System.out.println("Username or Password is incorrect!");
					}					
				}
			} else {
				System.out.println("User not found");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
}
