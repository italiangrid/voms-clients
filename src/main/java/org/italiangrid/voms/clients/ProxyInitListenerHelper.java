package org.italiangrid.voms.clients;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSValidationResult;
import org.italiangrid.voms.clients.impl.InitListenerAdapter;
import org.italiangrid.voms.clients.util.MessageLogger;
import org.italiangrid.voms.request.VOMSACRequest;
import org.italiangrid.voms.request.VOMSServerInfo;

import eu.emi.security.authn.x509.proxy.ProxyCertificate;

public class ProxyInitListenerHelper implements InitListenerAdapter {

	MessageLogger logger;

	public ProxyInitListenerHelper(MessageLogger logger) {
		this.logger = logger;
	}

	@Override
	public void loadCredentialNotification(LoadCredentialOutcome outcome,
			Throwable error, String... locations) {

		if (outcome.equals(LoadCredentialOutcome.FAILURE))
			logger.trace("Credentials couldn't be loaded [%s]: %s\n",
					StringUtils.join(locations, ","), error.getMessage());
		else
			logger.trace("Credentials loaded successfully [%s]\n",
					StringUtils.join(locations, ","));
	}

	@Override
	public void notifyFailure(VOMSACRequest request, VOMSServerInfo endpoint,
			Throwable error) {
		if (endpoint != null)
			logger.error("Error contacting %s:%d for VO %s: %s\n", endpoint
					.getURL().getHost(), endpoint.getURL().getPort(), endpoint
					.getVoName(), error.getMessage());
		else
			logger.error(
					"None of the contacted servers for %s were capable of returning a valid AC for the user.\n",
					request.getVoName());
	}

	@Override
	public void notifyStart(VOMSACRequest request, VOMSServerInfo si) {
		logger.info("Contacting %s:%d [%s] \"%s\"...", si.getURL().getHost(),
				si.getURL().getPort(), si.getVOMSServerDN(), si.getVoName());
	}

	@Override
	public void notifySuccess(VOMSACRequest request, VOMSServerInfo endpoint) {
		logger.info("Done\n");
	}

	@Override
	public void notifyValidationResult(VOMSValidationResult result,
			VOMSAttribute attributes) {

		if (!result.isValid()) {
			logger.error("AC validation error: %s\n",
					StringUtils.join(result.getValidationErrors(), ", "));
		} else
			logger.trace("AC validation succeded. Valid attributes: %s\n",
					attributes.toString());
	}

	@Override
	public void proxyCreated(String proxyPath, ProxyCertificate cert) {
		logger.info("Creating proxy in %s...Done\n\n", proxyPath);
		logger.info("Your proxy is valid until %s", cert.getCredential()
				.getCertificateChain()[0].getNotAfter());
	}

	@Override
	public void serverInfoLoaded(String vomsesPath, VOMSServerInfo info) {
		if (vomsesPath != null)
			logger.trace("Loaded vomses information '%s' from %s.\n", info,
					vomsesPath);
		else
			logger.trace("Loaded vomses information '%s'\n", info);
	}

	@Override
	public void lookupNotification(String vomsesPath) {
		logger.trace("Looking for VOMSES information in %s...\n", vomsesPath);
		
	}

	@Override
	public void noValidVomsesNotification(List<String> searchedPaths) {
		logger.info("No valid VOMSES information found locally while looking in: "+searchedPaths);
	}

}
