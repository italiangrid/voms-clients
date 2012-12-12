package org.italiangrid.voms.clients.util;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSGenericAttribute;

public class VOMSAttributesPrinter {

	public static void printVOMSAttributes(MessageLogger logger,
			MessageLogger.MessageLevel level, VOMSAttribute attributes) {
		
		String validityString = TimeUtils.getACValidityAsString(attributes);

		logger.formatMessage(level, "=== VO %s extension information ===\n",
				attributes.getVO());
		logger.formatMessage(level, "VO        : %s\n", attributes.getVO());

		logger.formatMessage(level, "subject   : %s\n",
				OpensslNameUtilities.getOpensslSubjectString(attributes.getHolder()));

		logger.formatMessage(level, "issuer    : %s\n",
				OpensslNameUtilities.getOpensslSubjectString(attributes.getIssuer()));
		
		for (String fqan : attributes.getFQANs())
			logger.formatMessage(level, "attribute : %s\n", fqan);

		for (VOMSGenericAttribute ga : attributes.getGenericAttributes())
			logger.formatMessage(level, "attribute : %s = %s (%s)\n",
					ga.getName(), ga.getValue(), ga.getContext());

		logger.formatMessage(level, "timeleft  : %s\n", validityString);
		logger.formatMessage(level, "uri       : %s:%d\n",
				attributes.getHost(), attributes.getPort());
	}

}
