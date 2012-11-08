package org.italiangrid.voms.clients.strategies;

import org.italiangrid.voms.clients.ProxyInitParams;

/**
 * The VOMS proxy init CLI 
 * 
 * @author andreaceccanti
 * 
 */
public interface ProxyInitStrategy {

	/**
	 * Creates a VOMS proxy as described by the {@link ProxyInitParams} object
	 * passed as argument.
	 * 
	 * @param options
	 *            the options that will drive the proxy creation
	 */
	public void initProxy(ProxyInitParams options);

}
