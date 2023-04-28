package com.journal.service;

import java.util.List;

import com.journal.dao.LabelDAO;
import com.journal.model.Label;
import com.journal.model.Post;

public class LabelService {

	private LabelDAO labelDAO = new LabelDAO();
	private PostService postService;
	
	public Label insertLabel(Label label) {
		Label persistedLabel = verifyLabelAlreadyExist(label);
		return (persistedLabel == null) ? labelDAO.insert(label) : persistedLabel;
	}
	
	
	public int removeIfNotUsed(Label label) {
		int affectedLines = 0;
		postService = new PostService();
		
		List<Post> postsSameLabel = postService.findByLabel(label);
		
		if(postsSameLabel != null && postsSameLabel.isEmpty())
			affectedLines += labelDAO.remove(label);
		
		return affectedLines;
	}
	
	
	public Label verifyLabelAlreadyExist(Label label) {
		Label persistedLabel = labelDAO.findByNameColor(label.getName(), label.getColor());
		return persistedLabel;
	}

}
