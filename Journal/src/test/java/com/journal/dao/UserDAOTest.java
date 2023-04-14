package com.journal.dao;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import com.journal.model.User;

@TestInstance(Lifecycle.PER_CLASS)
public class UserDAOTest {
	
	UserDAO userDAO = new UserDAO();

	@Test
	public void testFindUserByUsername() {
		
		User userTest = userDAO.findByUsernameOrEmail("test01");
		
		assertEquals("03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4", userTest.getPassword());
	}
	
	@Test
	public void testFindUserByEmail() {
		
		User userTest = userDAO.findByUsernameOrEmail("test@test.com");
		
		assertEquals("test01 03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4", userTest.getUsername() + " " +userTest.getPassword());
	}
	
	@Test
	public void testValidateUsername() {
		//Username already used, must return true
		assertTrue(userDAO.existUsername("test01"));
	}
	
	@Test
	public void testValidateEmail() {
		//Username already used, must return true
		assertTrue(userDAO.existEmail("test@test.com"));
	}
	
	@Test
	public void testInsertNewUser() {		
		User newUser = new User();
		newUser.setUsername("insertTestUser01");
		newUser.setEmail("insert01@test.com");
		newUser.setPassword("123456");
		
		try {
			userDAO.insert(newUser);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		User persistedUser = userDAO.findByUsernameOrEmail("insertTestUser01");
		
		//Must be not null, means that it was persisted sucessfully
		assertTrue(persistedUser != null
				&& persistedUser.getUsername().equals("insertTestUser01")
				&& persistedUser.getEmail().equals("insert01@test.com"));
	}
	
	@Test
	public void testRemoveNewUser() {		
		User newUser = new User();
		newUser.setUsername("insertTestUser02");
		newUser.setEmail("insert02@test.com");
		newUser.setPassword("123456");
		
		try {
			userDAO.insert(newUser);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		User persistedUser = userDAO.findByUsernameOrEmail("insertTestUser02");
		
		int affectedLines = 0;
		try {
			affectedLines = userDAO.remove(persistedUser);
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		User removedUser = userDAO.findByUsernameOrEmail("insertTestUser02");
		
		//Must be null, means that it was removed sucessfully
		assertTrue(removedUser == null && affectedLines == 1);
	}
	
	@AfterAll
    public void cleanDB() {
        List<User> users = userDAO.findListByUsername("insertTestUser");
        
        userDAO.removeList(users);
    }


}
