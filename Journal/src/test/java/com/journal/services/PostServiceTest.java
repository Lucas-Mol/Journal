package com.journal.services;


import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.journal.dao.UserDAO;
import com.journal.enumeration.ColorEnum;
import com.journal.model.Label;
import com.journal.model.Post;
import com.journal.model.User;
import com.journal.service.PostService;

@TestInstance(Lifecycle.PER_CLASS)
public class PostServiceTest {
	
	PostService postService = new PostService();
	UserDAO userDAO = new UserDAO();
	User user = userDAO.findByUsernameOrEmail("test01");
	
	
	// Constants
	final String POST_CONTENT_TO_NEW_LABEL = "Testing to send post from JUnit test";
	final String POST_CONTENT_TO_EXISTING_LABEL = "Testing to send post with already existing label from JUnit test";
	final String POST_CONTENT_TO_BE_LISTED = "Testing to send post to be listed from JUnit test";
	final String POST_CONTENT_NO_EDITED = "Testing new post to edit from JUnit test";
	final String POST_CONTENT_EDITED = "Testing already edited post from JUnit test";
	final String POST_CONTENT_TO_REMOVE = "Testing new post to remove from JUnit test";
	
	String LABEL_NAME = "JUnit_";
	String NEW_LABEL_NAME = LABEL_NAME + "SendPostNewLabel";
	String PERSISTING_LABEL_NAME = LABEL_NAME + "SendPostExistingLabel";
	String LISTED_LABEL_NAME = LABEL_NAME + "FindPostsNoLabel";
	String EDITED_LABEL_NAME = LABEL_NAME + "EditPost";
	String REMOVED_LABEL_NAME = LABEL_NAME + "RemovePost";
	
	final String CURRENT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDate.now());

	final ColorEnum COLOR_ENUM = ColorEnum.RED;
	
	 @BeforeAll
	    public void setupLabelNames() {
			PERSISTING_LABEL_NAME += CURRENT_DATE;
			LISTED_LABEL_NAME += CURRENT_DATE;
			EDITED_LABEL_NAME += CURRENT_DATE;
			REMOVED_LABEL_NAME += CURRENT_DATE;
	    }

	@Test
	public void testSendPostNewLabel() {
		Label newLabel = new Label(NEW_LABEL_NAME, COLOR_ENUM);
		Post sentPost = postService.sendPost(new Post(POST_CONTENT_TO_NEW_LABEL, user, newLabel));

		assertTrue(sentPost != null 
				&& sentPost.getContent().equals(POST_CONTENT_TO_NEW_LABEL)
				&& sentPost.getLabel().getName().equals(NEW_LABEL_NAME)
				&& sentPost.getLabel().getColor().getId().equals(COLOR_ENUM.getId()));
	}
	
	@Test
	public void testSendPostExistingLabel() {
		Label newLabel = new Label(PERSISTING_LABEL_NAME, COLOR_ENUM);
		Post postNewLabel = postService.sendPost(new Post(POST_CONTENT_TO_NEW_LABEL, user, newLabel));
		
		Post resultPostWithPersistedLabel = 
				postService.sendPost(new Post(POST_CONTENT_TO_EXISTING_LABEL, user, postNewLabel.getLabel()));

		assertTrue(postNewLabel != null 
				&& resultPostWithPersistedLabel != null
				&& postNewLabel.getLabel().equals(resultPostWithPersistedLabel.getLabel()));
	}
	
	@Test
	public void testFindPostsNoLabel() {
		Label label = new Label(LISTED_LABEL_NAME, COLOR_ENUM);
		Post resultPost = postService.sendPost(new Post(POST_CONTENT_TO_BE_LISTED, user, label));
		
		List<Post> listedPosts = postService.findPosts(user, null, null, null);
		
		assertTrue(listedPosts != null
				&& !listedPosts.isEmpty()
				&& listedPosts.contains(resultPost));
	}
	
	@Test
	public void testEditPost() {
		Label label = new Label(EDITED_LABEL_NAME, COLOR_ENUM);
		Post resultPost = postService.sendPost(new Post(POST_CONTENT_NO_EDITED, user, label));
		
		resultPost.setContent(POST_CONTENT_EDITED);
		
		Post editedPost = postService.editPost(resultPost);
		
		assertTrue(editedPost != null 
				&& editedPost.getContent().equals(POST_CONTENT_EDITED));
	}
	
	@Test
	public void testRemovePost() {	
		Label label = new Label(REMOVED_LABEL_NAME, COLOR_ENUM);
		Post resutlPost = postService.sendPost(new Post(POST_CONTENT_TO_REMOVE, user, label));
		
		Integer affectedLines = postService.removePost(resutlPost);
		
		// remove post and label if no other post references it
		assertTrue(affectedLines == 2);
	}
	
	@AfterAll
    public void cleanDB() {
        List<Post> posts = postService.findByUserLabelName(user, LABEL_NAME);
        
        postService.removePostList(posts);
    }

}
