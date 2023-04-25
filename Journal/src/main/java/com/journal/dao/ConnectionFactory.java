package com.journal.dao;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ConnectionFactory {
		
		private EntityManagerFactory factory;
		
		public ConnectionFactory() {
			factory = Persistence.createEntityManagerFactory("journal");
		}
		
		public EntityManager getEntityManager() {
			return factory.createEntityManager();
		}
		
		public void close() {
			factory.close();
		}


}
