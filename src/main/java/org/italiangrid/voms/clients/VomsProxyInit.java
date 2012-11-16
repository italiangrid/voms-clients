package org.italiangrid.voms.clients;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.cli.CommandLine;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.clients.impl.DefaultVOMSCommandsParser;
import org.italiangrid.voms.clients.impl.DefaultVOMSProxyInitBehaviour;
import org.italiangrid.voms.clients.impl.ProxyInitListenerHelper;
import org.italiangrid.voms.clients.options.CLIOption;
import org.italiangrid.voms.clients.options.CommonOptions;
import org.italiangrid.voms.clients.options.ProxyInitOptions;
import org.italiangrid.voms.clients.strategies.ProxyInitStrategy;
import org.italiangrid.voms.clients.util.TimeUtils;
import org.italiangrid.voms.util.VOMSFQANNamingScheme;

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
	
	private static final int[] SUPPORTED_KEY_SIZES = {512,1024,2048,4096};

	private static final int EXIT_ERROR_CODE = 1;
	
	public static void main(String[] args) {
		new VomsProxyInit(args);	
	}
	
	/** The implementation of the VOMS proxy init behaviour **/
	private ProxyInitStrategy proxyInitBehaviour;

	private ProxyInitListenerHelper listenerHelper; 

	public VomsProxyInit(String[] args) {
		super(COMMAND_NAME);
		
		try{
			initOptions();
			parseOptionsFromCommandLine(args);
			listenerHelper = new ProxyInitListenerHelper(logger);
			execute();
		}catch(Throwable t){
			if (logger != null)
				logger.error(t);
			else{
				System.err.println(t.getMessage());
				t.printStackTrace(System.err);
			}
			System.exit(EXIT_ERROR_CODE);
		}
	}

	private ProxyInitStrategy getProxyInitBehaviour(){
		
		return new DefaultVOMSProxyInitBehaviour(new DefaultVOMSCommandsParser(), 
				listenerHelper);
	}
	
	private int parseKeySize(String keySizeParam){
		try{
			
			int keySize = Integer.parseInt(keySizeParam);
			
			if (Arrays.binarySearch(SUPPORTED_KEY_SIZES, keySize) < 0)
				throw new VOMSError("Unsupported key size:"+keySize);
			
			return keySize;
			
		}catch(NumberFormatException e){
			throw new VOMSError("Invalid input for key size parameter. Please provide a valid key size value.",e);
		}
	}
	private ProxyInitParams getProxyInitParamsFromCommandLine(
			CommandLine line) {

		ProxyInitParams params = new ProxyInitParams();
		
		if (commandLineHasOption(ProxyInitOptions.KEY_SIZE)){
			params.setKeySize(parseKeySize(getOptionValue(ProxyInitOptions.KEY_SIZE)));
		}
		if (commandLineHasOption(ProxyInitOptions.ENABLE_STDIN_PWD))
			params.setReadPasswordFromStdin(true);

		if (commandLineHasOption(ProxyInitOptions.LIMITED_PROXY))
			params.setLimited(true);

		if (commandLineHasOption(ProxyInitOptions.CERT_LOCATION))
			params.setCertFile(getOptionValue(ProxyInitOptions.CERT_LOCATION));

		if (commandLineHasOption(ProxyInitOptions.KEY_LOCATION))
			params.setKeyFile(getOptionValue(ProxyInitOptions.KEY_LOCATION));

		if (commandLineHasOption(ProxyInitOptions.AC_VALIDITY))
			params.setAcLifetimeInSeconds(parseLifeTimeInHoursAndMinutesString(
					getOptionValue(ProxyInitOptions.AC_VALIDITY), ProxyInitOptions.AC_VALIDITY));

		if (commandLineHasOption(ProxyInitOptions.AC_LIFETIME))
			params.setAcLifetimeInSeconds(parseLifeTimeInHoursAndMinutesString(
					getOptionValue(ProxyInitOptions.AC_LIFETIME), 
					ProxyInitOptions.AC_LIFETIME));

		if (commandLineHasOption(ProxyInitOptions.VOMS_COMMAND))
			params.setVomsCommands(getOptionValues(ProxyInitOptions.VOMS_COMMAND));
		
		if (commandLineHasOption(ProxyInitOptions.VERIFY_CERT))
			params.setValidateUserCredential(true);
		
		if (commandLineHasOption(ProxyInitOptions.PROXY_NOREGEN))
			params.setNoRegen(true);
		
		if (commandLineHasOption(ProxyInitOptions.SKIP_AC_VERIFICATION))
			params.setVerifyAC(false);
		
		if (commandLineHasOption((ProxyInitOptions.PROXY_LIFETIME_IN_HOURS)))
			params.setProxyLifetimeInSeconds(parseLifetimeInHoursString(getOptionValue(ProxyInitOptions.PROXY_LIFETIME_IN_HOURS), 
					ProxyInitOptions.PROXY_LIFETIME_IN_HOURS));

		if (commandLineHasOption(ProxyInitOptions.FQANS_ORDERING))
			params.setFqanOrder(parseFQANOrdering(getOptionValue(ProxyInitOptions.FQANS_ORDERING)));
		
		return params;

	}

	private List<String> parseFQANOrdering(String orderingParam){
		 
		String[] orderStrings = orderingParam.split(",");
		
		for (String fqan: orderStrings)
			VOMSFQANNamingScheme.checkSyntax(fqan);
		
		return Arrays.asList(orderStrings); 
		
	}
	private void initOptions() {

		List<CLIOption> options = new ArrayList<CLIOption>();
		
		options.addAll(Arrays.asList(CommonOptions.values()));
		options.addAll(Arrays.asList(ProxyInitOptions.values()));
		
		initOptions(options);
	}
	
	
	private int parseLifetimeInHoursString(String proxyLifetimeProperty, CLIOption option){
		
		try{
			return TimeUtils.parseLifetimeInHours(proxyLifetimeProperty);
		}catch (ParseException e){
			throw new VOMSError("Invalid format for the time interval option '"
					+ option.getLongOptionName()
					+ "'. It should follow the hh pattern.",e);
		}
		
	}
	private int parseLifeTimeInHoursAndMinutesString(String acLifetimeProperty,
			CLIOption option) {

		try {
			
			return TimeUtils.parseLifetimeInHoursAndSeconds(acLifetimeProperty);

		} catch (ParseException e) {
			throw new VOMSError("Invalid format for the time interval option '"
					+ option.getLongOptionName()
					+ "'. It should follow the hh:mm pattern.",e);
		}
	}

	@Override
	protected void execute() {
		ProxyInitParams params = getProxyInitParamsFromCommandLine(commandLine);

		try {

			proxyInitBehaviour = getProxyInitBehaviour();
			proxyInitBehaviour.initProxy(params);

		} catch (Throwable t) {
			logger.error(t);
			System.exit(EXIT_ERROR_CODE);
		}
	}
}


