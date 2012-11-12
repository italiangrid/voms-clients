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
   * 
   */
  public void warnProxyToRemove(String proxyFile);
  
}
