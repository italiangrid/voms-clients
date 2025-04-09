// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.impl;

import eu.emi.security.authn.x509.helpers.PasswordSupplier;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
import org.italiangrid.voms.credential.LoadCredentialsEventListener;
import org.italiangrid.voms.credential.impl.AbstractLoadCredentialsStrategy;

import eu.emi.security.authn.x509.X509Credential;

public class LoadProxyCredential extends AbstractLoadCredentialsStrategy {

  final String proxyFile;

  public LoadProxyCredential(LoadCredentialsEventListener listener,
    String proxyFile) {

    super(listener);
    this.proxyFile = proxyFile;
  }

  public LoadProxyCredential(LoadCredentialsEventListener listener) {

    this(listener, null);
  }

  @Override
  public X509Credential loadCredentials(PasswordSupplier passwordFinder) {

    if (proxyFile == null) {
      String envProxyPath = System.getenv(X509_USER_PROXY);
      if (envProxyPath != null)
        return loadProxyCredential(envProxyPath);

      return loadProxyCredential(VOMSProxyPathBuilder.buildProxyPath());
    }

    return loadProxyCredential(proxyFile);
  }

}
