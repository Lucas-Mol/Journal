package com.journal.util;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringUtilsTest {

	@Test
	public void testEncryptString() {
		
		// If hash is using SHA-256 and if it's correct
		assertTrue(StringUtils.encryptPassword("1234").equals("03AC674216F3E15C761EE1A5E255F067953623C8B388B4459E13F978D7C846F4"));
	}

}
