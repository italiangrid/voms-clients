package org.italiangrid.voms.clients.impl;

import org.italiangrid.voms.ac.ValidationResultListener;
import org.italiangrid.voms.credential.LoadCredentialsEventListener;
import org.italiangrid.voms.request.VOMSProtocolListener;
import org.italiangrid.voms.request.VOMSRequestListener;
import org.italiangrid.voms.request.VOMSServerInfoStoreListener;
import org.italiangrid.voms.store.VOMSTrustStoreStatusListener;

import eu.emi.security.authn.x509.StoreUpdateListener;
import eu.emi.security.authn.x509.ValidationErrorListener;

/**
 * An adapter for CANL and VOMS API event listeners.
 * 
 * @author andreaceccanti
 * 
 */
public interface InitListenerAdapter extends ValidationResultListener,
  ProxyCreationListener, VOMSRequestListener, VOMSServerInfoStoreListener,
  LoadCredentialsEventListener, ValidationErrorListener,
  VOMSTrustStoreStatusListener, VOMSProtocolListener, StoreUpdateListener {

  /**
   * Returns true if a validation error was raised by
   * {@link ValidationErrorListener} or {@link ValidationResultListener}.
   * 
   * @return <code>true</code> if validation errors were raised,
   *         <code>false</code> otherwise
   */
  public boolean hadValidationErrors();

}
