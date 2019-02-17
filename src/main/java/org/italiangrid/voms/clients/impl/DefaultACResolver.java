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
package org.italiangrid.voms.clients.impl;

import static java.util.Objects.isNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.clients.ProxyInitParams;
import org.italiangrid.voms.clients.strategies.ACResolverStrategy;
import org.italiangrid.voms.clients.strategies.VOMSCommandsParsingStrategy;
import org.italiangrid.voms.request.VOMSACRequest;
import org.italiangrid.voms.request.VOMSACService;
import org.italiangrid.voms.request.VOMSESLookupStrategy;
import org.italiangrid.voms.request.VOMSProtocolListener;
import org.italiangrid.voms.request.VOMSRequestListener;
import org.italiangrid.voms.request.VOMSServerInfoStore;
import org.italiangrid.voms.request.impl.BaseVOMSESLookupStrategy;
import org.italiangrid.voms.request.impl.DefaultVOMSACRequest;
import org.italiangrid.voms.request.impl.DefaultVOMSACService;
import org.italiangrid.voms.request.impl.DefaultVOMSESLookupStrategy;
import org.italiangrid.voms.util.VOMSFQANNamingScheme;

import eu.emi.security.authn.x509.X509CertChainValidatorExt;
import eu.emi.security.authn.x509.X509Credential;

public class DefaultACResolver implements ACResolverStrategy {


  final VOMSCommandsParsingStrategy commandsParser;

  final VOMSRequestListener requestListener;
  final VOMSProtocolListener protocolListener;

  X509CertChainValidatorExt certChainValidator;

  public DefaultACResolver(VOMSCommandsParsingStrategy commandsParser,
      X509CertChainValidatorExt certChainValidator, VOMSRequestListener requestListener,
      VOMSProtocolListener protocolListener) {
    this.commandsParser = commandsParser;
    this.certChainValidator = certChainValidator;
    this.requestListener = requestListener;
    this.protocolListener = protocolListener;
  }

  protected VOMSESLookupStrategy getVOMSESLookupStrategyFromParams(ProxyInitParams params) {

    if (params.getVomsesLocations() != null && !params.getVomsesLocations().isEmpty())
      return new BaseVOMSESLookupStrategy(params.getVomsesLocations());
    else
      return new DefaultVOMSESLookupStrategy();

  }

  protected List<String> sortFQANsIfRequested(ProxyInitParams params, List<String> unsortedFQANs) {

    if (params.getFqanOrder() != null && !params.getFqanOrder().isEmpty()) {

      Set<String> fqans = new LinkedHashSet<String>();
      for (String fqan : params.getFqanOrder()) {

        if (VOMSFQANNamingScheme.isGroup(fqan))
          fqans.add(fqan);

        if (VOMSFQANNamingScheme.isQualifiedRole(fqan) && unsortedFQANs.contains(fqan))
          fqans.add(fqan);

      }

      fqans.addAll(unsortedFQANs);

      return new ArrayList<String>(fqans);
    }

    return unsortedFQANs;
  }

  @Override
  public List<AttributeCertificate> getVOMSACs(ProxyInitParams params,
      X509Credential userCredential, VOMSServerInfoStore serverInfoStore) {

    List<String> vomsCommands = params.getVomsCommands();

    if (isNull(vomsCommands) || vomsCommands.isEmpty()) {
      return Collections.emptyList();
    }

    Map<String, List<String>> vomsCommandsMap =
        commandsParser.parseCommands(params.getVomsCommands());


    List<AttributeCertificate> acs = new ArrayList<AttributeCertificate>();


    for (Map.Entry<String, List<String>> voCommands : vomsCommandsMap.entrySet()) {

      final String vo = voCommands.getKey();
      final List<String> fqans = voCommands.getValue();

      VOMSACRequest request =
          new DefaultVOMSACRequest.Builder(vo).fqans(sortFQANsIfRequested(params, fqans))
            .targets(params.getTargets())
            .lifetime(params.getAcLifetimeInSeconds())
            .build();

      VOMSACService acService =
          new DefaultVOMSACService.Builder(certChainValidator).requestListener(requestListener)
            .serverInfoStore(serverInfoStore)
            .vomsesLookupStrategy(getVOMSESLookupStrategyFromParams(params))
            .protocolListener(protocolListener)
            .connectTimeout((int) TimeUnit.SECONDS.toMillis(params.getTimeoutInSeconds()))
            .readTimeout((int) TimeUnit.SECONDS.toMillis(params.getTimeoutInSeconds()))
            .skipHostnameChecks(params.isSkipHostnameChecks())
            .build();

      AttributeCertificate ac = acService.getVOMSAttributeCertificate(userCredential, request);

      if (ac != null) {
        acs.add(ac);
      }
    }

    if (!vomsCommandsMap.keySet().isEmpty() && acs.isEmpty())
      throw new VOMSError("User's request for VOMS attributes could not be fulfilled.");

    return acs;
  }

}
