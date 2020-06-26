package com.opentext.explore.importer.reddit;

import org.junit.Test;

import junit.framework.TestCase;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;

public class TestRedditImporter  extends TestCase {

	@Test
	public void testStart() {
		RedditImporter redditImp = new RedditImporter("http://localhost:8983");
		Listing<Submission> firstPage = redditImp.readSubreddit("CanadaPost");
		
		assertNotNull(firstPage);
		assertEquals(100, firstPage.size());
				
		boolean result = redditImp.solrBatchUpdate("Reddit Canada Post", firstPage);
		assertTrue(result);
	}
}
