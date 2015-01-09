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
package org.italiangrid.voms.clients;

/**
 * 
 * Holder for parameters used by the {@link VomsProxyDestroy} class.
 * 
 * @author valerioventuri
 *
 */
public class ProxyDestroyParams {

  /**
   * Whether the command should run in dry mode.
   * 
   */
  private boolean dryRun;

  /**
   * Name of the proxy certificate file.
   */
  private String proxyFile;

  /**
   * @return the dryRun
   */
  public boolean isDryRun() {

    return dryRun;
  }

  /**
   * @param dryRun
   *          the dryRun to set
   */
  public void setDryRun(boolean dryRun) {

    this.dryRun = dryRun;
  }

  /**
   * @return the proxyFile
   */
  public String getProxyFile() {

    return proxyFile;
  }

  /**
   * @param proxyFile
   *          the proxyFile to set
   */
  public void setProxyFile(String proxyFile) {

    this.proxyFile = proxyFile;
  }

}
