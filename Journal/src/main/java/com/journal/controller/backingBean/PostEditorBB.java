package com.journal.controller.backingBean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.journal.enumeration.ColorEnum;
import com.journal.model.User;

@ManagedBean
@ViewScoped
public class PostEditorBB {
	
	private User user;
	private String postContent;
	private String labelContent;
	private int labelColor;
	
	private List<ColorEnum> lsColorEnum;
	
	public PostEditorBB (User user) {
		this.user = user;
		
		setLsColorEnum(Arrays.stream(ColorEnum.values())
                .collect(Collectors.toList()));
	}
	
	public void sendPost() {
		
		if(!validateFields()) return; 
		
		
	}
	
	private boolean validateFields() {
		if(this.postContent == null || this.postContent.isEmpty()) {
			GrowlUtils.addErrorMessage("Post text", "The 'Post text' field is required");
			return false;
		}
		if((this.labelContent != null && !this.labelContent.isEmpty())
				&& this.labelColor == 0) {
			GrowlUtils.addErrorMessage("Label Color", "The 'Label Color' field is required if 'Label' field was filled");
			return false;
		}
		if(this.labelColor != 0 &&
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

	public int getLabelColor() {
		return labelColor;
	}

	public void setLabelColor(int labelColor) {
		this.labelColor = labelColor;
	}

	public List<ColorEnum> getLsColorEnum() {
		return lsColorEnum;
	}

	public void setLsColorEnum(List<ColorEnum> lsColorEnum) {
		this.lsColorEnum = lsColorEnum;
	}
	
	
}
