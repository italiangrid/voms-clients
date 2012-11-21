package org.italiangrid.voms.clients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.italiangrid.voms.clients.ProxyInfoParams.PrintOption;
import org.italiangrid.voms.clients.impl.DefaultVOMSProxyInfoBehaviour;
import org.italiangrid.voms.clients.impl.ProxyInfoListenerHelper;
import org.italiangrid.voms.clients.options.CLIOption;
import org.italiangrid.voms.clients.options.CommonOptions;
import org.italiangrid.voms.clients.options.ProxyInfoOptions;
import org.italiangrid.voms.clients.strategies.ProxyInfoStrategy;

/**
 * 
 * This class implements a command-line voms-proxy-info client.
 * 
 * @author Daniele Andreotti
 * 
 */

public class VomsProxyInfo extends AbstractCLI {

	private static final String COMMAND_NAME = "voms-proxy-info";

	private static final int EXIT_ERROR_CODE = 1;

	/** The implementation of the VOMS proxy info behaviour **/
	private ProxyInfoStrategy proxyInfoBehaviour;

	private final ProxyInfoListenerHelper listenerHelper;

	protected VomsProxyInfo(String[] args) {
		super(COMMAND_NAME);

		initOptions();
		parseOptionsFromCommandLine(args);

		listenerHelper = new ProxyInfoListenerHelper(logger);

		execute();
	}

	/**
	 * Initializes command-line options
	 */

	private void initOptions() {

		List<CLIOption> options = new ArrayList<CLIOption>();

		options.addAll(Arrays.asList(CommonOptions.values()));
		options.addAll(Arrays.asList(ProxyInfoOptions.values()));

		initOptions(options);

	}

	private ProxyInfoParams getProxyInfoParamsFromCommandLine(
			CommandLine commandLine) {

		ProxyInfoParams params = new ProxyInfoParams();

		if (commandLineHasOption(ProxyInfoOptions.PROXY_FILENAME))
			params.setProxyFile(getOptionValue(ProxyInfoOptions.PROXY_FILENAME));

		if (commandLineHasOption(ProxyInfoOptions.PRINT_TEXT))
			params.addPrintOption(PrintOption.TEXT);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_CHAIN))
			params.addPrintOption(PrintOption.CHAIN);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_ALL_OPTIONS))
			params.addPrintOption(PrintOption.ALL_OPTIONS);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_TYPE))
			params.addPrintOption(PrintOption.TYPE);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_SUBJECT))
			params.addPrintOption(PrintOption.SUBJECT);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_ISSUER))
			params.addPrintOption(PrintOption.ISSUER);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_IDENTITY))
			params.addPrintOption(PrintOption.IDENTITY);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_PROXY_PATH))
			params.addPrintOption(PrintOption.PROXY_PATH);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_KEYSIZE))
			params.addPrintOption(PrintOption.KEYSIZE);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_TIMELEFT))
			params.addPrintOption(PrintOption.TIMELEFT);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_VONAME))
			params.addPrintOption(PrintOption.VONAME);

		if (commandLineHasOption(ProxyInfoOptions.SKIP_AC_VERIFICATION))
			params.addPrintOption(PrintOption.SKIP_AC);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_ACISSUER))
			params.addPrintOption(PrintOption.ACISSUER);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_ACSUBJECT))
			params.addPrintOption(PrintOption.ACSUBJECT);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_ACSERIAL))
			params.addPrintOption(PrintOption.ACSERIAL);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_ACTIMELEFT))
			params.addPrintOption(PrintOption.ACTIMELEFT);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_FQAN))
			params.addPrintOption(PrintOption.FQAN);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_KEYUSAGE))
			params.addPrintOption(PrintOption.KEYUSAGE);

		if (commandLineHasOption(ProxyInfoOptions.PRINT_SERVER_URI))
			params.addPrintOption(PrintOption.SERVER_URI);

		if (commandLineHasOption(ProxyInfoOptions.PROXY_TIME_VALIDITY)) {
			params.setValidTime(getOptionValue(ProxyInfoOptions.PROXY_TIME_VALIDITY));
			params.addPrintOption(PrintOption.PROXY_TIME_VALIDITY);
		}

		if (commandLineHasOption(ProxyInfoOptions.PROXY_HOURS_VALIDITY)) {
			params.setValidHours(getOptionValue(ProxyInfoOptions.PROXY_HOURS_VALIDITY));
			params.addPrintOption(PrintOption.PROXY_HOURS_VALIDITY);
		}

		if (commandLineHasOption(ProxyInfoOptions.PROXY_STRENGTH_VALIDITY)) {
			params.setKeyLength(getOptionValue(ProxyInfoOptions.PROXY_STRENGTH_VALIDITY));
			params.addPrintOption(PrintOption.PROXY_STRENGTH_VALIDITY);
		}

		if (commandLineHasOption(ProxyInfoOptions.PROXY_EXISTS))
			params.addPrintOption(PrintOption.PROXY_EXISTS);

		if (commandLineHasOption(ProxyInfoOptions.AC_EXISTS)) {
			params.setACVO(getOptionValue(ProxyInfoOptions.AC_EXISTS));
			params.addPrintOption(PrintOption.AC_EXISTS);
		}

		return params;
	}

	@Override
	protected void execute() {
		ProxyInfoParams params = getProxyInfoParamsFromCommandLine(commandLine);

		try {
			proxyInfoBehaviour = new DefaultVOMSProxyInfoBehaviour(logger,
					listenerHelper);
			proxyInfoBehaviour.printProxyInfo(params);

			System.exit(0);

		} catch (Throwable t) {
			logger.error(t);
			System.exit(EXIT_ERROR_CODE);
		}

	}

	public static void main(String[] args) {
		new VomsProxyInfo(args);
	}
}
