// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

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
   * @param command
   *    a command string
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
      version.append(String.format(" bcprov/%s", bcVersion));
    }

    if (bcMailVersion != null) {
      version.append(String.format(" bcpkix/%s", bcMailVersion));
    }

    return version.toString();

  }
}
