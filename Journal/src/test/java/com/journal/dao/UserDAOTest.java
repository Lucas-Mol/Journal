package com.journal.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import com.journal.model.User;

public class UserDAOTest {

	@Test
	public void testFindUserByUsername() {
		
		UserDAO userDAO = new UserDAO();
		
		User userTest = userDAO.findByUsernameOrEmail("test01").get(0);
		
		assertEquals("1234", userTest.getPassword());
	}
	
	@Test
	public void testFindUserByEmail() {
		
		UserDAO userDAO = new UserDAO();
		
		User userTest = userDAO.findByUsernameOrEmail("test@test.com").get(0);
		
		assertEquals("test01 1234", userTest.getUsername() + " " +userTest.getPassword());
	}

}
