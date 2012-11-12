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
   * The method implementing the voms-proxy-destroy behaviour.
   * 
   * @param params
   */
  void destroyProxy(ProxyDestroyParams params);

}