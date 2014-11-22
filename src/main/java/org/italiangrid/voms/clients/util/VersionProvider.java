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

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

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

    final String vomsAPIVersion  = VOMSAttribute.class.getPackage()
      .getImplementationVersion();
    
    final String canlVersion = X509CertChainValidatorExt.class.
      getPackage().getImplementationVersion();
    
    final String bcVersion = X509CertificatePair
      .class.getPackage().getImplementationVersion();
    
    return String.format("voms-api-java/%s canl/%s bouncycastle/%s",
      vomsAPIVersion, canlVersion, bcVersion);
    
  }
}
