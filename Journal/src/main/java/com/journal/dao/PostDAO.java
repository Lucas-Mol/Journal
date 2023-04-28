package com.journal.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;

import com.journal.model.Label;
import com.journal.model.Label_;
import com.journal.model.Post;
import com.journal.model.Post_;
import com.journal.model.User;

public class PostDAO extends ObjectDAO<Post>{

	public PostDAO() {
		setClass(Post.class);
	}
	
	public List<Post> findByUser(User user) {
	    return findByUserLabels(user, null, null, null);
	}
	
	public List<Post> findByUserLabels(User user, List<Label> labels) {
	    return findByUserLabels(user, labels, null, null);
	}
	
	public List<Post> findByUserLabels(User user, List<Label> labels, Integer first, Integer pageSize) {
		initializeDefaultManagers();
		
		try {
			Predicate restrictions = (labels != null && !labels.isEmpty()) 
									 ? builder.and(filterByUser(user), filterByListLabel(labels)) 
									 : filterByUser(user);
			
			query.where(restrictions).distinct(true);
			Order orderByLatestDateDesc = orderDesc(Post_.LATEST_DATE);
			TypedQuery<Post> tq = manager.createQuery(query.orderBy(orderByLatestDateDesc));
			paginizeTypedQuery(tq, first, pageSize);

			return tq.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Post>();
		} finally {
			tearDownManagers();
		}
	}
	
	public Long countPosts(User user, List<Label> labels) {
		initializeCounterManagers();

		try {
			Predicate restrictions = (labels != null && !labels.isEmpty()) 
					? builder.and(filterByUser(user), filterByListLabel(labels))
					: filterByUser(user);

			counter.where(restrictions).distinct(true);
			counter.select(builder.countDistinct(root));

			return manager.createQuery(counter).getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			tearDownManagers();
		}
	}
	
	public List<Post> findByLabel(Label label) {
		initializeDefaultManagers();

		try {
			query.where(filterByLabel(label));
			TypedQuery<Post> tq = manager.createQuery(query);
			return tq.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Post>();
		} finally {
			tearDownManagers();
		}
	}
	
	public List<Post> findByUserLabelName(User user, String labelName) {
		initializeDefaultManagers();

		try {
			query.where(builder.and(filterByUser(user), filterByLabelName(labelName)));
			TypedQuery<Post> tq = manager.createQuery(query);
			return tq.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Post>();
		} finally {
			tearDownManagers();
		}
	}
	
	public List<Post> findByContent(String postContent) {
		initializeDefaultManagers();

		try {
			query.where(builder.equal(root.get(Post_.CONTENT), postContent));
			TypedQuery<Post> tq = manager.createQuery(query);
			return tq.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<Post>();
		} finally {
			tearDownManagers();
		}
	}
	
	public Post insert(Post post) {
		EntityTransaction transaction = initializeTransaction();
		post = manager.merge(post);
				
		try {
			transaction.begin();
			manager.persist(post);
			transaction.commit();
			
			return post;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			tearDownManagers();
		}
	}
	
	public Post edit(Post post) {
		EntityTransaction transaction = initializeTransaction();
				
		try {
			transaction.begin();
			post = manager.merge(post);
			transaction.commit();
			
			return post;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			tearDownManagers();
		}
	}
	
	public int remove(Post post) {
		initializeDefaultManagers();
		EntityTransaction transaction = manager.getTransaction();
		CriteriaDelete<Post> deleteQuery = initializeDeleteQuery();
		
		try {
			transaction.begin();
			deleteQuery.where(builder.equal(root.get(Post_.ID), post.getId()));
			Query query = manager.createQuery(deleteQuery);
			int affectedLines = query.executeUpdate();
			transaction.commit();
			
			return affectedLines;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			tearDownManagers();
		}
	}
	
	public int removeList(List<Post> posts) {
		int affectedLines = 0;
		
		for(Post post : posts)
			remove(post);

		return affectedLines;
	}
	
	private Predicate filterByUser(User user) {
		return builder.equal(root.get(Post_.USER), user.getId());
	}
	
	private Predicate filterByLabel(Label label) {
		return builder.equal(root.get(Post_.LABEL), label);
	}
	
	private Predicate filterByListLabel(List<Label> labels) {
		return root.get(Post_.LABEL).in(labels);
	}
	
	private Predicate filterByLabelName(String labelName) {
		Join<Post, Label> joinLabel = root.join(Post_.LABEL);
		return builder.like(joinLabel.get(Label_.NAME), "%" + labelName + "%");
	}
}
