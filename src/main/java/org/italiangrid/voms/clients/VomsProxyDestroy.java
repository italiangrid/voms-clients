package org.italiangrid.voms.clients;

import java.io.File;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.italiangrid.voms.clients.util.OptionsFileLoader;
import org.italiangrid.voms.clients.util.VersionProvider;
import org.italiangrid.voms.credential.ProxyPathBuilder;
import org.italiangrid.voms.credential.impl.DefaultProxyPathBuilder;

/**
 * Class implementing voms-proxy-destroy.
 * 
 * @author valerioventuri
 * 
 */
public class VomsProxyDestroy {

	/**
	 * Commons CLI {@link Options} object for holding a set of command line
	 * options.
	 * 
	 */
	private Options options = new Options();

	/**
	 * Commons CLI {@link CommandLine} object for holding options after parsing,
	 * and get them in the code.
	 * 
	 */
	private CommandLine commandLine;

	/**
	 * Whether the command should produce extra output for debug purposes.
	 * 
	 */
	private boolean debug;

	/**
	 * Whether the command should produce a minimal output.
	 * 
	 */
	private boolean quiet;

	/**
	 * Whether the command should run in dry mode.
	 * 
	 */
	private boolean dryRun;

	/**
	 * Name of the proxy certificate file.
	 */
	private String proxyFile;

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		new VomsProxyDestroy(args);
	}

	public VomsProxyDestroy(String[] args) {

		initOptions();
		parseOptions(args);
		doDestroy();
	}

	private void initOptions() {

		options.addOption("h", "help", false, "Displays usage");
		options.addOption("v", "version", false, "Displays version");
		options.addOption("debug", false, "Enables extra debug output");
		options.addOption("f", "file", true, "Specifies proxy file name");
		options.addOption("dry", false, "Only go in dryrun mode");
		options.addOption("c", "conf", true, "Load options from file <file>");
		options.addOption("q", "quiet", false, "Quiet mode, minimal output");
	}

	private void parseOptions(String[] args) {

		CommandLineParser commandLineParser = new GnuParser();

		try {

			commandLine = commandLineParser.parse(options, args);

			if (commandLine.hasOption("conf")) {

				String optionFile = commandLine.getOptionValue("conf");

				String[] reloadedArgs = OptionsFileLoader.loadOptions(args,
						optionFile);

				commandLine = commandLineParser.parse(options, reloadedArgs);
			}

			if (commandLine.hasOption("help")) {

				displaysUsage();

				System.exit(0);
			}

			if (commandLine.hasOption("version")) {

				VersionProvider.displayVersionInfo("voms-proxy-destoy");

				System.exit(0);
			}

			debug = commandLine.hasOption("debug");

			quiet = commandLine.hasOption("quiet");

			dryRun = commandLine.hasOption("dry");

			proxyFile = commandLine.getOptionValue("file");

		} catch (ParseException e) {

			System.err.println("voms-proxy-destroy: " + e.getMessage());

			displaysUsage();

			System.exit(1);
		}

	}

	private void doDestroy() {

		if (proxyFile == null) {

			ProxyPathBuilder proxyPathBuilder = new DefaultProxyPathBuilder();

			int userId = Integer.valueOf(System.getProperty("effectiveUserId"));

			proxyFile = proxyPathBuilder.buildProxyFilePath("/tmp", userId);
		}

		File file = new File(proxyFile);

		if (!file.exists()) {

			if (!quiet) {

				System.err
						.println("\nProxy file doesn't exist or has bad permissions\n");
			}

			System.exit(1);
		}

		if (dryRun) {

			System.out.println("Would remove " + proxyFile);

			System.exit(0);
		}

		file.delete();
	}

	private void displaysUsage() {

		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp("VomsProxyDestroy", options);
	}

}
