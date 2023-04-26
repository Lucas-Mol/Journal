package com.journal.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;

public class ObjectDAO<T> {
	
	private Class<T> clazz;
	
	public ObjectDAO() {}

	protected ConnectionFactory connectionFactory;
	protected EntityManager manager;
	
	protected CriteriaBuilder builder;
	protected CriteriaQuery<T> query;
	protected CriteriaQuery<Long> counter;
	protected Root<T> root;
	
	protected void initializeDefaultManagers() {
		connectionFactory = new ConnectionFactory();
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(clazz);
		root = query.from(clazz);
	}
	
	protected void initializeCounterManagers() {
		connectionFactory = new ConnectionFactory();
        manager = connectionFactory.getEntityManager();
        builder = manager.getCriteriaBuilder();
        counter = builder.createQuery(Long.class);
        root = counter.from(clazz);
	}
	
	protected EntityTransaction initializeTransaction() {
		connectionFactory = new ConnectionFactory();
		manager = connectionFactory.getEntityManager();
		return manager.getTransaction();
	}
	
	protected CriteriaDelete<T> initializeDeleteQuery() {
		CriteriaDelete<T> deleteQuery = builder.createCriteriaDelete(clazz);
		root = deleteQuery.from(clazz);
		return deleteQuery;
	}
	
	protected void tearDownManagers() {
		if(manager != null) manager.close();
		if(connectionFactory != null) connectionFactory.close();
	}
	
	protected TypedQuery<T> paginizeTypedQuery(TypedQuery<T> tq, Integer first, Integer pageSize) {
		if (first != null)
			tq.setFirstResult(first);
		if (pageSize != null)
			tq.setMaxResults(pageSize);
		return tq;
	}
	
	protected Order orderAsc(String attribute){
		return builder.asc(root.get(attribute));
	}
	
	protected Order orderDesc(String attribute){
		return builder.desc(root.get(attribute));
	}
	
	
	protected void setClass(Class<T> clazz) {
		this.clazz = clazz;
	}
	
}
