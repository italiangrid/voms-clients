package org.italiangrid.voms.clients.util;

import java.io.PrintStream;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSGenericAttribute;

import eu.emi.security.authn.x509.impl.X500NameUtils;

public class VOMSAttributesPrinter {

	public static void printVOMSAttributes(PrintStream os, VOMSAttribute attributes){
		
		String holderSubject = X500NameUtils.getReadableForm(attributes.getHolder());
		String issuerSubject = X500NameUtils.getReadableForm(attributes.getIssuer());
		String validityString = TimeUtils.getACValidityAsString(attributes);
				
		os.format("=== VO %s extension information ===\n", attributes.getVO());
		os.format("VO        : %s\n", attributes.getVO());
		os.format("subject   : %s\n", holderSubject);
		os.format("issuer    : %s\n", issuerSubject);
		for (String fqan: attributes.getFQANs())
			os.format("attribute : %s\n", fqan);
		
		for (VOMSGenericAttribute ga: attributes.getGenericAttributes())
			os.format("attribute : %s = %s (%s)\n", ga.getName(), ga.getValue(), ga.getContext());
		
		os.format("timeleft  : %s\n", validityString);
		os.format("uri       : %s:%d\n", attributes.getHost(), attributes.getPort());
	}
}
