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
