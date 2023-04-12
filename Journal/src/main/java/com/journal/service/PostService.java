package com.journal.service;

import java.util.Date;
import java.util.List;

import com.journal.dao.LabelDAO;
import com.journal.dao.PostDAO;
import com.journal.enumeration.ColorEnum;
import com.journal.model.Label;
import com.journal.model.Post;
import com.journal.model.User;

public class PostService {

	private PostDAO postDAO = new PostDAO();
	private LabelDAO labelDAO = new LabelDAO();

	public Post sendPost(User user, String postContent, String labelContent, Integer labelColor)  {
		if(user != null) {
			Label label = new Label();
			Post post = new Post();
			
			if(labelContent != null && !labelContent.isEmpty() && labelColor != null) {
				label.setName(labelContent);
				label.setColor(ColorEnum.getColorById(labelColor));
				
				//if label already persisted
				Label persistedLabel = labelDAO.findByNameColor(label);
				if(persistedLabel != null) label = persistedLabel;
			}
			
			post.setUser(user);
			post.setContent(postContent);
			if(label != null) post.setLabel(label);
			post.setLatestDate(new Date());
			
			return postDAO.insert(post);		
		}
		return null;
	}
	
	public List<Post> findPosts(User user, Label label, Integer first, Integer pageSize) {
		return postDAO.findByUserLabel(user, label, first, pageSize);
	}
	
	public List<Post> findByUserLabelName(User user, String labelName) {
		return postDAO.findByUserLabelName(user, labelName);
	}
	
	public Long countPosts(User user, Label label) {
		return postDAO.countPosts(user, label);
	}
	
	public Post editPost(Post post) {
		return postDAO.edit(post);
	}
	
	public int removePost(Post post) {
		List<Post> postsSameLabel = postDAO.findByLabel(post.getLabel());
		
		Integer affectedLines = postDAO.remove(post);
		postsSameLabel.remove(post);
		
		if(postsSameLabel.isEmpty()) {
			affectedLines += labelDAO.remove(post.getLabel());
		}
		
		return affectedLines;
	}
	
	public int removePostList(List<Post> posts) {
		Integer affectedLines = 0;
		for(Post post : posts) {
			affectedLines += removePost(post);	
		}
		
		return affectedLines;
	}
}
