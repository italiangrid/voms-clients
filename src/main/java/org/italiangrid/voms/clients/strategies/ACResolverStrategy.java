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
package org.italiangrid.voms.clients.strategies;

import java.util.List;

import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.italiangrid.voms.clients.ProxyInitParams;
import org.italiangrid.voms.request.VOMSServerInfoStore;

import eu.emi.security.authn.x509.X509Credential;

public interface ACResolverStrategy {

  List<AttributeCertificate> getVOMSACs(ProxyInitParams
      params, X509Credential userCredential, VOMSServerInfoStore serverInfoStore);
}
