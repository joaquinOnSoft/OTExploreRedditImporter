package com.opentext.explore.importer.reddit;

import org.junit.Test;

import junit.framework.TestCase;

public class TestRedditImporter  extends TestCase {

	@Test
	public void testStart() {
		RedditImporter redditImp = new RedditImporter("http://localhost:8983");
		redditImp.start("CanadaPost", "Reddit");
		
		//TODO add asserts
	}
}
