package com.journal.security.sanitizer;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;

public class HTMLSanitizer {

	public String sanitizeHtml(String html) {
	    PolicyFactory policy = new HtmlPolicyBuilder()
	        .allowElements("b", "i", "u", "em", "strong", "a")
	        .allowAttributes("href").onElements("a")
	        .toFactory();

	    return policy.sanitize(html);
	}

}
