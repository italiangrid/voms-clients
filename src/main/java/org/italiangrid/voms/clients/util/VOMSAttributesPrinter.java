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

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSGenericAttribute;

public class VOMSAttributesPrinter {

  public static void printVOMSAttributes(MessageLogger logger,
    MessageLogger.MessageLevel level, VOMSAttribute attributes) {

    String validityString = TimeUtils.getValidityAsString(attributes
      .getNotAfter());

    logger.formatMessage(level, "=== VO %s extension information ===\n",
      attributes.getVO());
    logger.formatMessage(level, "VO        : %s\n", attributes.getVO());

    logger.formatMessage(level, "subject   : %s\n",
      OpensslNameUtilities.getOpensslSubjectString(attributes.getHolder()));

    logger.formatMessage(level, "issuer    : %s\n",
      OpensslNameUtilities.getOpensslSubjectString(attributes.getIssuer()));

    for (String fqan : attributes.getFQANs())
      logger.formatMessage(level, "attribute : %s\n", fqan);

    for (VOMSGenericAttribute ga : attributes.getGenericAttributes())
      logger.formatMessage(level, "attribute : %s = %s (%s)\n", ga.getName(),
        ga.getValue(), ga.getContext());

    logger.formatMessage(level, "timeleft  : %s\n", validityString);
    logger.formatMessage(level, "uri       : %s:%d\n", attributes.getHost(),
      attributes.getPort());
  }

}
