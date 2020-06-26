package com.opentext.explore.importer.reddit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * 
 * @author Joaqu�n Garz�n
 * @since 20.2
 */
public class RedditImporterLauncher {
	
	private static final Logger log = LogManager.getLogger(RedditImporterLauncher.class);

	public static void main(String[] args) {		
		Options options = new Options();

		Option hostOption = new Option("h", "host", true, "Solr URL. Default value: http://localhost:8983");
		options.addOption(hostOption);			
		
		Option rtagOption = new Option("r", "rtag", true, "Explore Reddit Importer tag. Added to each article importer");
		options.addOption(rtagOption);		
		
		Option threadOption = new Option("s", "subreddit", true, "Subreddit thread name");
		threadOption.setRequired(true);
		options.addOption(threadOption);
		
		Option poolOption = new Option("p", "pool", true, "Seconds between each call against Reddit API");
		options.addOption(poolOption);		
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			String rtag = "Reddit";
			String host = "http://localhost:8983";
			String subreddit = "CanadaPost";
			int pool = 30;
			
			
			if (cmd.hasOption("rtag") || cmd.hasOption("r")) {
				rtag = cmd.getOptionValue("rtag");
			}

			if (cmd.hasOption("host") || cmd.hasOption("h")) {
				host = cmd.getOptionValue("host");
			}				
			
			if (cmd.hasOption("subreddit") || cmd.hasOption("s")) {
				subreddit = cmd.getOptionValue("subreddit");
			}
		
			if (cmd.hasOption("pool") || cmd.hasOption("p")) {
				String strPool = cmd.getOptionValue("pool");
				try {
					pool = Integer.parseInt(strPool);
				}
				catch(NumberFormatException e) {
					formatter.printHelp("java -jar OTExploreRedditImporter.20.2.jar --rtag \"Reddit Canada Post\" --subreddit CanadaPost", options);

					exitInError(e);
				}
			}
					
			RedditImporter importer = new RedditImporter(host);
			importer.start(subreddit, rtag, pool);
			
		}
		catch (ParseException e) {
			formatter.printHelp("java -jar OTExploreRedditImporter.20.2.jar --rtag \"Reddit Canada Post\" --subreddit CanadaPost", options);

			exitInError(e);	
		}
	}
	
	private static void exitInError(Exception e) {
		log.error(e.getMessage());
		System.exit(-1);	
	}
}
