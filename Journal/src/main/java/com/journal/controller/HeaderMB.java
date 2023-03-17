package com.journal.controller;

import java.util.Random;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.journal.enumeration.ColorEnum;
import com.journal.model.User;

@ManagedBean
@ViewScoped
public class HeaderMB {
	
	@ManagedProperty(value = "#{sessionMB.sessionUser}")
	private User user;
	
	public String getUsernameInitials() {
		String initials = "";
		
		if(user != null) {

			for (String text : user.getUsername().split(" ")) {
				initials += (text.length() > 2) ? text.substring(0, 1) : "";
			}
			
			initials = (initials.length() > 2) ? initials.substring(0, 2).toUpperCase() : initials.toUpperCase();
		}
		
		return initials;
	}
	
	public String randomizeHexColor() {
		Random random = new Random();
		ColorEnum[] colorEnumArray = ColorEnum.values();
		
		int randomIndex = random.nextInt(colorEnumArray.length);
		
		return colorEnumArray[randomIndex].getColor();
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
