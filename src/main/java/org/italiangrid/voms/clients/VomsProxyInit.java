package org.italiangrid.voms.clients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.italiangrid.voms.clients.options.VomsCliOption;
import org.italiangrid.voms.clients.options.VomsClientsCommonOptions;
import org.italiangrid.voms.clients.options.VomsProxyInitOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * This class implements a command-line voms-proxy-init client.
 * 
 * @author Daniele Andreotti
 * 
 */

public class VomsProxyInit {

	private static final Logger log = LoggerFactory
			.getLogger(VomsProxyInit.class);

	/** The CLI options **/
	private static Options cliOptions;

	/** The CLI options parser **/
	private static CommandLineParser cliParser = new GnuParser();

	/**
	 * Initializes command-line options
	 */

	private static void initOptions() {

		cliOptions = new Options();

		List<VomsCliOption> listOptions = new ArrayList<VomsCliOption>();
		listOptions.addAll(Arrays.asList(VomsClientsCommonOptions.values()));
		listOptions.addAll(Arrays.asList(VomsProxyInitOptions.values()));

		for (VomsCliOption optionElement : listOptions) {

			Option option = new Option(optionElement.getOpt(),
					optionElement.getLongOpt(), optionElement.hasArg(),
					optionElement.getDescription());

			option.setArgName(optionElement.getArgDescription());

			cliOptions.addOption(option);
		}

	}

	/**
	 * Prints usage information
	 */
	private static void usage() {

		int lineWidth = 120;
		String header = "options:";
		String footer = "";

		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(lineWidth,
				"java -cp ... org.italiangrid.voms.clients.VomsProxyInit",
				header, cliOptions, footer);
	}

	public static void main(String[] args) {

		initOptions();

		try {
			CommandLine line = cliParser.parse(cliOptions, args);

			if (line.hasOption("help") || line.hasOption("usage")) {
				usage();
				System.exit(0);
			}

		} catch (ParseException e) {
			log.error(e.getMessage(), e.getCause());
			System.err.println("Error parsing command line arguments: "
					+ e.getMessage());
			usage();
			System.exit(1);
		}
	}
}
