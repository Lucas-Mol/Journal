package com.journal.dao;

import static org.junit.Assert.*;

import javax.persistence.EntityManager;

import org.junit.Test;

public class ConnectionFactoryTest {

	@Test
	public void testConnection() {
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		
		EntityManager manager = connectionFactory.getEntityManager();
		
		assertTrue(manager != null);
	}

}
