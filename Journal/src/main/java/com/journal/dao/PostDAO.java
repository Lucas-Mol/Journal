package com.journal.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import com.journal.model.Post;
import com.journal.model.Post_;
import com.journal.model.User;
import com.journal.model.User_;

public class PostDAO {

	private ConnectionFactory connectionFactory = new ConnectionFactory();
	private EntityManager manager;
	
	private CriteriaBuilder builder;
	private CriteriaQuery<Post> query;
	private CriteriaQuery<Long> count;
	private Root<Post> rootPost;
	
	public Post find(User user) {
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Post.class);
		rootPost = query.from(Post.class);
		
		query.where(builder.equal(rootPost.get(User_.ID), user.getId()));

	    TypedQuery<Post> tq = manager.createQuery(query);
	    Post result = tq.getSingleResult();
	    manager.close();
	    return result;
	}
	
	public Post insert(Post post) {
		manager = connectionFactory.getEntityManager();
		EntityTransaction transaction = manager.getTransaction();
				
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
}
