// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.impl;

/**
 * Interface for events of the voms-proxy-destroy command.
 * 
 * @author valerioventuri
 *
 */
public interface ProxyDestroyListenerAdapter {

  /**
   * 
   * 
   */
  public void notifyProxyNotFound();

  /**
   * @param proxyFile
   *          the name of the proxy that is about to be removed
   */
  public void warnProxyToRemove(String proxyFile);

}
