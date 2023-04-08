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

	public void sendPost(User user, String postContent, String labelContent, Integer labelColor)  {
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
			
			postDAO.insert(post);		
		}
	}
	
	public List<Post> findPosts(User user, Label label, Integer first, Integer pageSize) {
		return postDAO.findByUserLabel(user, label, first, pageSize);
	}
	
	public Long countPosts(User user, Label label) {
		return postDAO.countPosts(user, label);
	}
}
