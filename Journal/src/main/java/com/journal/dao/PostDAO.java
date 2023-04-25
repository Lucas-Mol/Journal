package com.journal.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.journal.model.Label;
import com.journal.model.Label_;
import com.journal.model.Post;
import com.journal.model.Post_;
import com.journal.model.User;

public class PostDAO {

	private ConnectionFactory connectionFactory;
	private EntityManager manager;
	
	private CriteriaBuilder builder;
	private CriteriaQuery<Post> query;
	private CriteriaQuery<Long> count;
	private Root<Post> rootPost;
	
	private void initializeDefaultManagers() {
		connectionFactory = new ConnectionFactory();
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Post.class);
		rootPost = query.from(Post.class);
	}
	
	private void intializeCountManagers() {
		connectionFactory = new ConnectionFactory();
        manager = connectionFactory.getEntityManager();
        builder = manager.getCriteriaBuilder();
        count = builder.createQuery(Long.class);
        rootPost = count.from(Post.class);
	}
	
	private CriteriaDelete<Post> initializeDeleteQuery() {
		CriteriaDelete<Post> deleteQuery = builder.createCriteriaDelete(Post.class);
		rootPost = deleteQuery.from(Post.class);
		return deleteQuery;
	}
	
	private EntityTransaction initializeTransaction() {
		connectionFactory = new ConnectionFactory();
		manager = connectionFactory.getEntityManager();
		return manager.getTransaction();
	}
	
	public List<Post> findByUser(User user) {
	    return findByUserLabel(user, null, null, null);
	}
	
	public List<Post> findByUserLabel(User user, Label label) {
	    return findByUserLabel(user, label, null, null);
	}
	
	public List<Post> findByUserLabel(User user, Label label, Integer first, Integer pageSize) {
		initializeDefaultManagers();
		
		Predicate restrictions = null;
		if(label != null)
			restrictions = builder.and(filterByUser(user), filterByLabel(label));
		else
			restrictions = filterByUser(user);
		
		query.where(restrictions).distinct(true);
		
		TypedQuery<Post> tq = manager.createQuery(query.orderBy(orderDesc()));

		if (first != null)
			tq.setFirstResult(first);
		if (pageSize != null)
			tq.setMaxResults(pageSize);

		List<Post> resultList = tq.getResultList();
		manager.close();
		connectionFactory.close();
		if (!resultList.isEmpty()) return resultList;
		else return new ArrayList<Post>();
	}
	
	public Long countPosts(User user, Label label) {
		intializeCountManagers();
		
		Predicate restrictions = null;
		if(label != null)
			restrictions = builder.and(filterByUser(user), filterByLabel(label));
		else
			restrictions = filterByUser(user);

		count.where(restrictions).distinct(true);
		count.select(builder.countDistinct(rootPost));
		
		Long result = manager.createQuery(count).getSingleResult();
		
		manager.close();
		connectionFactory.close();
		
		return result;
	}
	
	public List<Post> findByLabel(Label label) {
		if(label != null) {
			initializeDefaultManagers();
			
			query.where(filterByLabel(label));
			
			TypedQuery<Post> tq = manager.createQuery(query);
			
			List<Post> resultList = tq.getResultList();
			manager.close();
			connectionFactory.close();
			if (!resultList.isEmpty()) return resultList;
			else return new ArrayList<Post>();		
		}
		return new ArrayList<Post>();
	}
	
	public List<Post> findByUserLabelName(User user, String labelName) {
		if(user != null && labelName != null) {
			initializeDefaultManagers();
			
			query.where(builder.and(filterByUser(user), filterByLabelName(labelName)));
			
			TypedQuery<Post> tq = manager.createQuery(query);
			
			List<Post> resultList = tq.getResultList();
			manager.close();
			connectionFactory.close();
			if (!resultList.isEmpty()) return resultList;
			else return new ArrayList<Post>();		
		}
		return new ArrayList<Post>();
	}
	
	public List<Post> findByContent(String postContent) {
		if(postContent != null) {
			initializeDefaultManagers();
			
			query.where(builder.equal(rootPost.get(Post_.CONTENT), postContent));
			
			TypedQuery<Post> tq = manager.createQuery(query);
			
			List<Post> resultList = tq.getResultList();
			manager.close();
			connectionFactory.close();
			if (!resultList.isEmpty()) return resultList;
			else return new ArrayList<Post>();		
		}
		return new ArrayList<Post>();
	}
	
	public Post insert(Post post) {
		EntityTransaction transaction = initializeTransaction();
		post = manager.merge(post);
				
		transaction.begin();
		manager.persist(post);
		transaction.commit();
		
		manager.close();
		connectionFactory.close();
		return post;
	}
	
	public Post edit(Post post) {
		EntityTransaction transaction = initializeTransaction();
				
		transaction.begin();
		manager.merge(post);
		transaction.commit();
		
		manager.close();
		connectionFactory.close();
		return post;
	}
	
	public int remove(Post post) {
		initializeDefaultManagers();
		EntityTransaction transaction = manager.getTransaction();
		CriteriaDelete<Post> deleteQuery = initializeDeleteQuery();
		
		transaction.begin();
		deleteQuery.where(builder.equal(rootPost.get(Post_.ID), post.getId()));
		
		Query query = manager.createQuery(deleteQuery);
		
		int affectedLines = query.executeUpdate();
		transaction.commit();
		manager.close();
		connectionFactory.close();

		return affectedLines;
	}
	
	public int removeList(List<Post> posts) {
		int affectedLines = 0;
		for(Post post : posts) {
			remove(post);
		}

		return affectedLines;
	}
	
	private Predicate filterByUser(User user) {
		return builder.equal(rootPost.get(Post_.USER), user.getId());
	}
	
	private Predicate filterByLabel(Label label) {
		return builder.equal(rootPost.get(Post_.LABEL), label.getId());
	}
	
	private Predicate filterByLabelName(String labelName) {
		Join<Post, Label> joinLabel = rootPost.join(Post_.LABEL);
		return builder.like(joinLabel.get(Label_.NAME), "%" + labelName + "%");
	}
	
	private Order orderAsc(){
		return builder.asc(rootPost.get(Post_.LATEST_DATE));
	}
	
	private Order orderDesc(){
		return builder.desc(rootPost.get(Post_.LATEST_DATE));
	}
}
