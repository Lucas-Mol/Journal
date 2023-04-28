package com.journal.dao;

import java.util.List;

import javax.persistence.EntityTransaction;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.Predicate;

import com.journal.enumeration.ColorEnum;
import com.journal.model.Label;
import com.journal.model.Label_;

public class LabelDAO extends ObjectDAO<Label>{
	
	public LabelDAO() {
		setClass(Label.class);
	}
	

	public Label findByNameColor(String name, ColorEnum color) {
		initializeDefaultManagers();
		try {
			Predicate restrictions = builder.and(filterByName(name), filterByColor(color));
			
			query.where(restrictions);
			TypedQuery<Label> tq = manager.createQuery(query);
			
			Label result = tq.getResultList().get(0);
			return result;
		} catch (IndexOutOfBoundsException ex) {
			return null;
		} finally {
			tearDownManagers();
		}
	}
	
	public List<Label> findByName(String name) {
		initializeDefaultManagers();
		try {
			Predicate restrictions = builder.and(filterByName(name));
			
			query.where(restrictions);
			TypedQuery<Label> tq = manager.createQuery(query);
			
			return tq.getResultList();
		} finally {
			tearDownManagers();
		}
	}

	public Label insert(Label label) {
		EntityTransaction transaction = initializeTransaction();
				
		try {
			transaction.begin();
			manager.persist(label);	
			transaction.commit();
			
			return label;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			tearDownManagers();
		}
	}
	
	public Label edit(Label label) {
		EntityTransaction transaction = initializeTransaction();
		
		try {
			transaction.begin();
			label = manager.merge(label);
			transaction.commit();
			
			return label;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			tearDownManagers();	
		}	
	}
	
	public int remove(Label label) {
		initializeDefaultManagers();
		EntityTransaction transaction = manager.getTransaction();
		CriteriaDelete<Label> deleteQuery = initializeDeleteQuery();
		
		try {
			transaction.begin();
			deleteQuery.where(builder.equal(root.get(Label_.ID), label.getId()));
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
	
	private Predicate filterByName(String name) {
		return builder.like(builder.lower(root.get(Label_.NAME)), name.toLowerCase());
	}
	
	private Predicate filterByColor(ColorEnum color) {
		return builder.equal(root.get(Label_.COLOR), color.getId());
	}


	
}
