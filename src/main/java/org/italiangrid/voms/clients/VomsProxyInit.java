package org.italiangrid.voms.clients;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.clients.impl.DefaultVOMSCommandsParser;
import org.italiangrid.voms.clients.impl.DefaultVOMSProxyInitBehaviour;
import org.italiangrid.voms.clients.options.v2.CLIOption;
import org.italiangrid.voms.clients.options.v2.CommonOptions;
import org.italiangrid.voms.clients.options.v2.ProxyInitOptions;
import org.italiangrid.voms.clients.strategies.ProxyInitStrategy;
import org.italiangrid.voms.clients.util.LifetimeIntervalParser;
import org.italiangrid.voms.clients.util.MessageLogger.MessageLevel;
import org.italiangrid.voms.credential.LoadCredentialsStrategy;
import org.italiangrid.voms.credential.impl.DefaultLoadCredentialsStrategy;

/**
 * 
 * This class implements a command-line voms-proxy-init client.
 * 
 * @author Andrea Ceccanti
 * @author Daniele Andreotti
 * 
 */
public class VomsProxyInit extends AbstractCLI {
	
	private static final String COMMAND_NAME = "voms-proxy-init";
	
	public static void main(String[] args) {
		new VomsProxyInit(args);	
	}
	
	/** The implementation of the VOMS proxy init behaviour **/
	private ProxyInitStrategy proxyInitBehaviour;

	private ProxyInitListenerHelper listenerHelper; 

	public VomsProxyInit(String[] args) {
		super(COMMAND_NAME);
		
		initOptions();		
		parseOptionsFromCommandLine(args);
		listenerHelper = new ProxyInitListenerHelper(logger);
		doInit();
	}
	
	protected void doInit(){
		ProxyInitParams params = getProxyInitParamsFromCommandLine(commandLine);

		try {

			proxyInitBehaviour = getProxyInitBehaviour();
			proxyInitBehaviour.initProxy(params);

		} catch (Throwable t) {
			if (t.getMessage() != null)
				logger.error("%s\n", t.getMessage());
			else{
				logger.error(t, "Error: %s\n", t.getClass().getSimpleName());
			}
		}
	}

	private ProxyInitStrategy getProxyInitBehaviour(){
		
		String home = System.getProperty("user.home");
		LoadCredentialsStrategy loadCredStrategy = new DefaultLoadCredentialsStrategy(home, 
				DEFAULT_TMP_PATH, 
				listenerHelper);
		
		return new DefaultVOMSProxyInitBehaviour(new DefaultVOMSCommandsParser(), 
				listenerHelper, 
				listenerHelper, 
				loadCredStrategy, 
				listenerHelper, 
				listenerHelper);
	}
	
	
	private ProxyInitParams getProxyInitParamsFromCommandLine(
			CommandLine line) {

		ProxyInitParams params = new ProxyInitParams();
		
		if (commandLineHasOption(ProxyInitOptions.ENABLE_STDIN_PWD))
			params.setReadPasswordFromStdin(true);

		if (commandLineHasOption(ProxyInitOptions.LIMITED_PROXY))
			params.setLimited(true);

		if (commandLineHasOption(ProxyInitOptions.CERT_LOCATION))
			params.setCertFile(getOptionValue(ProxyInitOptions.CERT_LOCATION));

		if (commandLineHasOption(ProxyInitOptions.KEY_LOCATION))
			params.setKeyFile(getOptionValue(ProxyInitOptions.KEY_LOCATION));

		if (commandLineHasOption(ProxyInitOptions.AC_VALIDITY))
			params.setAcLifetimeInSeconds(parseACLifeTimeString(
					getOptionValue(ProxyInitOptions.AC_VALIDITY), ProxyInitOptions.AC_VALIDITY));

		if (commandLineHasOption(ProxyInitOptions.AC_LIFETIME))
			params.setAcLifetimeInSeconds(parseACLifeTimeString(
					getOptionValue(ProxyInitOptions.AC_LIFETIME), 
					ProxyInitOptions.AC_LIFETIME));

		if (commandLineHasOption(ProxyInitOptions.VOMS_COMMAND))
			params.setVomsCommands(getOptionValues(ProxyInitOptions.VOMS_COMMAND));

		return params;

	}

	private void initOptions() {

		List<CLIOption> options = new ArrayList<CLIOption>();
		
		options.addAll(Arrays.asList(CommonOptions.values()));
		options.addAll(Arrays.asList(ProxyInitOptions.values()));
		
		initOptions(options);
	}
	
	private int parseACLifeTimeString(String acLifetimeProperty,
			CLIOption option) {

		try {
			
			return LifetimeIntervalParser.parseLifetimeFromString(acLifetimeProperty);

		} catch (java.text.ParseException e) {
			throw new VOMSError("Invalid format for the time interval option '"
					+ option.getLongOptionName()
					+ "'. It should follow the hh:mm pattern.");
		}
	}
}


