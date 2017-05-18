package run;

import java.io.File;
import java.io.FileNotFoundException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import objects.CliResponse;

public class CLI {
	private static final Logger log = LoggerFactory.getLogger(CLI.class.getName());
	private String[] args = null;
	private Options options = new Options();

	public CLI(String[] args) {
		this.args = args;
		options.addOption("d", "directory", true, "mandatory!!  path of the directory with the review files");
		options.addOption("t", "topic", true, "mandatory!!  topic of interest in the hotels");
		options.addOption("r", "ratings", false, "show the user ratings for attributes of the hotel");
		options.addOption("s", "sentence", false, "show representative sentences where topic was found in");
		options.addOption("rd", "review details", false, "show additional information about the review");
		options.addOption("i", "hotel info", false, "show aditional information regarding the hotel");
		options.addOption("h", "help", false, "");

	}

	public CliResponse parse() throws Exception {
		CommandLineParser parser = new GnuParser(); // BasicParser();
		CommandLine cmd = null;
		CliResponse cliResp = new CliResponse();
		try {
			cmd = parser.parse(options, args);
			String directory;

			if (!cmd.hasOption("d")) {
				help();
			} else {
				directory = cmd.getOptionValue("d");
				File dataFolder = new File(directory);
				if (!dataFolder.exists()) {
					throw new FileNotFoundException("the specified directory path was not found");
				} else if (!dataFolder.isDirectory()) {
					throw new FileNotFoundException("this is not a folder/directory");
				} else if (dataFolder.list().length == 0) {
					throw new FileNotFoundException("this folder is empty");
				}
				cliResp.setDir(directory);
			}

			if (!cmd.hasOption("t"))
				help();
			else
				cliResp.setTopic(cmd.getOptionValue("t"));

			if (cmd.hasOption("r"))
				cliResp.setRatings(true);

			if (cmd.hasOption("s"))
				cliResp.setRepresentativeSentences(true);

			if (cmd.hasOption("rd"))
				cliResp.setReviewDetails(true);

			if (cmd.hasOption("i"))
				cliResp.setHotlInfo(true);

			if (cmd.hasOption("h"))
				help();

		} catch (ParseException e) {
			log.warn("Failed to parse comand line properties", e);
			help();
		}
		return cliResp;
	}

	private void help() {
		// This prints out some help
		HelpFormatter formater = new HelpFormatter();

		formater.printHelp("Main", options);
		System.exit(0);
	}
}
