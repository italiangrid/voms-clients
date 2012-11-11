package org.italiangrid.voms.clients.util;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSGenericAttribute;

import eu.emi.security.authn.x509.impl.X500NameUtils;

public class VOMSAttributesPrinter {

	public static void printVOMSAttributes(MessageLogger logger, MessageLogger.MessageLevel level, VOMSAttribute attributes){
		
		String holderSubject = X500NameUtils.getReadableForm(attributes.getHolder());
		String issuerSubject = X500NameUtils.getReadableForm(attributes.getIssuer());
		String validityString = TimeUtils.getACValidityAsString(attributes);
				
		logger.formatMessage(level, "=== VO %s extension information ===\n", attributes.getVO());
		logger.formatMessage(level, "VO        : %s\n", attributes.getVO());
		logger.formatMessage(level, "subject   : %s\n", holderSubject);
		logger.formatMessage(level, "issuer    : %s\n", issuerSubject);
		for (String fqan: attributes.getFQANs())
			logger.formatMessage(level, "attribute : %s\n", fqan);
		
		for (VOMSGenericAttribute ga: attributes.getGenericAttributes())
			logger.formatMessage(level, "attribute : %s = %s (%s)\n", ga.getName(), ga.getValue(), ga.getContext());
		
		logger.formatMessage(level, "timeleft  : %s\n", validityString);
		logger.formatMessage(level, "uri       : %s:%d\n", attributes.getHost(), attributes.getPort());
	}
}
