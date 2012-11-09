package org.italiangrid.voms.clients.impl;

import eu.emi.security.authn.x509.proxy.ProxyCertificate;

public interface ProxyCreationListener {
	
	public void proxyCreated(String proxyPath, ProxyCertificate proxy);

}
