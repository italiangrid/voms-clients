package org.italiangrid.voms.clients.impl;

import org.italiangrid.voms.ac.ValidationResultListener;
import org.italiangrid.voms.credential.LoadCredentialsEventListener;
import org.italiangrid.voms.request.VOMSServerInfoStoreListener;

public interface InitListenerAdapter extends ValidationResultListener,
		ProxyCreationListener, VOMSRequestListener,
		VOMSServerInfoStoreListener, LoadCredentialsEventListener {

}
