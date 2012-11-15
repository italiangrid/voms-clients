package org.italiangrid.voms.clients.impl;

import org.italiangrid.voms.ac.ValidationResultListener;
import org.italiangrid.voms.store.VOMSTrustStoreStatusListener;

import eu.emi.security.authn.x509.ValidationErrorListener;

public interface ProxyInfoListenerAdapter extends ValidationErrorListener,
		ValidationResultListener, VOMSTrustStoreStatusListener {

	public void notifyProxyNotFound();

	public void logInfoMessage(String msg);

}
