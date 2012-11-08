package org.italiangrid.voms.clients.util;

import org.italiangrid.voms.credential.ProxyPathBuilder;
import org.italiangrid.voms.credential.impl.DefaultProxyPathBuilder;

public class VOMSProxyPathBuilder {
	
	private static final String TMP_PATH = "/tmp";
	
	public static String buildProxyPath(){
		
		ProxyPathBuilder pathBuilder = new DefaultProxyPathBuilder();
		return pathBuilder.buildProxyFilePath(TMP_PATH, EffectiveUserIdProvider.getEUID());
		
	}
}
