// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.italiangrid.voms.clients.ProxyInitParams;
import org.italiangrid.voms.clients.strategies.ACResolverStrategy;
import org.italiangrid.voms.request.VOMSACRequest;
import org.italiangrid.voms.request.VOMSRequestListener;
import org.italiangrid.voms.request.VOMSServerInfoStore;
import org.italiangrid.voms.request.impl.DefaultVOMSACRequest;
import org.italiangrid.voms.request.impl.FakeVOMSACService;

import eu.emi.security.authn.x509.X509Credential;

public class FakeACResolver implements ACResolverStrategy {

  final FakeVOMSACService acService;
  
  public FakeACResolver(VOMSRequestListener listener) {
    acService = FakeVOMSACService.newInstanceFromProperties(listener);
  }

  @Override
  public List<AttributeCertificate> getVOMSACs(ProxyInitParams params,
      X509Credential userCredential, VOMSServerInfoStore serverInfoStore) {
    
    VOMSACRequest request =
        new DefaultVOMSACRequest.Builder(acService.getAcParams().getVo())
          .build();
    
    AttributeCertificate ac = acService.getVOMSAttributeCertificate(userCredential, request); 
    
    if (ac == null) {
      return Collections.emptyList();
    }
    
    return Arrays.asList(ac); 
    
  }

}
