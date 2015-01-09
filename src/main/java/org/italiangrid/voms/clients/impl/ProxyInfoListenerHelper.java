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
 * Helper to manage messages related to a voms-proxy-info execution
 * 
 * 
 * @author Daniele Andreotti
 * 
 */

public class ProxyInfoListenerHelper extends ProxyInitListenerHelper {

  private MessageLogger logger;

  public ProxyInfoListenerHelper(MessageLogger logger) {

    super(logger);
  }

  public void notifyProxyNotFound() {

    logger.error("\nProxy file doesn't exist\n");
  }

}
