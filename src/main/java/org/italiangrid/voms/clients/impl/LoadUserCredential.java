// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

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
