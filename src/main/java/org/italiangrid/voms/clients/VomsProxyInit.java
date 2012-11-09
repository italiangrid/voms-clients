package org.italiangrid.voms.clients;

import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.AC_LIFETIME;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.AC_VALIDITY;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.CERT_LOCATION;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.ENABLE_STDIN_PWD;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.KEY_LOCATION;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.LIMITED_PROXY;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.VOMS_COMMAND;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.clients.impl.DefaultVOMSProxyInitBehaviour;
import org.italiangrid.voms.clients.options.VomsCliOption;
import org.italiangrid.voms.clients.options.VomsClientsCommonOptions;
import org.italiangrid.voms.clients.options.VomsProxyInitOptions;
import org.italiangrid.voms.clients.strategies.ProxyInitStrategy;
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

	/** The parsed command line **/
	private static CommandLine commandLine = null;

	/** The implementation of the VOMS proxy init behaviour **/
	private static ProxyInitStrategy proxyInitBehaviour = new DefaultVOMSProxyInitBehaviour();

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
		helpFormatter.printHelp(lineWidth, "voms-proxy-init [options]", header,
				cliOptions, footer);
	}

	/**
	 * Checks the command line to see whether the display of this CLI
	 * 
	 * @param line
	 */
	private static void displayHelpIfRequested(CommandLine line) {

		if (line.hasOption(VomsClientsCommonOptions.HELP.getLongOpt())
				|| line.hasOption(VomsClientsCommonOptions.USAGE.getLongOpt())) {
			usage();
			System.exit(0);
		}

	}

	private static boolean commandLineHasOption(VomsCliOption option) {

		return commandLine.hasOption(option.getLongOpt());
	}

	private static String getOptionValue(VomsCliOption option) {

		if (commandLineHasOption(option))
			return commandLine.getOptionValue(option.getLongOpt());
		return null;
	}

	private static List<String> getOptionValues(VomsCliOption option) {

		if (commandLineHasOption(option)) {

			String[] values = commandLine.getOptionValues(option.getLongOpt());
			return Arrays.asList(values);
		}

		return null;
	}

	private static int parseACLifeTimeString(String acLifetimeProperty,
			VomsCliOption option) {

		try {

			SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

			Calendar c = Calendar.getInstance();
			Date d;

			d = sdf.parse(acLifetimeProperty.trim());
			c.setTime(d);

			long timeIntevalInSeconds = TimeUnit.HOURS.toSeconds(c
					.get(Calendar.HOUR_OF_DAY))
					+ TimeUnit.MINUTES.toSeconds(c.get(Calendar.MINUTE));

			return (int) timeIntevalInSeconds;

		} catch (java.text.ParseException e) {
			throw new VOMSError("Invalid format for the time interval option '"
					+ option.getLongOpt()
					+ "'. It should follow the hh:mm pattern.");

		}
	}

	private static ProxyInitParams getProxyInitParamsFromCommandLine(
			CommandLine line) {

		ProxyInitParams params = new ProxyInitParams();

		if (commandLineHasOption(ENABLE_STDIN_PWD))
			params.setReadPasswordFromStdin(true);

		if (commandLineHasOption(LIMITED_PROXY))
			params.setLimited(true);

		if (commandLineHasOption(CERT_LOCATION))
			params.setCertFile(getOptionValue(CERT_LOCATION));

		if (commandLineHasOption(KEY_LOCATION))
			params.setKeyFile(getOptionValue(KEY_LOCATION));

		if (commandLineHasOption(AC_VALIDITY))
			params.setAcLifetimeInSeconds(parseACLifeTimeString(
					getOptionValue(AC_VALIDITY), AC_VALIDITY));

		if (commandLineHasOption(AC_LIFETIME))
			params.setAcLifetimeInSeconds(parseACLifeTimeString(
					getOptionValue(AC_LIFETIME), AC_LIFETIME));

		if (commandLineHasOption(VOMS_COMMAND))
			params.setVomsCommands(getOptionValues(VOMS_COMMAND));

		// Add missing options
		return params;

	}

	/**
	 * Parses cli options
	 * 
	 * @throws ParseException
	 */
	private static void parseOptions(String[] args) throws ParseException {

		commandLine = cliParser.parse(cliOptions, args);

		displayHelpIfRequested(commandLine);

		ProxyInitParams params = getProxyInitParamsFromCommandLine(commandLine);

		try {

			proxyInitBehaviour.initProxy(params);

		} catch (Throwable t) {

			System.err.println("Error: " + t.getMessage());
		}

	}

	public static void main(String[] args) {

		initOptions();

		try {

			parseOptions(args);

		} catch (ParseException e) {
			log.error(e.getMessage(), e.getCause());
			System.err.println("Error parsing command line arguments: "
					+ e.getMessage());
			usage();
			System.exit(1);
		}
	}
}
