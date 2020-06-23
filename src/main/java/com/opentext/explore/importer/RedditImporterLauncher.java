package com.opentext.explore.importer;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RedditImporterLauncher {
	
	private static final Logger log = LogManager.getLogger(RedditImporterLauncher.class);

	public static void main(String[] args) {		
		Options options = new Options();

		Option itagOption = new Option("i", "itag", true, "Explore Importer tag. Added to each article importer");
		itagOption.setRequired(true);
		options.addOption(itagOption);		
		
		Option threadOption = new Option("s", "subreddit", true, "Subreddit thread name");
		threadOption.setRequired(true);
		options.addOption(threadOption);
		
		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();
		CommandLine cmd = null;

		try {
			cmd = parser.parse(options, args);

			String itag = null;
			
			for(String arg: args){
				if (arg.equals("--itag") || arg.equals("-i")) {
					itag = arg;
				}
				
				if (arg.equals("--subreddit") || arg.equals("-s")) {
				}
			}
			
		}
		catch (ParseException e) {
			log.error(e.getMessage());			
			
			formatter.printHelp("java -jar file.jar --config/-c 'config file path'", options);

			System.exit(-1);	
		}
	}
}
