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
