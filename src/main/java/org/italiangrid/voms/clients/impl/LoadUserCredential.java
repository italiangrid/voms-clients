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

import eu.emi.security.authn.x509.helpers.PasswordSupplier;
import org.italiangrid.voms.credential.LoadCredentialsEventListener;
import org.italiangrid.voms.credential.impl.AbstractLoadCredentialsStrategy;

import eu.emi.security.authn.x509.X509Credential;

public class LoadUserCredential extends AbstractLoadCredentialsStrategy {

  String certFile;
  String keyFile;

  String pkcs12File;

  public LoadUserCredential(LoadCredentialsEventListener listener,
    String certFile, String keyFile) {

    super(listener);
    this.certFile = certFile;
    this.keyFile = keyFile;
  }

  public LoadUserCredential(LoadCredentialsEventListener listener,
    String pkcs12File) {

    super(listener);
    this.pkcs12File = pkcs12File;
  }

  @Override
  public X509Credential loadCredentials(PasswordSupplier passwordFinder) {

    if (pkcs12File != null)
      return loadPKCS12Credential(pkcs12File, passwordFinder);

    if (certFile != null && keyFile != null)
      return loadPEMCredential(keyFile, certFile, passwordFinder);

    return null;
  }

}
