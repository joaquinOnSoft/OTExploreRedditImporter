package com.opentext.explore.importer.reddit;

import java.io.File;
import java.io.IOException;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.opentext.explore.connector.SolrAPIWrapper;
import com.opentext.explore.util.FileUtil;

import net.dean.jraw.RedditClient;
import net.dean.jraw.http.NetworkAdapter;
import net.dean.jraw.http.OkHttpNetworkAdapter;
import net.dean.jraw.http.UserAgent;
import net.dean.jraw.models.Listing;
import net.dean.jraw.models.Submission;
import net.dean.jraw.models.SubredditSort;
import net.dean.jraw.models.TimePeriod;
import net.dean.jraw.oauth.Credentials;
import net.dean.jraw.oauth.OAuthHelper;
import net.dean.jraw.pagination.DefaultPaginator;
import net.dean.jraw.pagination.DefaultPaginator.Builder;
import net.dean.jraw.pagination.Paginator;
import net.dean.jraw.references.SubredditReference;

/**
 * Reddit importer for OpenText Explore (Voice of the customer solution)
 * @see https://mattbdean.gitbooks.io/jraw/
 * @author Joaquín Garzón
 * @since 20.2
 */
public class RedditImporter {

	private RedditClient reddit;

	/** Solr URL (this Solr instance is used by Explore) */
	private String host = null; 

	protected static final Logger log = LogManager.getLogger(RedditImporter.class);
	
	/**
	 * @see https://mattbdean.gitbooks.io/jraw/quickstart.html
	 */
	public RedditImporter(String host) {
		this.host = host;
		
		Properties prop = FileUtil.loadProperties("reddit.properties");

		if(prop != null) {
			host = prop.getProperty("host");
			
			// Create our credentials
			Credentials credentials = Credentials.script(
					prop.getProperty("username"), 
					prop.getProperty("password"),
					prop.getProperty("clientID"), 
					prop.getProperty("clientSecret"));

			UserAgent userAgent = new UserAgent("script", "OTExploreRedditImporter", "v20.2", "JoaquinOpenText");

			// This is what really sends HTTP requests
			NetworkAdapter adapter = new OkHttpNetworkAdapter(userAgent);

			// Authenticate and get a RedditClient instance
			reddit = OAuthHelper.automatic(adapter, credentials);
		}
		else {
			log.error("reddit.properties configuration file not found.");	
		}
	}

	/**
	 * 
	 * @param subreddit
	 * @see https://mattbdean.gitbooks.io/jraw/basics.html
	 * @see https://github.com/mattbdean/JRAW/blob/master/exampleScript/src/main/java/net/dean/jraw/example/script/ScriptExample.java
	 */
	public void start(String subreddit, String itag) {
		if(reddit != null) {
			// "Navigate" to the subreddit
			SubredditReference sr = reddit.subreddit(subreddit);

			// Browse through the top posts of the last month, requesting as much data as possible per request
			Builder<Submission, SubredditSort> builder = sr.posts();

			builder.limit(Paginator.RECOMMENDED_MAX_LIMIT)
			.sorting(SubredditSort.TOP)
			.timePeriod(TimePeriod.MONTH)
			.build();

			DefaultPaginator<Submission> paginator = builder.build();
			// Request the first page
			Listing<Submission> firstPage = paginator.next();
			System.out.println("# post: " + firstPage.size());

			
			String xmlPath = null;
			String xmlFileName = FileUtil.getRandomFileName(".xml");
			try {
				
				xmlPath = RedditTransformer.submissionsToXMLFile(firstPage, xmlFileName, itag);
				
				SolrAPIWrapper wrapper = null;
				if(host == null)
					wrapper = new SolrAPIWrapper();
				else {
					wrapper = new SolrAPIWrapper(host);
				}
				wrapper.otcaBatchUpdate(new File(xmlPath));	
			} catch (IOException e) {
				log.error(e.getMessage());
			}
			finally {
				if(xmlPath != null) {
					FileUtil.deleteFile(xmlPath);	
				}
				
			}			
			
			//for (Submission post : firstPage) {				
			//	System.out.println(String.format("%s (/r/%s, %s points) - %s",
			//			post.getTitle(), post.getSubreddit(), post.getScore(), post.getUrl()));
			//}
		}
	}
}
