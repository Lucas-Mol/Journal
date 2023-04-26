package com.journal.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;

import com.journal.model.User;
import com.journal.model.User_;


public class UserDAO extends ObjectDAO<User>{

	public UserDAO() {
		setClass(User.class);
	}

	
	public User findByUsernameOrEmail(String login) {
		initializeDefaultManagers();
		
		try {
			Predicate restrictions = builder.or(filterByUsername(login), filterByEmail(login));
			query.where(restrictions);
			TypedQuery<User> tq = manager.createQuery(query);
			return tq.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			tearDownManagers();
		}
	}
	
	public User find(User user) {
		initializeDefaultManagers();
		
		try {
			query.where(builder.equal(root.get(User_.ID), user.getId()));
			TypedQuery<User> tq = manager.createQuery(query);
			return tq.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			tearDownManagers();
		}
	}
	
	public List<User> findListByUsername(String username) {
		initializeDefaultManagers();

		try {
			query.where(filterLikeUsername(username));
			TypedQuery<User> tq = manager.createQuery(query);
			return tq.getResultList();
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<User>();
		} finally {
			tearDownManagers();
		}
	}
	
	public User insert(User user) {
		EntityTransaction transaction = initializeTransaction();
		user = manager.merge(user);
				
		try {
			transaction.begin();
			manager.persist(user);
			transaction.commit();
			
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			tearDownManagers();
		}
	}
	
	public User edit(User user) {
		EntityTransaction transaction = initializeTransaction();
				
		try {
			transaction.begin();
			 user = manager.merge(user);
			transaction.commit();
			
			return user;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			tearDownManagers();
		}
	}
	
	public int remove(User user) {
		initializeDefaultManagers();
		EntityTransaction transaction = manager.getTransaction();
		CriteriaDelete<User> deleteQuery = initializeDeleteQuery();
		
		try {
			transaction.begin();
			deleteQuery.where(builder.equal(root.get(User_.ID), user.getId()));
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
	
	public int removeList(List<User> users) {
		int affectedLines = 0;

		for(User user : users)
			affectedLines += remove(user);
		
		return affectedLines;
	}
	
	public boolean existUsername(String username) {
		initializeCounterManagers();
		
		try {
			counter.where(filterByUsername(username));
			counter.select(builder.count(root));
			
			return manager.createQuery(counter).getSingleResult() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			tearDownManagers();
		}
	}
	
	public boolean existEmail(String email) {
		initializeCounterManagers();
		
		try {
			counter.where(filterByEmail(email));
			counter.select(builder.count(root));
			return manager.createQuery(counter).getSingleResult() > 0;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			tearDownManagers();
		}
	}
	
	private Predicate filterByUsername(String username) {
		return builder.like(builder.lower(root.get(User_.USERNAME)), username.toLowerCase());
	}
	
	private Predicate filterLikeUsername(String username) {
		return builder.like(builder.lower(root.get(User_.USERNAME)), "%" + username.toLowerCase() + "%");
	}
	
	private Predicate filterByEmail(String email) {
		return builder.like(builder.lower(root.get(User_.EMAIL)), email.toLowerCase());
	}
}
