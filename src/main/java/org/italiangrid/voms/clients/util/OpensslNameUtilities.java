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
package org.italiangrid.voms.clients.util;

import javax.security.auth.x500.X500Principal;

import eu.emi.security.authn.x509.impl.OpensslNameUtils;
import eu.emi.security.authn.x509.impl.X500NameUtils;

/**
 * Utils to deal with OpenSSL ugly DNs
 * 
 * @author cecco
 *
 */
public class OpensslNameUtilities {

  private OpensslNameUtilities() {

  }

  /**
   * Formats principal in the ugly, extremely non-standard and widely hated
   * OpenSSL, slash-separated format (which everyone on the Grid uses, btw...).
   * 
   * @param principal
   *          the principal for which the DN should be serialized
   * @return a string representing the principal in the terrible OpenSSL
   *         slash-separated format
   */
  public static final String getOpensslSubjectString(X500Principal principal) {

    String rfcReadableString = X500NameUtils.getReadableForm(principal);
    return OpensslNameUtils.convertFromRfc2253(rfcReadableString, false);
  }

}
