package org.italiangrid.voms.clients.strategies;

import java.util.List;

import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.italiangrid.voms.clients.ProxyInitParams;
import org.italiangrid.voms.request.VOMSServerInfoStore;

import eu.emi.security.authn.x509.X509Credential;

public interface ACResolverStrategy {

  List<AttributeCertificate> getVOMSACs(ProxyInitParams
      params, X509Credential userCredential, VOMSServerInfoStore serverInfoStore);
}
