// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients;

/**
 * 
 * 
 * @author valerioventuri
 *
 */
public interface ProxyDestroyBehaviour {

  /**
   * 
   * The method implementing the voms-proxy-destroy behaviour
   * 
   * @param params
   *          the params describing this voms-proxy-destroy invocation
   */
  void destroyProxy(ProxyDestroyParams params);

}