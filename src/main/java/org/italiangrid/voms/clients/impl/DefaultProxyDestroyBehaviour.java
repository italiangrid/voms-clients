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
package org.italiangrid.voms.clients.impl;

import java.io.File;

import org.italiangrid.voms.clients.ProxyDestroyBehaviour;
import org.italiangrid.voms.clients.ProxyDestroyParams;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;

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

    if (params.getProxyFile() == null) {

      params.setProxyFile(VOMSProxyPathBuilder.buildProxyPath());
    }

    File file = new File(params.getProxyFile());

    if (!file.exists()) {

      listener.notifyProxyNotFound();

      System.exit(1);
    }

    if (params.isDryRun()) {

      listener.warnProxyToRemove(params.getProxyFile());

      System.exit(0);
    }

    file.delete();

  }

}
