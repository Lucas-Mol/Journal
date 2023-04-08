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
import com.journal.model.User;

public class LabelDAO {

	private ConnectionFactory connectionFactory = new ConnectionFactory();
	private EntityManager manager;
	
	private CriteriaBuilder builder;
	private CriteriaQuery<Label> query;
	private CriteriaQuery<Long> count;
	private Root<Label> rootLabel;
	
	public Label findByNameColor(Label label) {
		manager = connectionFactory.getEntityManager();
		builder = manager.getCriteriaBuilder();
		query = builder.createQuery(Label.class);
		rootLabel = query.from(Label.class);
		
		Predicate restrictions = builder.and(filterByName(label.getName()), filterByColor(label.getColor()));
		
		query.where(restrictions);
		TypedQuery<Label> tq = manager.createQuery(query);
		
		try {
			Label result = tq.getResultList().get(0);
			manager.close();
			return result;
		} catch (IndexOutOfBoundsException ex) {
			return null;
		}
	}

	public Label insert(Label label) {
		manager = connectionFactory.getEntityManager();
		EntityTransaction transaction = manager.getTransaction();
				
		transaction.begin();
		manager.persist(label);
		transaction.commit();
		
		manager.close();
		return label;
	}
	
	public Label edit(Label label) {
		manager = connectionFactory.getEntityManager();
		EntityTransaction transaction = manager.getTransaction();
				
		transaction.begin();
		manager.merge(label);
		transaction.commit();
		
		manager.close();
		return label;
	}
	
	public int remove(Label label) {
		manager = connectionFactory.getEntityManager();
		manager.getTransaction().begin();
		builder = manager.getCriteriaBuilder();
		CriteriaDelete<Label> deleteQuery = builder.createCriteriaDelete(Label.class);
		rootLabel = deleteQuery.from(Label.class);
		
		deleteQuery.where(builder.equal(rootLabel.get(Label_.ID), label.getId()));
		
		Query query = manager.createQuery(deleteQuery);
		
		int affectedLines = query.executeUpdate();
		manager.getTransaction().commit();
		manager.close();

		return affectedLines;
	}
	
	private Predicate filterByName(String name) {
		return builder.like(builder.lower(rootLabel.get(Label_.NAME)), name.toLowerCase());
	}
	
	private Predicate filterByColor(ColorEnum color) {
		return builder.equal(rootLabel.get(Label_.COLOR), color.getId());
	}

	
	
}
