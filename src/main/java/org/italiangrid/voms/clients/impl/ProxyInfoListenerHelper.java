package org.italiangrid.voms.clients.impl;

import org.italiangrid.voms.clients.util.MessageLogger;

/**
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
}
