package com.journal.util;

import javax.faces.application.FacesMessage;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@RequestScoped
public class GrowlUtils {
	
	private static void addMessage(FacesMessage.Severity severity, String title, String message) {
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, title, message));
        FacesContext.getCurrentInstance().getExternalContext().getFlash().setKeepMessages(true);
    }
	
	public static void addInfoMessage(String message) {
		addInfoMessage("", message);
	}
	
	public static void addInfoMessage(String title, String message) {
		addMessage(FacesMessage.SEVERITY_INFO, title,message);
	}
	
	public static void addWarningMessage(String message) {
		addWarningMessage("", message);
	}
	
	public static void addWarningMessage(String title, String message) {
		addMessage(FacesMessage.SEVERITY_WARN, title,message);
	}
	
	public static void addErrorMessage(String message) {
		addErrorMessage("", message);
	}
	
	public static void addErrorMessage(String title, String message) {
		addMessage(FacesMessage.SEVERITY_ERROR, title,message);
	}
}
