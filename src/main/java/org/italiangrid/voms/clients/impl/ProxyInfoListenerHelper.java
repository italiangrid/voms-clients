package org.italiangrid.voms.clients.impl;

import org.italiangrid.voms.clients.util.MessageLogger;

/**
 * Helper to manage messages related to a voms-proxy-info execution
 * 
 * 
 * @author Daniele Andreotti
 * 
 */

public class ProxyInfoListenerHelper extends ProxyInitListenerHelper {

	private MessageLogger logger;

	public ProxyInfoListenerHelper(MessageLogger logger) {
		super(logger);
	}

	public void notifyProxyNotFound() {
		logger.error("\nProxy file doesn't exist\n");
	}

}
