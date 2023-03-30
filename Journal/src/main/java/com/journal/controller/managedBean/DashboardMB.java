package com.journal.controller.managedBean;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;

import com.journal.controller.backingBean.PostDataScrollerBB;
import com.journal.controller.backingBean.PostEditorBB;
import com.journal.model.User;

@ManagedBean
@ViewScoped
public class DashboardMB {

	@ManagedProperty(value = "#{sessionMB.sessionUser}")
	private User user;
	
	private PostEditorBB postEditorBB;
	private PostDataScrollerBB postDataScrollerBB;
	
	public void init() {
		postEditorBB = new PostEditorBB(user);
		postDataScrollerBB = new PostDataScrollerBB(user);
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public PostEditorBB getPostEditorBB() {
		return postEditorBB;
	}

	public void setPostEditorBB(PostEditorBB postEditorBB) {
		this.postEditorBB = postEditorBB;
	}

	public PostDataScrollerBB getPostDataScrollerBB() {
		return postDataScrollerBB;
	}

	public void setPostDataScrollerBB(PostDataScrollerBB postDataScrollerBB) {
		this.postDataScrollerBB = postDataScrollerBB;
	}

	
}
