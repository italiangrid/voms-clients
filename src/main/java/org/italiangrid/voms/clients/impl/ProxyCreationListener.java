package org.italiangrid.voms.clients.impl;

import eu.emi.security.authn.x509.proxy.ProxyCertificate;
/**
 * A listener which informs of succesfull creation of a VOMS proxy certificate
 * @author andreaceccanti
 *
 */
public interface ProxyCreationListener {
	
	/**
	 * Informs of the succesfull creation of a VOMS proxy certificate
	 * @param proxyPath the file where the proxy has been saved
	 * @param proxy the {@link ProxyCertificate}
	 */
	public void proxyCreated(String proxyPath, ProxyCertificate proxy);

}
