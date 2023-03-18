package com.journal.dao;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.journal.model.User;

public class UserDAOTest {

	@Test
	public void testFindUserByUsername() {
		
		UserDAO userDAO = new UserDAO();
		
		User userTest = userDAO.findByUsernameOrEmail("test01");
		
		assertEquals("03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4", userTest.getPassword());
	}
	
	@Test
	public void testFindUserByEmail() {
		
		UserDAO userDAO = new UserDAO();
		
		User userTest = userDAO.findByUsernameOrEmail("test@test.com");
		
		assertEquals("test01 03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4", userTest.getUsername() + " " +userTest.getPassword());
	}
	
	@Test
	public void testValidateUsername() {
		
		UserDAO userDAO = new UserDAO();
		
		//Username already used, must return true
		assertTrue(userDAO.existUsername("test01"));
	}
	
	@Test
	public void testValidateEmail() {
		UserDAO userDAO = new UserDAO();
		
		//Username already used, must return true
		assertTrue(userDAO.existEmail("test@test.com"));
	}
	
	@Test
	public void testInsertNewUser() {
		UserDAO userDAO = new UserDAO();
		
		User newUser = new User();
		newUser.setUsername("insertTestUser");
		newUser.setEmail("insert@test.com");
		newUser.setPassword("123456");
		
		try {
			userDAO.insert(newUser);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		User persistedUser = userDAO.findByUsernameOrEmail("insertTestUser");
		
		//Must be not null, means that it was persisted sucessfully
		assertTrue(persistedUser != null);
	}
	
	@Test
	public void testRemoveNewUser() {
		UserDAO userDAO = new UserDAO();
		
		User persistedUser = userDAO.findByUsernameOrEmail("insertTestUser");
		int affectedLines = 0;
		try {
			affectedLines = userDAO.remove(persistedUser);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		User removedUser = userDAO.findByUsernameOrEmail("insertTestUser");
		
		//Must be null, means that it was removed sucessfully
		assertTrue(removedUser == null && affectedLines == 1);
	}

}
