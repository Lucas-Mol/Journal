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

import com.journal.enumeration.ColorEnum;
import com.journal.model.Label;
import com.journal.model.Label_;

public class LabelDAO {

	private ConnectionFactory connectionFactory;
	private EntityManager manager;
	
	private CriteriaBuilder builder;
	private CriteriaQuery<Label> query;
	private CriteriaQuery<Long> count;
	private Root<Label> rootLabel;
	
	private void initializeDefaultManagers() {
		connectionFactory = new ConnectionFactory();
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Label.class);
		rootLabel = query.from(Label.class);
	}
	
	private EntityTransaction initializeTransaction() {
		connectionFactory = new ConnectionFactory();
		manager = connectionFactory.getEntityManager();
		return manager.getTransaction();
	}
	
	private CriteriaDelete<Label> initializeDeleteQuery() {
		CriteriaDelete<Label> deleteQuery = builder.createCriteriaDelete(Label.class);
		rootLabel = deleteQuery.from(Label.class);
		return deleteQuery;
	}
	
	public Label findByNameColor(Label label) {
		initializeDefaultManagers();
		
		Predicate restrictions = builder.and(filterByName(label.getName()), filterByColor(label.getColor()));
		
		query.where(restrictions);
		TypedQuery<Label> tq = manager.createQuery(query);
		
		try {
			Label result = tq.getResultList().get(0);
			manager.close();
			connectionFactory.close();
			return result;
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}

	public Label insert(Label label) {
		EntityTransaction transaction = initializeTransaction();
				
		transaction.begin();
		manager.persist(label);
		transaction.commit();
		
		manager.close();
		connectionFactory.close();
		return label;
	}
	
	public Label edit(Label label) {
		EntityTransaction transaction = initializeTransaction();
				
		transaction.begin();
		manager.merge(label);
		transaction.commit();
		
		manager.close();
		connectionFactory.close();
		return label;
	}
	
	public int remove(Label label) {
		initializeDefaultManagers();
		EntityTransaction transaction = manager.getTransaction();
		CriteriaDelete<Label> deleteQuery = initializeDeleteQuery();
		
		transaction.begin();
		
		deleteQuery.where(builder.equal(rootLabel.get(Label_.ID), label.getId()));
		
		Query query = manager.createQuery(deleteQuery);
		
		int affectedLines = query.executeUpdate();
		transaction.commit();
		manager.close();
		connectionFactory.close();

		return affectedLines;
	}
	
	private Predicate filterByName(String name) {
		return builder.like(builder.lower(rootLabel.get(Label_.NAME)), name.toLowerCase());
	}
	
	private Predicate filterByColor(ColorEnum color) {
		return builder.equal(rootLabel.get(Label_.COLOR), color.getId());
	}

	
	
}
