package org.italiangrid.voms.clients.impl;

import java.io.File;
import java.security.cert.X509Certificate;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSValidationResult;
import org.italiangrid.voms.clients.util.MessageLogger;
import org.italiangrid.voms.clients.util.VOMSAttributesPrinter;
import org.italiangrid.voms.clients.util.MessageLogger.MessageLevel;
import org.italiangrid.voms.error.VOMSValidationErrorMessage;
import org.italiangrid.voms.store.LSCInfo;

import eu.emi.security.authn.x509.ValidationError;

/**
 * Helper to manage messages related to a voms-proxy-info execution
 * 
 * 
 * @author Daniele Andreotti
 * 
 */

public class ProxyInfoListenerHelper implements ProxyInfoListenerAdapter {

	private final MessageLogger logger;

	public ProxyInfoListenerHelper(MessageLogger logger) {
		this.logger = logger;
	}

	@Override
	public void notifyProxyNotFound() {

		logger.error("\nProxy file doesn't exist\n");
	}

	@Override
	public void logInfoMessage(String msg) {
		logger.info(msg);
	}

	@Override
	public boolean onValidationError(ValidationError error) {
		logger.warning("Certificate validation error: %s\n", error.getMessage());
		return false;
	}

	@Override
	public void notifyValidationResult(VOMSValidationResult result,
			VOMSAttribute attributes) {

		if (!result.isValid()) {
			logger.error("\nWARNING: VOMS AC validation failed for the following reasons:");
			for (VOMSValidationErrorMessage m : result.getValidationErrors())
				logger.error("\t%s\n", m.getMessage());
		} else {
			logger.trace("VOMS AC validation succeded.\n");
			VOMSAttributesPrinter.printVOMSAttributes(logger,
					MessageLevel.TRACE, attributes);
		}
	}

	@Override
	public void notifyCertficateLookupEvent(String dir) {
		logger.trace("Looking for VOMS AA certificates in %s...\n", dir);

	}

	@Override
	public void notifyLSCLookupEvent(String dir) {
		logger.trace("Looking for LSC information in %s...\n", dir);

	}

	@Override
	public void notifyCertificateLoadEvent(X509Certificate cert, File f) {
		// TODO Auto-generated method stub

	}

	@Override
	public void notifyLSCLoadEvent(LSCInfo info, File file) {
		logger.trace("Loaded LSC information from file %s: %s\n",
				file.getAbsolutePath(), info.toString());

	}
}
