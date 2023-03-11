package com.journal.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import com.journal.model.User;
import com.journal.model.User_;

public class UserDAO {

	private static EntityManager manager = ConnectionFactory.getEntityManager();
	
	private CriteriaBuilder builder;
	private CriteriaQuery<User> query;
	private Root<User> rootUser;
	private Predicate restrictions;

	
	public List<User> findByUsernameOrEmail(String login) {
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(User.class);
		rootUser = query.from(User.class);
		
		restrictions = builder.or(builder.like(builder.lower(rootUser.get(User_.USERNAME)), login.toLowerCase()),
								  builder.like(builder.lower(rootUser.get(User_.EMAIL)), login.toLowerCase()));
		
		query.where(restrictions);
		TypedQuery<User> tq = manager.createQuery(query);

		return tq.getResultList();

	}
	
}
