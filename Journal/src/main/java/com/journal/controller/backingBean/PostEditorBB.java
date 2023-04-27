package com.journal.controller.backingBean;

import java.util.Arrays;
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
	
	private Post selectedPost;
	
	public PostEditorBB (User user, PostDataScrollerBB postDataScrollerBB) {
		this.user = user;
		this.postDataScrollerBB = postDataScrollerBB;
		
		setLsColorEnum(Arrays.stream(ColorEnum.values())
                .collect(Collectors.toList()));
	}
	
	public void sendPost() {
		
		if(!validateFields()) return; 
		
		try {
			Label label = null;
			if(labelContent != null && labelColor != null)
				label = new Label(labelContent, ColorEnum.getColorById(labelColor));

			Post post = new Post(postContent, user, label);
			postService.sendPost(post);
			
			postDataScrollerBB.updatePosts();
		} catch (Exception e) {
			e.printStackTrace();
			GrowlUtils.addErrorMessage("Send Post Error", "We have a problem to send your post. Please contact the System Administrator.");
		}
		
	}
	
	public void editPost() {
		
		if(!validateFields()) return; 
		
		try {
			if(selectedPost != null) {
				Label label = null;
				if(labelContent != null && labelColor != null)
					label = new Label(labelContent, ColorEnum.getColorById(labelColor));

				selectedPost.setContent(postContent);
				selectedPost.setUser(user);
				selectedPost.setLabel(label);
				postService.editPost(selectedPost);	
				
				postDataScrollerBB.updatePosts();	
			} else {
				throw new Exception();
			}	
		} catch (Exception e) {
			e.printStackTrace();
			GrowlUtils.addErrorMessage("Edit Post Error", "We have a problem to edit your post. Please contact the System Administrator.");
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
	
	public void updateSelectedPost(Post selectedPost) {
		if(selectedPost != null) {
			this.selectedPost = selectedPost;
			updateFields(selectedPost);
		}
	}

	private void updateFields(Post selectedPost) {
		this.setPostContent(selectedPost.getContent());
		if(selectedPost.getLabel() != null) {
			this.setLabelContent(selectedPost.getLabel().getName());
			this.setLabelColor(selectedPost.getLabel().getColor().getId());
		} else {
			this.setLabelContent(null);
			this.setLabelColor(null);
		}
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

	public Post getSelectedPost() {
		return selectedPost;
	}

	public void setSelectedPost(Post selectedPost) {
		this.selectedPost = selectedPost;
	}
	
	
}
