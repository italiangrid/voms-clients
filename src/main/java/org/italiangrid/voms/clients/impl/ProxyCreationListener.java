package org.italiangrid.voms.clients.impl;

import java.util.List;

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
	 * @param warnings a list of warnings related to this proxy creation
	 */
	public void proxyCreated(String proxyPath, ProxyCertificate proxy, List<String> warnings);

}
