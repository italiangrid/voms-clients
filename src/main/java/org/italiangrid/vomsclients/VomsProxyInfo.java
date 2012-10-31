package org.italiangrid.vomsclients;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 
 * This class implements a command-line voms-proxy-info client.
 * 
 * @author Daniele Andreotti
 * 
 */

public class VomsProxyInfo {

	private static final Logger log = LoggerFactory.getLogger(VomsProxyInfo.class);

	/** The CLI options **/
	private static Options cliOptions;

	/** The CLI options parser **/
	private static CommandLineParser cliParser = new GnuParser();


	@SuppressWarnings("static-access")
	private static void initOptions() {

		cliOptions = new Options();

		/* Options */
		cliOptions.addOption("help", "usage", false, "Displays helps and exits");
		cliOptions.addOption(OptionBuilder.withDescription("Displays version").create("version"));
		cliOptions.addOption(OptionBuilder.hasArgs().withArgName("proxyfile")
				.withDescription("Non-standard location of proxy").create("file"));
		cliOptions.addOption(OptionBuilder.withDescription("Skips AC verification").create("dont_verify_ac"));
		cliOptions.addOption(OptionBuilder.withDescription("Displays debugging output").create("debug"));

		cliOptions.addOption(OptionBuilder.withDescription("Returns 0 if valid proxy exists, 1 otherwise")
				.withArgName("[options]").hasOptionalArgs().create("exists"));
		cliOptions.addOption(OptionBuilder.withArgName("voname")
				.withDescription("Returns 0 if AC exists corresponding to voname, 1 otherwise").create("acexists"));
		cliOptions.addOption(OptionBuilder.hasArgs().withDescription("Read options from file <name>").withArgName("name")
				.create("conf"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints included file").create("included"));


		/* Printout options */
		cliOptions.addOption(OptionBuilder.withDescription("Prints information about the whole proxy chain (CA excluded)")
				.create("chain"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Distinguished name (DN) of proxy subject").create(
				"subject"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints DN of proxy issuer (certificate signer)").create(
				"issuer"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints DN of the identity represented by the proxy").create(
				"identity"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Type of proxy (full or limited)").create("type"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Time (in seconds) until proxy expires").create(
				"timeleft"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Key size (in bits)").create("strength"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints All proxy options in a human readable format").create(
				"all"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints All of the certificate").create("text"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Pathname of proxy file").create("path"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Vo name").create("vo"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Attribute in FQAN format").create("fqan"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Distinguished name (DN) of AC subject").create(
				"acsubject"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints DN of AC issuer (certificate signer)")
				.create("acissuer"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Time (in seconds) until AC expires")
				.create("actimeleft"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints AC serial number").create("serial"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints Server URI").create("uri"));
		cliOptions.addOption(OptionBuilder.withDescription("Prints content of KeyUsage extension").create("keyusage"));


		/* Sub options for -exists */
		cliOptions.addOption(OptionBuilder.withDescription("[option to -exists] time requirement for proxy to be valid")
				.withArgName("H:M").hasArgs(2).withValueSeparator(':').create("valid"));
		cliOptions.addOption(OptionBuilder
				.withDescription("[option to -exists] time requirement for proxy to be valid (deprecated, use -valid instead)")
				.hasArg().withArgName("H").create("hours"));
		cliOptions.addOption(OptionBuilder
				.withDescription("[option to -exists] strength requirement for proxy to be valid").hasArg().withArgName("B")
				.create("bits"));

	}


	/**
	 * Prints usage information
	 */
	private static void usage() {

		int lineWidth = 120;
		String header = "options:";
		String footer = "";

		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(lineWidth, "java -cp ... org.italiangrid.vomsclients.VomsProxyInfo", header, cliOptions,
				footer);
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
			System.err.println("Error parsing command line arguments: " + e.getMessage());
			usage();
			System.exit(1);
		}
	}
}
