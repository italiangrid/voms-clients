// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.strategies;

import org.italiangrid.voms.clients.ProxyInfoParams;



public interface ProxyInfoStrategy {

  /**
   * Query a VOMS proxy as described by the {@link ProxyInfoParams} object passed
   * as argument.
   * 
   * @param options
   *          the options to query the proxy
   */
  public void printProxyInfo(ProxyInfoParams options);

}
