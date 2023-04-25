package com.journal.controller.backingBean;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.journal.enumeration.ColorEnum;
import com.journal.model.Label;
import com.journal.model.Post;
import com.journal.model.User;
import com.journal.service.PostService;
import com.journal.util.GrowlUtils;


@ManagedBean
@ViewScoped
public class PostEditorBB {
	
	private PostService postService = new PostService();
	
	private User user;
	private PostDataScrollerBB postDataScrollerBB;
	
	private String postContent;
	private String labelContent;
	private Integer labelColor;
	
	private List<ColorEnum> lsColorEnum;
	
	public PostEditorBB (User user, PostDataScrollerBB postDataScrollerBB) {
		this.user = user;
		this.postDataScrollerBB = postDataScrollerBB;
		
		setLsColorEnum(Arrays.stream(ColorEnum.values())
                .collect(Collectors.toList()));
	}
	
	public void sendPost() {
		
		if(!validateFields()) return; 
		
		try {
			Label label = new Label(labelContent, ColorEnum.getColorById(labelColor));
			Post post = new Post(postContent, user, label);
			postService.sendPost(post);
			
			postDataScrollerBB.updatePosts();
		} catch (Exception e) {
			e.printStackTrace();
			GrowlUtils.addErrorMessage("Send Post Error", "We have a problem to send your post. Please contact the System Administrator.");
		}
		
	}
	
	private boolean validateFields() {
		if(this.postContent == null || this.postContent.isEmpty()) {
			GrowlUtils.addErrorMessage("Post text", "The 'Post text' field is required");
			return false;
		}
		if((this.labelContent != null && !this.labelContent.isEmpty())
				&& this.labelColor == null) {
			GrowlUtils.addErrorMessage("Label Color", "The 'Label Color' field is required if 'Label' field was filled");
			return false;
		}
		if(this.labelColor != null &&
				(this.labelContent == null || this.labelContent.isEmpty())) {
			GrowlUtils.addErrorMessage("Label", "The 'Label' field is required if 'Label Color' field was filled");
			return false;
		}
		return true;
	}

	public String getPostContent() {
		return postContent;
	}

	public void setPostContent(String postContent) {
		this.postContent = postContent;
	}

	public String getLabelContent() {
		return labelContent;
	}

	public void setLabelContent(String labelContent) {
		this.labelContent = labelContent;
	}

	public Integer getLabelColor() {
		return labelColor;
	}

	public void setLabelColor(Integer labelColor) {
		this.labelColor = labelColor;
	}

	public List<ColorEnum> getLsColorEnum() {
		return lsColorEnum;
	}

	public void setLsColorEnum(List<ColorEnum> lsColorEnum) {
		this.lsColorEnum = lsColorEnum;
	}
	
	
}
