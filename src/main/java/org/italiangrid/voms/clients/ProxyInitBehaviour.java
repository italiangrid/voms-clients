package org.italiangrid.voms.clients;

/**
 * The VOMS proxy init CLI behaviour
 * 
 * @author andreaceccanti
 * 
 */
public interface ProxyInitBehaviour {

	/**
	 * Creates a VOMS proxy as described by the {@link ProxyInitOptions} object
	 * passed as argument.
	 * 
	 * @param options
	 *            the options that will drive the proxy creation
	 */
	public void createProxy(ProxyInitOptions options);

}
