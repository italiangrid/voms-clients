package org.italiangrid.voms.clients.impl;

import org.italiangrid.voms.ac.ValidationResultListener;
import org.italiangrid.voms.credential.LoadCredentialsEventListener;
import org.italiangrid.voms.request.VOMSRequestListener;
import org.italiangrid.voms.request.VOMSServerInfoStoreListener;
import org.italiangrid.voms.store.VOMSTrustStoreStatusListener;

import eu.emi.security.authn.x509.StoreUpdateListener;
import eu.emi.security.authn.x509.ValidationErrorListener;

public interface InitListenerAdapter extends ValidationResultListener,
		ProxyCreationListener, 
		VOMSRequestListener,
		VOMSServerInfoStoreListener, 
		LoadCredentialsEventListener,
		ValidationErrorListener,
		VOMSTrustStoreStatusListener,
		StoreUpdateListener
		{

}
