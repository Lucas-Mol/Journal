package com.journal.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.journal.model.User;
import com.journal.model.User_;

public class UserDAO {

	private ConnectionFactory connectionFactory = new ConnectionFactory();
	private EntityManager manager;
	
	private CriteriaBuilder builder;
	private CriteriaQuery<User> query;
	private CriteriaQuery<Long> count;
	private Root<User> rootUser;

	
	public User findByUsernameOrEmail(String login) {
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(User.class);
		rootUser = query.from(User.class);
		
		Predicate restrictions = builder.or(filterByUsername(login), filterByEmail(login));
		
		query.where(restrictions);
		TypedQuery<User> tq = manager.createQuery(query);
		
		try {
			User userResult = tq.getResultList().get(0);
			manager.close();
			return userResult;
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public User find(User user) {
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(User.class);
		rootUser = query.from(User.class);
		
		query.where(builder.equal(rootUser.get(User_.ID), user.getId()));

	    TypedQuery<User> tq = manager.createQuery(query);
	    User result = tq.getSingleResult();
	    manager.close();
	    return result;
	}
	
	public User insert(User user) {
		manager = connectionFactory.getEntityManager();
		EntityTransaction transaction = manager.getTransaction();
				
		transaction.begin();
		manager.persist(user);
		transaction.commit();
		
		manager.close();
		return user;
	}
	
	public User edit(User user) {
		manager = connectionFactory.getEntityManager();
		EntityTransaction transaction = manager.getTransaction();
				
		transaction.begin();
		manager.merge(user);
		transaction.commit();
		
		manager.close();
		return user;
	}
	
	public int remove(User user) {
		manager = connectionFactory.getEntityManager();
		manager.getTransaction().begin();
		builder = manager.getCriteriaBuilder();
		CriteriaDelete<User> deleteQuery = builder.createCriteriaDelete(User.class);
		rootUser = deleteQuery.from(User.class);
		
		deleteQuery.where(builder.equal(rootUser.get(User_.ID), user.getId()));
		
		Query query = manager.createQuery(deleteQuery);
		
		int affectedLines = query.executeUpdate();
		manager.getTransaction().commit();
		manager.close();

		return affectedLines;
	}
	
	public boolean existUsername(String username) {
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		count = builder.createQuery(Long.class);
		rootUser = count.from(User.class);
		
		count.where(filterByUsername(username));
		count.select(builder.count(rootUser));
		
		boolean result = manager.createQuery(count).getSingleResult() > 0;
		
		manager.close();
		return result;
	}
	
	public boolean existEmail(String email) {
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		count = builder.createQuery(Long.class);
		rootUser = count.from(User.class);
		
		count.where(filterByEmail(email));
		count.select(builder.count(rootUser));
		
		boolean result = manager.createQuery(count).getSingleResult() > 0;
		
		manager.close();
		return result;

	}
	
	private Predicate filterByUsername(String username) {
		return builder.like(builder.lower(rootUser.get(User_.USERNAME)), username.toLowerCase());
	}
	
	private Predicate filterByEmail(String email) {
		return builder.like(builder.lower(rootUser.get(User_.EMAIL)), email.toLowerCase());
	}
}
