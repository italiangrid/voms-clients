// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.impl;

import java.io.File;

import org.italiangrid.voms.clients.ProxyDestroyBehaviour;
import org.italiangrid.voms.clients.ProxyDestroyParams;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
import org.italiangrid.voms.credential.VOMSEnvironmentVariables;

/**
 * 
 * The class implementing the behaviour of the voms-proxy-destroy command.
 * 
 * @author valerioventuri
 *
 */
public class DefaultProxyDestroyBehaviour implements ProxyDestroyBehaviour {

  /**
   * The {@link ProxyDestroyListenerAdapter}.
   * 
   */
  private ProxyDestroyListenerAdapter listener;

  /**
   * Constructor that takes a {@link ProxyDestroyListenerAdapter}.
   * 
   * @param listener
   *          The listener that will receive events related to proxy destroy
   *          events
   */
  public DefaultProxyDestroyBehaviour(ProxyDestroyListenerAdapter listener) {

    this.listener = listener;
  }

  /*
   * (non-Javadoc)
   * 
   * @see
   * org.italiangrid.voms.clients.ProxyDestroyBehaviourInt#destroyProxy(org.
   * italiangrid.voms.clients.ProxyDestroyParams)
   */
  @Override
  public void destroyProxy(ProxyDestroyParams params) {
    String proxyFilePath = VOMSProxyPathBuilder.buildProxyPath();

    String envProxyPath = System
      .getenv(VOMSEnvironmentVariables.X509_USER_PROXY);

    if (envProxyPath != null)
      proxyFilePath = envProxyPath;

    if (params.getProxyFile() != null)
      proxyFilePath = params.getProxyFile();

    File file = new File(proxyFilePath);

    if (!file.exists()) {

      listener.notifyProxyNotFound();

      System.exit(1);
    }

    if (params.isDryRun()) {

      listener.warnProxyToRemove(proxyFilePath);

      System.exit(0);
    }

    file.delete();

  }

}
