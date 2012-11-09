package org.italiangrid.voms.clients;

import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.AC_LIFETIME;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.AC_VALIDITY;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.CERT_LOCATION;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.ENABLE_STDIN_PWD;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.KEY_LOCATION;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.LIMITED_PROXY;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.QUIET_MODE;
import static org.italiangrid.voms.clients.options.VomsProxyInitOptions.VOMS_COMMAND;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;
import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.ac.VOMSValidationResult;
import org.italiangrid.voms.ac.ValidationResultListener;
import org.italiangrid.voms.clients.impl.DefaultVOMSCommandsParser;
import org.italiangrid.voms.clients.impl.DefaultVOMSProxyInitBehaviour;
import org.italiangrid.voms.clients.impl.ProxyCreationListener;
import org.italiangrid.voms.clients.impl.VOMSRequestListener;
import org.italiangrid.voms.clients.options.VomsCliOption;
import org.italiangrid.voms.clients.options.VomsClientsCommonOptions;
import org.italiangrid.voms.clients.options.VomsProxyInitOptions;
import org.italiangrid.voms.clients.strategies.ProxyInitStrategy;
import org.italiangrid.voms.credential.LoadCredentialsEventListener;
import org.italiangrid.voms.credential.LoadCredentialsStrategy;
import org.italiangrid.voms.credential.impl.DefaultLoadCredentialsStrategy;
import org.italiangrid.voms.request.VOMSACRequest;
import org.italiangrid.voms.request.VOMSServerInfo;
import org.italiangrid.voms.request.VOMSServerInfoStoreListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.emi.security.authn.x509.proxy.ProxyCertificate;
import eu.emi.security.authn.x509.proxy.ProxyCertificateOptions;

/**
 * 
 * This class implements a command-line voms-proxy-init client.
 * 
 * @author Daniele Andreotti
 * 
 */
