package com.journal.service;

import java.util.Date;

import com.journal.dao.PostDAO;
import com.journal.model.Label;
import com.journal.model.Post;
import com.journal.model.User;

public class PostService {

	private PostDAO postDAO = new PostDAO();

	public void sendPost(User user, String postContent, String labelContent, int labelColor)  {
		if(user != null) {
			Label label = new Label();
			Post post = new Post();
			
			if(labelContent != null && !labelContent.isEmpty() && labelColor != 0) {
				label.setName(labelContent);
				label.setColor(labelColor);
			}
			
			post.setUser(user);
			post.setContent(postContent);
			if(label != null) post.setLabel(label);
			post.setLatestDate(new Date());
			
			postDAO.insert(post);		
		}
	}


}
