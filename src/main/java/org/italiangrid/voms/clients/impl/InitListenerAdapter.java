/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2006-2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
