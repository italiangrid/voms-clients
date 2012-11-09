package org.italiangrid.voms.clients.impl;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.openssl.PasswordFinder;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.ac.ValidationResultListener;
import org.italiangrid.voms.clients.ProxyInitParams;
import org.italiangrid.voms.clients.strategies.ProxyInitStrategy;
import org.italiangrid.voms.clients.strategies.VOMSCommandsParsingStrategy;
import org.italiangrid.voms.clients.util.PasswordFinders;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
import org.italiangrid.voms.credential.CredentialsUtils;
import org.italiangrid.voms.credential.LoadCredentialsStrategy;
import org.italiangrid.voms.request.VOMSACService;
import org.italiangrid.voms.request.VOMSServerInfoStoreListener;
import org.italiangrid.voms.request.impl.DefaultVOMSACRequest;

import eu.emi.security.authn.x509.X509Credential;
import eu.emi.security.authn.x509.proxy.ProxyCertificate;
import eu.emi.security.authn.x509.proxy.ProxyCertificateOptions;
import eu.emi.security.authn.x509.proxy.ProxyGenerator;

/**
 * The default VOMS proxy init behaviour.
 * 
 * @author andreaceccanti
 * 
 */
public class DefaultVOMSProxyInitBehaviour implements ProxyInitStrategy {

	private VOMSCommandsParsingStrategy commandsParser;
	private ValidationResultListener validationResultListener;
	private VOMSRequestListener requestListener;
	private ProxyCreationListener proxyCreationListener;
	private VOMSServerInfoStoreListener serverInfoStoreListener;

	private LoadCredentialsStrategy loadCredStrategy;

	public DefaultVOMSProxyInitBehaviour(VOMSCommandsParsingStrategy commandsParser,
			ValidationResultListener validationListener,
			VOMSRequestListener requestListener,
			LoadCredentialsStrategy loadCredentialStrategy,
			ProxyCreationListener pxCreationListener,
			VOMSServerInfoStoreListener serverInfoStoreListener)
			{
		
		this.commandsParser = commandsParser;
		this.validationResultListener = validationListener;
		this.requestListener = requestListener;
		this.proxyCreationListener = pxCreationListener;
		this.serverInfoStoreListener = serverInfoStoreListener;
		this.loadCredStrategy = loadCredentialStrategy;
		
	}

	public void initProxy(ProxyInitParams params) {

		X509Credential cred = lookupCredential(params);
		if (cred == null)
			throw new VOMSError("No credentials found!");

		List<AttributeCertificate> acs = getAttributeCertificates(params, cred);

		createProxy(params, cred, acs);

	}
	
	
	private void createProxy(ProxyInitParams params,
			X509Credential credential, List<AttributeCertificate> acs) {
		
		String proxyFilePath = VOMSProxyPathBuilder.buildProxyPath();
		
		if (params.getGeneratedProxyFile() != null)
			proxyFilePath = params.getGeneratedProxyFile();
		
		ProxyCertificateOptions certOptions = new ProxyCertificateOptions(credential.getCertificateChain());
		
		certOptions.setProxyPathLimit(params.getPathLenConstraint());
		certOptions.setLimited(params.isLimited());
		certOptions.setLifetime(params.getProxyLifetimeInSeconds());
		certOptions.setType(params.getProxyType());
		
		if (acs != null && !acs.isEmpty())
			certOptions.setAttributeCertificates(acs.toArray(new AttributeCertificate[acs.size()]));
		
		try {
			
			ProxyCertificate cert = ProxyGenerator.generate(certOptions, credential.getKey());
			CredentialsUtils.saveCredentials(new FileOutputStream(proxyFilePath), cert.getCredential());
			proxyCreationListener.proxyCreated(proxyFilePath, cert);
			
		} catch (Throwable t) {
			
			throw new VOMSError("Error creating proxy certificate: "+t.getMessage(), t);
		}
	}

	
	protected List<AttributeCertificate> getAttributeCertificates(
			ProxyInitParams params, X509Credential cred) {

		List<String> vomsCommands = params.getVomsCommands();

		if (vomsCommands == null || vomsCommands.isEmpty())
			return Collections.emptyList();

		Map<String, List<String>> vomsCommandsMap = commandsParser
				.parseCommands(params.getVomsCommands());

		List<AttributeCertificate> acs = new ArrayList<AttributeCertificate>();

		for (String vo : vomsCommandsMap.keySet()) {

			DefaultVOMSACRequest request = new DefaultVOMSACRequest();

			request.setVoName(vo);
			request.setRequestedFQANs(vomsCommandsMap.get(vo));
			request.setTargets(params.getTargets());
			request.setLifetime(params.getAcLifetimeInSeconds());

			VOMSACService acService = new BaseVOMSACService(requestListener, serverInfoStoreListener);

			AttributeCertificate ac = acService.getVOMSAttributeCertificate(
					cred, request);

			if (ac != null)
				acs.add(ac);
		}

		if (!vomsCommandsMap.keySet().isEmpty() && acs.isEmpty())
			throw new VOMSError("Unable to satisfy user request!");
		return acs;
	}

	private X509Credential lookupCredential(ProxyInitParams params) {

		PasswordFinder pf = null;

		if (params.isReadPasswordFromStdin())
			pf = PasswordFinders.getInputStreamPasswordFinder(System.in, System.out);
		else
			// FIXME: Require explictly the console password finder?
			pf = PasswordFinders.getDefault();

		return loadCredStrategy.loadCredentials(pf);
	}

}
