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
	private static final Options cliOptions = new Options();

	/** The CLI options parser **/
	private static final CommandLineParser cliParser = new GnuParser();


	/**
	 * Setup for command options
	 */
	@SuppressWarnings("static-access")
	private void initOptions() {

		cliOptions.addOption("help", "usage", false, "Displays helps and exits");
		cliOptions.addOption(OptionBuilder.withLongOpt("version").withDescription("Displays version").create("version"));
		cliOptions.addOption(OptionBuilder.hasArgs().withLongOpt("file <proxyfile>")
				.withDescription("Non-standard location of proxy").create("file"));
		cliOptions.addOption(OptionBuilder.withLongOpt("dont-verify-ac").withDescription("Skips AC verification")
				.create("skipac"));

		cliOptions.addOption(OptionBuilder.withLongOpt("exists")
				.withDescription("Returns 0 if valid proxy exists, 1 otherwise").create("exists"));
		cliOptions.addOption(OptionBuilder.hasArg().withLongOpt("acexists")
				.withDescription("Returns 0 if AC exists corresponding to voname, 1 otherwise").create("acexists"));
		cliOptions.addOption(OptionBuilder.hasArgs().withLongOpt("conf <name>")
				.withDescription("Read options from file <name>").create("conf"));
		cliOptions.addOption(OptionBuilder.withLongOpt("included").withDescription("Print included file")
				.create("included"));


		cliOptions.addOption(OptionBuilder.withLongOpt("chain")
				.withDescription("Prints information about the whole proxy chain (CA excluded)").create("chain"));
		cliOptions.addOption(OptionBuilder.withLongOpt("subject")
				.withDescription("Distinguished name (DN) of proxy subject").create("subject"));
		cliOptions.addOption(OptionBuilder.withLongOpt("issuer").withDescription("DN of proxy issuer (certificate signer)")
				.create("issuer"));
		cliOptions.addOption(OptionBuilder.withLongOpt("identity")
				.withDescription("DN of the identity represented by the proxy").create("identity"));
		cliOptions.addOption(OptionBuilder.withLongOpt("type").withDescription("Type of proxy (full or limited)")
				.create("type"));
		cliOptions.addOption(OptionBuilder.withLongOpt("timeleft").withDescription("Time (in seconds) until proxy expires")
				.create("timeleft"));
		cliOptions
				.addOption(OptionBuilder.withLongOpt("strength").withDescription("Key size (in bits)").create("strength"));
		cliOptions.addOption(OptionBuilder.withLongOpt("all")
				.withDescription("All proxy options in a human readable format").create("all"));
		cliOptions.addOption(OptionBuilder.withLongOpt("text").withDescription("All of the certificate").create("text"));
		cliOptions.addOption(OptionBuilder.withLongOpt("path").withDescription("Pathname of proxy file").create("path"));
		cliOptions.addOption(OptionBuilder.withLongOpt("vo").withDescription("Vo name").create("vo"));
		cliOptions.addOption(OptionBuilder.withLongOpt("fqan").withDescription("Attribute in FQAN format").create("fqan"));

		cliOptions.addOption(OptionBuilder.withLongOpt("acsubject")
				.withDescription("Distinguished name (DN) of AC subject").create("acsubject"));
		cliOptions.addOption(OptionBuilder.withLongOpt("acissuer").withDescription("DN of AC issuer (certificate signer)")
				.create("acissuer"));
		cliOptions.addOption(OptionBuilder.withLongOpt("actimeleft").withDescription("Time (in seconds) until AC expires")
				.create("actimeleft"));
		cliOptions.addOption(OptionBuilder.withLongOpt("serial").withDescription("AC serial number").create("serial"));
		cliOptions.addOption(OptionBuilder.withLongOpt("uri").withDescription("Server URI").create("uri"));
		cliOptions.addOption(OptionBuilder.withLongOpt("keyusage").withDescription("Print content of KeyUsage extension")
				.create("keyusage"));
		cliOptions.addOption(OptionBuilder.withLongOpt("text").withDescription("All of the certificate").create("text"));


		cliOptions.addOption(OptionBuilder.withLongOpt("valid").withDescription("time requirement for proxy to be valid")
				.create("valid"));
		cliOptions.addOption(OptionBuilder.withLongOpt("hours")
				.withDescription("time requirement for proxy to be valid (deprecated, use -valid instead)").create("hours"));
		cliOptions.addOption(OptionBuilder.withLongOpt("bits")
				.withDescription("strength requirement for proxy to be valid").create("bits"));

	}


	private void parseCommandLineOptions(String[] args) {
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


	/**
	 * Prints usage information
	 */
	static void usage() {

		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("VomsProxyInfo", cliOptions);
	}

	public VomsProxyInfo(String[] args) {
		initOptions();
		parseCommandLineOptions(args);
	}
}
