package org.italiangrid.voms.clients.impl;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.glite.voms.contact.VOMSErrorMessage;
import org.glite.voms.contact.VOMSWarningMessage;
import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSValidationResult;
import org.italiangrid.voms.clients.util.MessageLogger;
import org.italiangrid.voms.clients.util.MessageLogger.MessageLevel;
import org.italiangrid.voms.clients.util.VOMSAttributesPrinter;
import org.italiangrid.voms.error.VOMSValidationErrorMessage;
import org.italiangrid.voms.request.VOMSACRequest;
import org.italiangrid.voms.request.VOMSServerInfo;
import org.italiangrid.voms.store.LSCInfo;

import eu.emi.security.authn.x509.ValidationError;
import eu.emi.security.authn.x509.impl.X500NameUtils;
import eu.emi.security.authn.x509.proxy.ProxyCertificate;

public class ProxyInitListenerHelper implements InitListenerAdapter {

	MessageLogger logger;

	public ProxyInitListenerHelper(MessageLogger logger) {
		this.logger = logger;
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
			logger.error("\nWARNING: VOMS AC validation failed for the following reasons:");
			for (VOMSValidationErrorMessage m: result.getValidationErrors())
				logger.error("\t%s\n",m.getMessage());
		} else{
			logger.trace("VOMS AC validation succeded.\n");
			VOMSAttributesPrinter.printVOMSAttributes(logger, MessageLevel.TRACE, attributes);
		}
	}

	@Override
	public void proxyCreated(String proxyPath, ProxyCertificate cert) {
		logger.info("\nCreating proxy in %s...Done\n\n", proxyPath);
		logger.info("Your proxy is valid until %s\n", cert.getCredential()
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

	@Override
	public boolean onValidationError(ValidationError error) {
		logger.warning("Certificate validation error: %s\n",error.getMessage());
		return false;
	}

	@Override
	public void notifyCertficateLookupEvent(String dir) {
		logger.trace("Looking for VOMS AA certificates in %s...\n", dir);
		
	}

	@Override
	public void notifyCertificateLoadEvent(X509Certificate cert, File file) {
		String certSubject = X500NameUtils.getReadableForm(cert.getSubjectX500Principal());
		logger.trace("Loaded VOMS AA certificate with subject %s from file %s\n", certSubject, file.getAbsolutePath());
	}

	@Override
	public void notifyLSCLoadEvent(LSCInfo info, File file) {
		logger.trace("Loaded LSC information from file %s: %s\n", file.getAbsolutePath(), info.toString());
		
	}

	@Override
	public void notifyLSCLookupEvent(String dir) {
		logger.trace("Looking for LSC information in %s...\n",dir);
	}



	@Override
	public void notifyCredentialLookup(String... locations) {
		logger.trace("Looking for user credentials in [%s]...\n", StringUtils.join(locations, ","));
		
	}



	@Override
	public void notifyLoadCredentialSuccess(String... locations) {
		logger.trace("Credentials loaded successfully [%s]\n",
				StringUtils.join(locations, ","));
	}



	@Override
	public void notifyLoadCredentialFailure(Throwable error, String... locations) {
		
		logger.trace("Credentials couldn't be loaded [%s]: %s\n",
				StringUtils.join(locations, ","), error.getMessage());
		
	}

	@Override
	public void notifyErrorsInReponse(VOMSErrorMessage[] errors) {
		
		for (VOMSErrorMessage e: errors)
			logger.error("ERROR: VOMS server error %d: %s\n", e.getCode(), e.getMessage());
	}

	@Override
	public void notifyWarningsInResponse(VOMSWarningMessage[] warnings) {
		for (VOMSWarningMessage e: warnings)
			logger.error("WARNING: VOMS server warning %d: %s\n", e.getCode(), e.getMessage());
		
	}
}
