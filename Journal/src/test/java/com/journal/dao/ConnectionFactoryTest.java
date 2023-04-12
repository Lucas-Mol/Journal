package com.journal.dao;

import static org.junit.jupiter.api.Assertions.assertTrue;

import javax.persistence.EntityManager;

import org.junit.jupiter.api.Test;
import org.junit.platform.commons.annotation.Testable;

@Testable
public class ConnectionFactoryTest {

	@Test
	public void testConnection() {
		
		ConnectionFactory connectionFactory = new ConnectionFactory();
		
		EntityManager manager = connectionFactory.getEntityManager();
		
		assertTrue(manager != null);
	}

}
