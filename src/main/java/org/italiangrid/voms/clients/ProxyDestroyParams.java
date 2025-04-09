// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

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
