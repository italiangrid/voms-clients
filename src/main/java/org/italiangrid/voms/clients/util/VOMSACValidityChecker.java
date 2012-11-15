package org.italiangrid.voms.clients.util;

import java.util.List;

import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.italiangrid.voms.VOMSValidators;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.italiangrid.voms.ac.ValidationResultListener;
import org.italiangrid.voms.store.VOMSTrustStoreStatusListener;
import org.italiangrid.voms.store.impl.DefaultVOMSTrustStore;

import eu.emi.security.authn.x509.helpers.pkipath.AbstractValidator;

/**
 * Utility class for validating a list of {@link AttributeCertificate}
 * 
 * 
 * @author Daniele Andreotti
 * 
 */

public class VOMSACValidityChecker {

	public static void verifyACs(List<AttributeCertificate> acs,
			VOMSTrustStoreStatusListener vomsTrustStoreListener,
			ValidationResultListener validationResultListener,
			AbstractValidator certChainValidator) {

		VOMSACValidator acValidator = VOMSValidators.newValidator(
				new DefaultVOMSTrustStore(vomsTrustStoreListener),
				certChainValidator, validationResultListener);

		acValidator.validateACs(acs);
	}
}
