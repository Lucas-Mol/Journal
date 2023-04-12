package com.journal.security.sanitizer;



import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class HTMLSanitizerTest {

	@Test
	public void testScriptSanitizer() {
		
		HTMLSanitizer htmlSanitizer = new HTMLSanitizer();
		
		String maliciousHTML = "<script>alert('I'm malicious code!!!')</script><p>Hello World!</p>";
		
		
		String sanitizedhtml = htmlSanitizer.sanitizeHtml(maliciousHTML);
		
		assertEquals("Hello World!", sanitizedhtml);
	}

}
