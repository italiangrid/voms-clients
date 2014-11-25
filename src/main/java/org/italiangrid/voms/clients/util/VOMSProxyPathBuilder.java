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
package org.italiangrid.voms.clients.util;

import org.italiangrid.voms.credential.ProxyNamingPolicy;
import org.italiangrid.voms.credential.impl.DefaultLoadCredentialsStrategy;
import org.italiangrid.voms.credential.impl.DefaultProxyPathBuilder;

public class VOMSProxyPathBuilder {
	
	private static final String TMP_PATH = System.getProperty(DefaultLoadCredentialsStrategy.TMPDIR_PROPERTY);
	
	public static String buildProxyPath(){
		
		ProxyNamingPolicy pathBuilder = new DefaultProxyPathBuilder();
		return pathBuilder.buildProxyFileName(TMP_PATH, EffectiveUserIdProvider.getEUID());
		
	}
}
