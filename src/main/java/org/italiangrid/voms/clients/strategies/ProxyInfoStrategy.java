package org.italiangrid.voms.clients.strategies;

import org.italiangrid.voms.clients.ProxyInfoParams;

/**
 * Query a VOMS proxy as described by the {@link ProxyInfoParams} object passed
 * as argument.
 * 
 * @param options
 *            the options to query the proxy
 */

public interface ProxyInfoStrategy {

	public void printProxyInfo(ProxyInfoParams options);

}
