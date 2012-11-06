package org.italiangrid.voms.clients.impl;

import java.util.List;

import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.italiangrid.voms.clients.ProxyInitBehaviour;
import org.italiangrid.voms.clients.ProxyInitOptions;

import eu.emi.security.authn.x509.X509Credential;

/**
 * The default VOMS proxy init behaviour.
 * 
 * @author andreaceccanti
 *
 */
public class DefaultVOMSProxyInitBehaviour implements ProxyInitBehaviour {

	public DefaultVOMSProxyInitBehaviour() {
		
	}

	protected void parseVOMSCommand(String vomsCommand){
		
		
	}
	
	protected List<AttributeCertificate> getAttributeCertificates(List<String> vomsCommands){
		
		return null;
	}
	
	private void createProxy(ProxyInitOptions options, X509Credential credential){
		
	}
	 
	
	public void createProxy(ProxyInitOptions options) {
		
		X509Credential cred = lookupCredential(options);
		
	}

	private X509Credential lookupCredential(ProxyInitOptions options) {
		
		return null;
	}

}
