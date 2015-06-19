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
/**
 * 
 */
package org.italiangrid.voms.clients.util;

import org.bouncycastle.cert.AttributeCertificateHolder;
import org.bouncycastle.x509.X509CertificatePair;
import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.clients.strategies.ProxyInitStrategy;

import eu.emi.security.authn.x509.X509CertChainValidatorExt;

/**
 * Util class for displaying version information.
 * 
 * @author valerioventuri
 * 
 */
public class VersionProvider {

  /**
   * Display version information.
   * 
   * @param command Command
   */
  public static void displayVersionInfo(String command) {

    String version = ProxyInitStrategy.class.getPackage()
      .getImplementationVersion();

    if (version == null) {
      version = "N/A";
    }

    System.out.format("%s v. %s (%s)\n", command, version,
      getAPIVersionString());
  }

  public static String getAPIVersionString() {

    StringBuilder version = new StringBuilder();

    final String vomsAPIVersion = VOMSAttribute.class.getPackage()
      .getImplementationVersion();

    final String canlVersion = X509CertChainValidatorExt.class.getPackage()
      .getImplementationVersion();

    final String bcVersion = X509CertificatePair.class.getPackage()
      .getImplementationVersion();

    final String bcMailVersion = AttributeCertificateHolder.class.getPackage()
      .getImplementationVersion();

    version.append(String.format("voms-api-java/%s canl/%s", vomsAPIVersion,
      canlVersion));

    if (bcVersion != null) {
      version.append(String.format(" bouncycastle/%s", bcVersion));
    }

    if (bcMailVersion != null) {
      version.append(String.format(" bcmail/%s", bcMailVersion));
    }

    return version.toString();

  }
}