public class VomsProxyInit implements 
	ValidationResultListener,
	LoadCredentialsEventListener,
	VOMSRequestListener,
	ProxyCreationListener,
	VOMSServerInfoStoreListener{

	private enum MessageLevel{
		TRACE,
		INFO,
		WARNING,
		ERROR
	}
	
	private static final EnumSet<MessageLevel> DEFAULT_LEVEL = EnumSet.range(MessageLevel.INFO, MessageLevel.ERROR);

	// TODO: move to where all CLIs can benefit
	public static final String DEFAULT_TMP_PATH = "/tmp";

	private static final Logger log = LoggerFactory
		.getLogger(VomsProxyInit.class);

	private static final EnumSet<MessageLevel> QUIET_LEVELS = EnumSet.of(MessageLevel.ERROR, MessageLevel.WARNING);

	private static final EnumSet<MessageLevel> VERBOSE_LEVELS = EnumSet.allOf(MessageLevel.class);

	public static void main(String[] args) {
		new VomsProxyInit(args);	
	}
	private EnumSet<MessageLevel> actualMessageLevel = DEFAULT_LEVEL;
	
	/** The CLI options **/
	private Options cliOptions;
	
	/** The CLI options parser **/
	private CommandLineParser cliParser = new GnuParser();
	/** The parsed command line **/
	private CommandLine commandLine = null;
	private boolean isQuiet = false;
	
	private boolean isVerbose = false;
	
	/** The implementation of the VOMS proxy init behaviour **/
	private ProxyInitStrategy proxyInitBehaviour;

	public VomsProxyInit(String[] args) {
		
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

	private boolean commandLineHasOption(VomsCliOption option) {

		return commandLine.hasOption(option.getLongOpt());
	}

	/**
	 * Checks the command line to see whether the display of this CLI
	 * 
	 * @param line
	 */
	private void displayHelpIfRequested(CommandLine line) {

		if (line.hasOption(VomsClientsCommonOptions.HELP.getLongOpt())
				|| line.hasOption(VomsClientsCommonOptions.USAGE.getLongOpt())) {
			usage();
			System.exit(0);
		}

	}

	private String getOptionValue(VomsCliOption option) {

		if (commandLineHasOption(option))
			return commandLine.getOptionValue(option.getLongOpt());
		return null;
	}

	private List<String> getOptionValues(VomsCliOption option) {

		if (commandLineHasOption(option)) {

			String[] values = commandLine.getOptionValues(option.getLongOpt());
			return Arrays.asList(values);
		}

		return null;
	}

	private ProxyInitStrategy getProxyInitBehaviour(){
		
		String home = System.getProperty("user.home");
		LoadCredentialsStrategy loadCredStrategy = new DefaultLoadCredentialsStrategy(home, DEFAULT_TMP_PATH, this);
		
		return new DefaultVOMSProxyInitBehaviour(new DefaultVOMSCommandsParser(), 
				this, 
				this, 
				loadCredStrategy, 
				this, 
				this);
	}

	private ProxyInitParams getProxyInitParamsFromCommandLine(
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

		
		return params;

	}
	/**
	 * Initializes command-line options
	 */
	private void initOptions() {

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

	@Override
	public void loadCredentialNotification(LoadCredentialOutcome outcome,
			Throwable error, String... locations) {
		
		if (outcome.equals(LoadCredentialOutcome.FAILURE))
			logMessage(MessageLevel.TRACE, "Credentials couldn't be loaded [%s]: %s\n", 
					StringUtils.join(locations,","),
					error.getMessage());
		else
			logMessage(MessageLevel.TRACE, "Credentials loaded successfully [%s]\n", 
					StringUtils.join(locations,","));
	}

	
	protected void logMessage(MessageLevel level, String fmt, Object...args){
		
		if (actualMessageLevel.contains(level)){
			if (level.equals(MessageLevel.ERROR))
				System.err.format(fmt, args);
			else
				System.out.format(fmt, args);
		}
	}
	
	
	@Override
	public void notifyFailure(VOMSACRequest request, VOMSServerInfo endpoint,
			Throwable error) {
		if (endpoint != null)
			logMessage(MessageLevel.ERROR, "Error contacting %s:%d for VO %s: %s\n", endpoint.getURL().getHost(), 
					endpoint.getURL().getPort(),  
					endpoint.getVoName(),
					error.getMessage());
		else
			logMessage(MessageLevel.ERROR, "None of the contacted servers for %s were capable of returning a valid AC for the user.\n", 
					request.getVoName());
	}
	
	@Override
	public void notifyStart(VOMSACRequest request, VOMSServerInfo si) {
		
		logMessage(MessageLevel.INFO, "Contacting %s:%d [%s] \"%s\"...", si.getURL().getHost(), 
				si.getURL().getPort(), si.getVOMSServerDN(), si.getVoName());
	}

	
	
	@Override
	public void notifySuccess(VOMSACRequest request, VOMSServerInfo endpoint) {
		
		logMessage(MessageLevel.INFO, "Done\n");
	}

	@Override
	public void notifyValidationResult(VOMSValidationResult result,
			VOMSAttribute attributes) {
		
		if (!result.isValid()){
			logMessage(MessageLevel.ERROR, "AC validation error: %s\n", StringUtils.join(result.getValidationErrors(), ", "));
		}else
			logMessage(MessageLevel.TRACE, "AC validation succeded. Valid attributes: %s\n", attributes.toString());
	}

	private int parseACLifeTimeString(String acLifetimeProperty,
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

	/**
	 * Parses cli options
	 * 
	 * @throws ParseException
	 */
	private void parseOptions(String[] args) throws ParseException {

		commandLine = cliParser.parse(cliOptions, args);

		setVerbosityFromCommandLine(commandLine);
		displayHelpIfRequested(commandLine);

		ProxyInitParams params = getProxyInitParamsFromCommandLine(commandLine);

		try {

			proxyInitBehaviour = getProxyInitBehaviour();
			proxyInitBehaviour.initProxy(params);

		} catch (Throwable t) {
			if (t.getMessage() != null)
				logMessage(MessageLevel.ERROR, "%s\n", t.getMessage());
			else{
				System.err.println("Error: "+t.getClass().getSimpleName());
				t.printStackTrace(System.err);
			}
		}

	}

	@Override
	public void proxyCreated(String proxyPath, ProxyCertificate cert) {
		logMessage(MessageLevel.INFO, "Creating proxy in %s...Done\n\n", proxyPath);
		logMessage(MessageLevel.INFO, "Your proxy is valid until %s", cert.getCredential().getCertificateChain()[0].getNotAfter());
	}
	
	private void setVerbosityFromCommandLine(CommandLine line){
		
		if (commandLineHasOption(VomsClientsCommonOptions.DEBUG))
			isVerbose = true;
		
		if (commandLineHasOption(QUIET_MODE))
			isQuiet = true;
			
		if (isVerbose && isQuiet)
			throw new VOMSError("Try to understand us: this command cannot be verbose and quiet at the same time!");
		
		if (isVerbose)
			actualMessageLevel = VERBOSE_LEVELS;
		
		if (isQuiet)
			actualMessageLevel = QUIET_LEVELS;
	}

	/**
	 * Prints usage information
	 */
	private void usage() {

		int lineWidth = 120;
		String header = "options:";
		String footer = "";

		HelpFormatter helpFormatter = new HelpFormatter();
		helpFormatter.printHelp(lineWidth, "voms-proxy-init [options]", header,
				cliOptions, footer);
	}

	@Override
	public void notifyUnknownVO(VOMSACRequest request) {
		logMessage(MessageLevel.ERROR, 
				"VOMS Server for VO %s is not configured on this host! Check your vomses configuration.\n", 
				request.getVoName());
	}

	@Override
	public void serverInfoLoaded(String vomsesPath, VOMSServerInfo info) {
		if (vomsesPath != null)
			logMessage(MessageLevel.TRACE, "Loaded vomses information '%s' from %s.\n", info, vomsesPath);
		else
			logMessage(MessageLevel.TRACE, "Loaded vomses information '%s'\n", info);
	}
}


