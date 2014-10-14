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

import org.italiangrid.voms.clients.util.MessageLogger;

/**
 * 
 * 
 * @author valerioventuri
 *
 */
public class ProxyDestroyListenerHelper implements ProxyDestroyListenerAdapter {

  /**
   * Message logger.
   * 
   */
  private MessageLogger logger;

  /**
   * 
   * Constructor that takes the message logger.
   * 
   * @param logger the message logger.
   */
  public ProxyDestroyListenerHelper(MessageLogger logger) {
    
    this.logger = logger;
  }

  @Override
  public void notifyProxyNotFound() {
    
    logger.info("\nProxy file doesn't exist or has bad permissions\n");
  }

  @Override
  public void warnProxyToRemove(String proxyFile) {
    
    logger.info("Would remove %s", proxyFile);
  }

  
}
