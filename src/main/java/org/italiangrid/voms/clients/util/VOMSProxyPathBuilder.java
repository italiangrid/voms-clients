// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.util;

import org.italiangrid.voms.credential.ProxyNamingPolicy;
import org.italiangrid.voms.credential.impl.DefaultProxyPathBuilder;

public class VOMSProxyPathBuilder {

  private static final String TMP_PATH = "/tmp";

  public static String buildProxyPath() {

    ProxyNamingPolicy pathBuilder = new DefaultProxyPathBuilder();
    return pathBuilder.buildProxyFileName(TMP_PATH,
      EffectiveUserIdProvider.getEUID());

  }
}
