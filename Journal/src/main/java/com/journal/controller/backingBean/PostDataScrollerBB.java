package com.journal.controller.backingBean;

import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.primefaces.model.FilterMeta;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.SortMeta;

import com.journal.enumeration.ColorEnum;
import com.journal.model.Label;
import com.journal.model.Post;
import com.journal.model.User;
import com.journal.service.PostService;

@ManagedBean
@ViewScoped
public class PostDataScrollerBB {
	
	private PostService postService = new PostService();
	
	private User user;
	private String labelFilter;
	private LazyDataModel<Post> lsPosts;
	
	public PostDataScrollerBB (User user) {
		this.user = user;

		searchPosts(user, null);
	}
	
	private void searchPosts(User user, Label label) {
		
		lsPosts = new LazyDataModel<Post>() {
			private static final long serialVersionUID = 1L;
			
			@Override
			public List<Post> load(int first, int pageSize, Map<String, SortMeta> sortBy,
					Map<String, FilterMeta> filterBy) {
				
				List<Post> result = postService.findPosts(user, label, first, pageSize);
					
				this.setRowCount(postService.countPosts(user, label).intValue());

				return result;
			}
			
			@Override
			public int count(Map<String, FilterMeta> filterBy) {
				// TODO Auto-generated method stub
				return postService.countPosts(user, label).intValue();
			}
		};
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
	

	
}
