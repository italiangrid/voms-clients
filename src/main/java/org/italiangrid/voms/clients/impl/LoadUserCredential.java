package org.italiangrid.voms.clients.impl;

import org.bouncycastle.openssl.PasswordFinder;
import org.italiangrid.voms.credential.LoadCredentialsEventListener;
import org.italiangrid.voms.credential.impl.DefaultLoadCredentialsStrategy;

import eu.emi.security.authn.x509.X509Credential;

public class LoadUserCredential extends DefaultLoadCredentialsStrategy {

	String certFile;
	String keyFile;
	
	String pkcs12File;
	
	public LoadUserCredential(LoadCredentialsEventListener listener, String certFile, String keyFile){
		super(listener);
		this.certFile = certFile;
		this.keyFile = keyFile;
	}
	
	public LoadUserCredential(LoadCredentialsEventListener listener, String pkcs12File){
		super(listener);
		this.pkcs12File = pkcs12File;
	}
	
	@Override
	public X509Credential loadCredentials(PasswordFinder passwordFinder) {
		
		if (pkcs12File != null)
			return loadPKCS12Credential(pkcs12File, passwordFinder);
		
		if (certFile != null && keyFile != null)
			return loadPEMCredential(keyFile, certFile, passwordFinder);
		
		X509Credential cred = loadPEMCredentialFromEnv(passwordFinder);
		
		if (cred == null)
			cred = loadPKCS12CredentialFromEnv(passwordFinder);
		
		if (cred == null)
			cred = loadPEMCredentialsFromGlobusDir(passwordFinder);
		
		if (cred == null)
			cred = loadPKCS12CredentialsFromGlobusDir(passwordFinder);
		return null;
	}

}
