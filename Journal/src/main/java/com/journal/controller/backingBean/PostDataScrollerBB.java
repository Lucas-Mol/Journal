package com.journal.controller.backingBean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.journal.enumeration.ColorEnum;
import com.journal.model.User;
import com.journal.service.PostService;
import com.journal.util.GrowlUtils;

@ManagedBean
@ViewScoped
public class PostDataScrollerBB {
	
	private PostService postService = new PostService();
	
	private User user;
	
	public PostDataScrollerBB (User user) {
		this.user = user;

	}
	

	
}
