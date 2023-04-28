package com.journal.controller.backingBean;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.PrimeFaces;
import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.journal.model.Label;
import com.journal.model.Post;
import com.journal.model.User;
import com.journal.service.LabelService;
import com.journal.service.PostService;
import com.journal.util.GrowlUtils;

@ManagedBean
@ViewScoped
public class PostDataScrollerBB {
	
	private PostService postService = new PostService();
	private LabelService labelService = new LabelService();
	
	private User user;
	private PostEditorBB postEditorBB;
	
	private String labelFilter;
	private LazyDataModel<Post> lsPosts;
	
	public PostDataScrollerBB (User user) {
		this.user = user;
		this.postEditorBB = new PostEditorBB(user, this);

		searchPosts(user, null);
	}
	
	private void searchPosts(User user, List<Label> labels) {
		
		lsPosts = new LazyDataModel<Post>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Post> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				
				List<Post> result = postService.findPosts(user, labels, first, pageSize);
					
				this.setRowCount(postService.countPosts(user, labels).intValue());

				return result;
			}
			
			@Override
			public int count(Map<String, FilterMeta> filterBy) {
				// TODO Auto-generated method stub
				return postService.countPosts(user, labels).intValue();
			}
		};
	}
	
	//TODO: use Label class
	public void updatePosts() {
		List<Label> labels = fillLabelListByName(labelFilter);

		searchPosts(user, labels);
		PrimeFaces.current().ajax().update("post-datascroller-form:postList");
	}
	
	public void updatePostEditor(Post selectedPost) {
		postEditorBB.updateSelectedPost(selectedPost);
	}
	
	public void removePost(Post post) {
		int affectedLines = 0;
		try {
			affectedLines += postService.removePost(post);
		} catch (Exception e) {
			e.printStackTrace();
			GrowlUtils.addErrorMessage("Failed", "Sorry :( The action failed");
		}
		
		if(affectedLines > 0) updatePosts();
	}
	
	private List<Label> fillLabelListByName(String labelName) {
		return (labelName != null) ?
				labelService.findByLabelName(labelFilter)
				: null;
	}

	public String getLabelFilter() {
		return labelFilter;
	}

	public void setLabelFilter(String labelFilter) {
		this.labelFilter = labelFilter;
	}

	public LazyDataModel<Post> getLsPosts() {
		return lsPosts;
	}

	public void setLsPosts(LazyDataModel<Post> lsPosts) {
		this.lsPosts = lsPosts;
	}

	public PostEditorBB getPostEditorBB() {
		return postEditorBB;
	}

	public void setPostEditorBB(PostEditorBB postEditorBB) {
		this.postEditorBB = postEditorBB;
	}
	
}
