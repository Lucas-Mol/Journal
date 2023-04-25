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
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.journal.model.User;
import com.journal.model.User_;


public class UserDAO {

	private ConnectionFactory connectionFactory;
	private EntityManager manager;
	
	private CriteriaBuilder builder;
	private CriteriaQuery<User> query;
	private CriteriaQuery<Long> count;
	private Root<User> rootUser;
	

	private void initializeDefaultManagers() {
		connectionFactory = new ConnectionFactory();
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(User.class);
		rootUser = query.from(User.class);
	}
	
	private void initializeCountManagers() {
		connectionFactory = new ConnectionFactory();
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		count = builder.createQuery(Long.class);
		rootUser = count.from(User.class);
	}
	
	private EntityTransaction initializeTransaction() {
		connectionFactory = new ConnectionFactory();
		manager = connectionFactory.getEntityManager();
		return manager.getTransaction();
	}
	
	private CriteriaDelete<User> initializeDeleteQuery() {
		CriteriaDelete<User> deleteQuery = builder.createCriteriaDelete(User.class);
		rootUser = deleteQuery.from(User.class);
		return deleteQuery;
	}
	
	public User findByUsernameOrEmail(String login) {
		initializeDefaultManagers();
		
		Predicate restrictions = builder.or(filterByUsername(login), filterByEmail(login));
		
		query.where(restrictions);
		TypedQuery<User> tq = manager.createQuery(query);
		
		try {
			User userResult = tq.getResultList().get(0);
			manager.close();
			connectionFactory.close();
			return userResult;
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}
	
	public User find(User user) {
		initializeDefaultManagers();
		
		query.where(builder.equal(rootUser.get(User_.ID), user.getId()));

	    TypedQuery<User> tq = manager.createQuery(query);
	    User result = tq.getSingleResult();
	    manager.close();
	    connectionFactory.close();
	    return result;
	}
	
	public List<User> findListByUsername(String username) {
		if(username != null) {
			initializeDefaultManagers();
			
			query.where(filterLikeUsername(username));
			
			TypedQuery<User> tq = manager.createQuery(query);
			
			List<User> resultList = tq.getResultList();
			manager.close();
			connectionFactory.close();
			if (!resultList.isEmpty()) return resultList;
			else return new ArrayList<User>();		
		}
		return new ArrayList<User>();
	}
	
	public User insert(User user) {
		EntityTransaction transaction = initializeTransaction();
				
		transaction.begin();
		manager.persist(user);
		transaction.commit();
		
		manager.close();
		connectionFactory.close();
		return user;
	}
	
	public User edit(User user) {
		EntityTransaction transaction = initializeTransaction();
				
		transaction.begin();
		manager.merge(user);
		transaction.commit();
		
		manager.close();
		connectionFactory.close();
		return user;
	}
	
	public int remove(User user) {
		initializeDefaultManagers();
		EntityTransaction transaction = manager.getTransaction();
		CriteriaDelete<User> deleteQuery = initializeDeleteQuery();
		
		
		transaction.begin();
		deleteQuery.where(builder.equal(rootUser.get(User_.ID), user.getId()));
		
		Query query = manager.createQuery(deleteQuery);
		
		int affectedLines = query.executeUpdate();
		transaction.commit();
		manager.close();
		connectionFactory.close();

		return affectedLines;
	}
	
	public int removeList(List<User> users) {
		int affectedLines = 0;

		for(User user : users) {
			affectedLines += remove(user);
		}
		
		return affectedLines;
	}
	
	public boolean existUsername(String username) {
		initializeCountManagers();
		
		count.where(filterByUsername(username));
		count.select(builder.count(rootUser));
		
		boolean result = manager.createQuery(count).getSingleResult() > 0;
		
		manager.close();
		connectionFactory.close();
		return result;
	}
	
	public boolean existEmail(String email) {
		initializeCountManagers();
		
		count.where(filterByEmail(email));
		count.select(builder.count(rootUser));
		
		boolean result = manager.createQuery(count).getSingleResult() > 0;
		
		manager.close();
		connectionFactory.close();
		return result;

	}
	
	private Predicate filterByUsername(String username) {
		return builder.like(builder.lower(rootUser.get(User_.USERNAME)), username.toLowerCase());
	}
	
	private Predicate filterLikeUsername(String username) {
		return builder.like(builder.lower(rootUser.get(User_.USERNAME)), "%" + username.toLowerCase() + "%");
	}
	
	private Predicate filterByEmail(String email) {
		return builder.like(builder.lower(rootUser.get(User_.EMAIL)), email.toLowerCase());
	}
}
