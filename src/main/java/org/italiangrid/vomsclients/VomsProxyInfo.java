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

	private final Options options = new Options();
	private final CommandLineParser cliParser = new GnuParser();
	private final HelpFormatter helpFormatter = new HelpFormatter();


	@SuppressWarnings("static-access")
	protected void setOptions() {

		options.addOption("help", "usage", false, "Displays helps and exits");
		options.addOption(OptionBuilder.withLongOpt("version").withDescription("Displays version").create("version"));
		options.addOption(OptionBuilder.hasArgs().withLongOpt("file <proxyfile>")
				.withDescription("Non-standard location of proxy").create("file"));
		options.addOption(OptionBuilder.withLongOpt("dont-verify-ac").withDescription("Skips AC verification")
				.create("skipac"));

		options.addOption(OptionBuilder.withLongOpt("exists")
				.withDescription("Returns 0 if valid proxy exists, 1 otherwise").create("exists"));
		options.addOption(OptionBuilder.hasArg().withLongOpt("acexists")
				.withDescription("Returns 0 if AC exists corresponding to voname, 1 otherwise").create("acexists"));
		options.addOption(OptionBuilder.hasArgs().withLongOpt("conf <name>")
				.withDescription("Read options from file <name>").create("conf"));
		options.addOption(OptionBuilder.withLongOpt("included").withDescription("Print included file").create("included"));


		options.addOption(OptionBuilder.withLongOpt("chain")
				.withDescription("Prints information about the whole proxy chain (CA excluded)").create("chain"));
		options.addOption(OptionBuilder.withLongOpt("subject").withDescription("Distinguished name (DN) of proxy subject")
				.create("subject"));
		options.addOption(OptionBuilder.withLongOpt("issuer").withDescription("DN of proxy issuer (certificate signer)")
				.create("issuer"));
		options.addOption(OptionBuilder.withLongOpt("identity")
				.withDescription("DN of the identity represented by the proxy").create("identity"));
		options.addOption(OptionBuilder.withLongOpt("type").withDescription("Type of proxy (full or limited)")
				.create("type"));
		options.addOption(OptionBuilder.withLongOpt("timeleft").withDescription("Time (in seconds) until proxy expires")
				.create("timeleft"));
		options.addOption(OptionBuilder.withLongOpt("strength").withDescription("Key size (in bits)").create("strength"));
		options.addOption(OptionBuilder.withLongOpt("all").withDescription("All proxy options in a human readable format")
				.create("all"));
		options.addOption(OptionBuilder.withLongOpt("text").withDescription("All of the certificate").create("text"));
		options.addOption(OptionBuilder.withLongOpt("path").withDescription("Pathname of proxy file").create("path"));
		options.addOption(OptionBuilder.withLongOpt("vo").withDescription("Vo name").create("vo"));
		options.addOption(OptionBuilder.withLongOpt("fqan").withDescription("Attribute in FQAN format").create("fqan"));

		options.addOption(OptionBuilder.withLongOpt("acsubject").withDescription("Distinguished name (DN) of AC subject")
				.create("acsubject"));
		options.addOption(OptionBuilder.withLongOpt("acissuer").withDescription("DN of AC issuer (certificate signer)")
				.create("acissuer"));
		options.addOption(OptionBuilder.withLongOpt("actimeleft").withDescription("Time (in seconds) until AC expires")
				.create("actimeleft"));
		options.addOption(OptionBuilder.withLongOpt("serial").withDescription("AC serial number").create("serial"));
		options.addOption(OptionBuilder.withLongOpt("uri").withDescription("Server URI").create("uri"));
		options.addOption(OptionBuilder.withLongOpt("keyusage").withDescription("Print content of KeyUsage extension")
				.create("keyusage"));
		options.addOption(OptionBuilder.withLongOpt("text").withDescription("All of the certificate").create("text"));


		options.addOption(OptionBuilder.withLongOpt("valid").withDescription("time requirement for proxy to be valid")
				.create("valid"));
		options.addOption(OptionBuilder.withLongOpt("hours")
				.withDescription("time requirement for proxy to be valid (deprecated, use -valid instead)").create("hours"));
		options.addOption(OptionBuilder.withLongOpt("bits").withDescription("strength requirement for proxy to be valid")
				.create("bits"));

	}

	protected void checkArgs(String[] args) {
		try {
			CommandLine line = cliParser.parse(options, args);

			if (line.hasOption("help") || line.hasOption("usage")) {
				helpFormatter.printHelp("VomsProxyInfo", options);
				System.exit(0);
			}

		} catch (ParseException e) {
			log.error(e.getMessage(), e.getCause());
		}
	}


	public VomsProxyInfo(String[] args) {
		setOptions();
		checkArgs(args);
	}
}
