package com.journal.controller.backingBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;

import com.journal.controller.managedBean.SessionMB;
import com.journal.exception.ChangePasswordException;
import com.journal.exception.MailException;
import com.journal.model.User;
import com.journal.service.ChangePasswordService;
import com.journal.util.GrowlUtils;

@ManagedBean
@ViewScoped
public class FormChangePasswordBB {
	
	private String currentPassword;
	private String newPassword;
	private String confirmNewPassword;
	
	private User user;
	private SessionMB session;
	private ChangePasswordService changePasswordService = new ChangePasswordService();
 	
	public FormChangePasswordBB () {
	}
	
	public FormChangePasswordBB (SessionMB session, User user) {
		this.session = session;
		this.setUser(user);
	}
	
	public void changePassword() {
		
		if(!validateFields()) return;
		
		boolean success = false;
		
		try {
			success = changePasswordService.changePassword(user, newPassword);
		} catch (ChangePasswordException cpe) {
			cpe.printStackTrace();
			GrowlUtils.addErrorMessage(cpe.growlTitle, cpe.growlMessage);
			return;
		} catch (MailException me) {
			me.printStackTrace();
			GrowlUtils.addWarningMessage(me.growlTitle, me.growlMessage);
		}
		
		if(success) {
			GrowlUtils.addInfoMessage("Success", "You've changed your password successfully");
			PrimeFaces.current().executeScript("PF('ChangePasswordDlg').hide();");
		}
		
	}
	
	private boolean validateFields() {
		return checkRequiredFields() && checkContent();
	}
	
	private boolean checkRequiredFields() {
		if(this.currentPassword == null || this.currentPassword.isEmpty()) {
			GrowlUtils.addErrorMessage("Current Password", "The 'Current Password' field is required");
			return false;
		}
		if(this.newPassword == null || this.newPassword.isEmpty()) {
			GrowlUtils.addErrorMessage("New Password", "The 'New Password' field is required");
			return false;
		}
		if(this.confirmNewPassword == null || this.confirmNewPassword.isEmpty()) {
			GrowlUtils.addErrorMessage("Confirm New Password", "The 'Confirm New Password' field is required");
			return false;
		}
		return true;
	}
 	
	private boolean checkContent() {
		boolean isCurrentPasswordCorrect = false;
		boolean isNewPasswordDifferentCurrentOne = false;
		boolean isEqualPasswords = changePasswordService.isEqualPasswords(newPassword, confirmNewPassword);
		boolean isAllowedNewPassword = changePasswordService.validatePassword(newPassword);
		
		if(!isEqualPasswords) GrowlUtils.addWarningMessage("New Passwords", "New Passwords are not matching!");
		if(!isAllowedNewPassword) GrowlUtils.addWarningMessage("New Password", "Invalid new password!");
		
		//Just check Current Password if others validations are true to avoid access DB unnecessarily
		if(isEqualPasswords && isAllowedNewPassword) {
			isCurrentPasswordCorrect = changePasswordService.validateCurrentPassword(user, currentPassword);
			if(!isCurrentPasswordCorrect) GrowlUtils.addWarningMessage("Current Password", "Current Password is not correct");	
		}
		
		//
		if(isCurrentPasswordCorrect) {
			isNewPasswordDifferentCurrentOne = !changePasswordService.isEqualPasswords(currentPassword, newPassword);
			if(!isNewPasswordDifferentCurrentOne) GrowlUtils.addErrorMessage("New Password", "New password can't be the same of current one.");
		}
		
		return isCurrentPasswordCorrect && isNewPasswordDifferentCurrentOne && isEqualPasswords && isAllowedNewPassword;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String userPassword) {
		this.currentPassword = userPassword;
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

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
}
