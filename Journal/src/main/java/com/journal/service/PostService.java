package com.journal.service;

import java.util.List;

import com.journal.dao.PostDAO;
import com.journal.model.Label;
import com.journal.model.Post;
import com.journal.model.User;

public class PostService {

	private PostDAO postDAO = new PostDAO();
	private LabelService labelService;

	public Post sendPost(Post post)  {
		labelService = new LabelService();
		if(post.getLabel() != null)
			post.setLabel(labelService.insertLabel(post.getLabel()));
		
		return postDAO.insert(post);		
	}
	
	public List<Post> findPosts(User user, List<Label> labels, Integer first, Integer pageSize) {
		return postDAO.findByUserLabels(user, labels, first, pageSize);
	}
	
	public List<Post> findByUserLabelName(User user, String labelName) {
		return postDAO.findByUserLabelName(user, labelName);
	}
	
	public List<Post> findByLabel(Label label) {
		return postDAO.findByLabel(label);
	}
	
	public Long countPosts(User user, List<Label> labels) {
		return postDAO.countPosts(user, labels);
	}
	
	public Post editPost(Post post) {
		return postDAO.edit(post);
	}
	
	public int removePost(Post post) {
		labelService = new LabelService();
		Label usedLabel = post.getLabel();
		Integer affectedLines = postDAO.remove(post);
		
		affectedLines += labelService.removeIfNotUsed(usedLabel);
		
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
