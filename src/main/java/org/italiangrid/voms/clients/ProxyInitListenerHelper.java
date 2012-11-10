package org.italiangrid.voms.clients;

import org.apache.commons.lang.StringUtils;
import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSValidationResult;
import org.italiangrid.voms.ac.ValidationResultListener;
import org.italiangrid.voms.clients.impl.ProxyCreationListener;
import org.italiangrid.voms.clients.impl.VOMSRequestListener;
import org.italiangrid.voms.clients.util.MessageLogger;
import org.italiangrid.voms.clients.util.MessageLogger.MessageLevel;
import org.italiangrid.voms.credential.LoadCredentialsEventListener;
import org.italiangrid.voms.request.VOMSACRequest;
import org.italiangrid.voms.request.VOMSServerInfo;
import org.italiangrid.voms.request.VOMSServerInfoStoreListener;

import eu.emi.security.authn.x509.proxy.ProxyCertificate;

public class ProxyInitListenerHelper implements 
	ValidationResultListener,
	LoadCredentialsEventListener,
	VOMSRequestListener,
	ProxyCreationListener,
	VOMSServerInfoStoreListener{

	MessageLogger logger;
	
	
	public ProxyInitListenerHelper(MessageLogger logger) {
		this.logger = logger;
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
	public void notifyUnknownVO(VOMSACRequest request) {
		logMessage(MessageLevel.ERROR, 
				"VOMS Server for VO %s is not configured on this host! Check your vomses configuration.\n", 
				request.getVoName());
	}

	@Override
	public void notifyValidationResult(VOMSValidationResult result,
			VOMSAttribute attributes) {
		
		if (!result.isValid()){
			logMessage(MessageLevel.ERROR, "AC validation error: %s\n", StringUtils.join(result.getValidationErrors(), ", "));
		}else
			logMessage(MessageLevel.TRACE, "AC validation succeded. Valid attributes: %s\n", attributes.toString());
	}
	
	@Override
	public void proxyCreated(String proxyPath, ProxyCertificate cert) {
		logMessage(MessageLevel.INFO, "Creating proxy in %s...Done\n\n", proxyPath);
		logMessage(MessageLevel.INFO, "Your proxy is valid until %s", cert.getCredential().getCertificateChain()[0].getNotAfter());
	}

	@Override
	public void serverInfoLoaded(String vomsesPath, VOMSServerInfo info) {
		if (vomsesPath != null)
			logMessage(MessageLevel.TRACE, "Loaded vomses information '%s' from %s.\n", info, vomsesPath);
		else
			logMessage(MessageLevel.TRACE, "Loaded vomses information '%s'\n", info);
	}
	
	private final void logMessage(MessageLevel level, String fmt, Object... args) {
		logger.formatMessage(level, fmt, args);
	}
}
