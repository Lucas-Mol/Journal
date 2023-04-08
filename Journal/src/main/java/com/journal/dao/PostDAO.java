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
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.journal.model.Label;
import com.journal.model.Post;
import com.journal.model.Post_;
import com.journal.model.User;

public class PostDAO {

	private ConnectionFactory connectionFactory = new ConnectionFactory();
	private EntityManager manager;
	
	private CriteriaBuilder builder;
	private CriteriaQuery<Post> query;
	private CriteriaQuery<Long> count;
	private Root<Post> rootPost;
	
	public List<Post> findByUser(User user) {
	    return findByUserLabel(user, null, null, null);
	}
	
	public List<Post> findByUserLabel(User user, Label label) {
	    return findByUserLabel(user, label, null, null);
	}
	
	public List<Post> findByUserLabel(User user, Label label, Integer first, Integer pageSize) {
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Post.class);
		rootPost = query.from(Post.class);
		
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
		if (!resultList.isEmpty()) return resultList;
		else return new ArrayList<Post>();
	}
	
	public Long countPosts(User user, Label label) {
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		count = builder.createQuery(Long.class);
		rootPost = count.from(Post.class);
		
		Predicate restrictions = null;
		if(label != null)
			restrictions = builder.and(filterByUser(user), filterByLabel(label));
		else
			restrictions = filterByUser(user);

		count.where(restrictions).distinct(true);
		count.select(builder.countDistinct(rootPost));
		
		return manager.createQuery(count).getSingleResult();
	}
	
	public Post insert(Post post) {
		manager = connectionFactory.getEntityManager();
		EntityTransaction transaction = manager.getTransaction();
		post = manager.merge(post);
				
		transaction.begin();
		manager.persist(post);
		transaction.commit();
		
		manager.close();
		return post;
	}
	
	public Post edit(Post post) {
		manager = connectionFactory.getEntityManager();
		EntityTransaction transaction = manager.getTransaction();
				
		transaction.begin();
		manager.merge(post);
		transaction.commit();
		
		manager.close();
		return post;
	}
	
	public int remove(Post post) {
		manager = connectionFactory.getEntityManager();
		manager.getTransaction().begin();
		builder = manager.getCriteriaBuilder();
		CriteriaDelete<Post> deleteQuery = builder.createCriteriaDelete(Post.class);
		rootPost = deleteQuery.from(Post.class);
		
		deleteQuery.where(builder.equal(rootPost.get(Post_.ID), post.getId()));
		
		Query query = manager.createQuery(deleteQuery);
		
		int affectedLines = query.executeUpdate();
		manager.getTransaction().commit();
		manager.close();

		return affectedLines;
	}
	
	private Predicate filterByUser(User user) {
		return builder.equal(rootPost.get(Post_.USER), user.getId());
	}
	
	private Predicate filterByLabel(Label label) {
		return builder.equal(rootPost.get(Post_.LABEL), label.getId());
	}
	
	private Order orderAsc(){
		return builder.asc(rootPost.get(Post_.LATEST_DATE));
	}
	
	private Order orderDesc(){
		return builder.desc(rootPost.get(Post_.LATEST_DATE));
	}
}
