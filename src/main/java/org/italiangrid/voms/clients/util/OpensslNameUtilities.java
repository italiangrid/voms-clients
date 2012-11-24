package org.italiangrid.voms.clients.util;

import javax.security.auth.x500.X500Principal;

import eu.emi.security.authn.x509.impl.OpensslNameUtils;
import eu.emi.security.authn.x509.impl.X500NameUtils;
/**
 * Utils to deal with OpenSSL ugly DNs
 * 
 * @author cecco
 *
 */
public class OpensslNameUtilities {
	
	private OpensslNameUtilities(){}
	
	/**
	 * Formats principal in the ugly, extremely non-standard and widely hated
	 * OpenSSL, slash-separated format (which everyone on the Grid uses, btw...). 
	 * 
	 * @param principal the principal for which the DN should be serialized 
	 * @return a string representing the principal in the terrible OpenSSL slash-separated format  
	 */
	public static final String getOpensslSubjectString(X500Principal principal){
		
		String rfcReadableString = X500NameUtils.getReadableForm(principal);
		return OpensslNameUtils.convertFromRfc2253(rfcReadableString, false);
	}

}
