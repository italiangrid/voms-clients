package org.italiangrid.voms.clients.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.bouncycastle.openssl.PasswordFinder;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.clients.ProxyInitBehaviour;
import org.italiangrid.voms.clients.ProxyInitOptions;
import org.italiangrid.voms.credential.UserCredentials;
import org.italiangrid.voms.request.VOMSACService;
import org.italiangrid.voms.request.impl.DefaultVOMSACRequest;
import org.italiangrid.voms.request.impl.DefaultVOMSACService;

import eu.emi.security.authn.x509.X509Credential;

/**
 * The default VOMS proxy init behaviour.
 * 
 * @author andreaceccanti
 * 
 */
public class DefaultVOMSProxyInitBehaviour implements ProxyInitBehaviour {

	private VOMSCommandsParser commandsParser;

	public DefaultVOMSProxyInitBehaviour() {
		this(new DefaultVOMSCommandsParser());
	}

	public DefaultVOMSProxyInitBehaviour(VOMSCommandsParser commandsParser) {
		this.commandsParser = commandsParser;
	}

	public void createProxy(ProxyInitOptions options) {

		X509Credential cred = lookupCredential(options);
		if (cred == null)
			throw new VOMSError("No credentials found!");

		List<AttributeCertificate> acs = getAttributeCertificates(options, cred);

		createProxy(options, cred, acs);

	}

	private void createProxy(ProxyInitOptions options,
			X509Credential credential, List<AttributeCertificate> acs) {

		// TODO: implement me

	}

	private int parseLifetimeString(String lifetimeString) {

		// FIXME: implement parsing
		return (int) TimeUnit.HOURS.toSeconds(12L);
	}

	protected List<AttributeCertificate> getAttributeCertificates(
			ProxyInitOptions options, X509Credential cred) {

		List<String> vomsCommands = options.getVomsCommands();

		if (vomsCommands == null || vomsCommands.isEmpty())
			return Collections.emptyList();

		Map<String, List<String>> vomsCommandsMap = commandsParser
				.parseCommands(options.getVomsCommands());

		List<AttributeCertificate> acs = new ArrayList<AttributeCertificate>();

		for (String vo : vomsCommandsMap.keySet()) {

			DefaultVOMSACRequest request = new DefaultVOMSACRequest();

			request.setVoName(vo);
			request.setRequestedFQANs(vomsCommandsMap.get(vo));
			request.setTargets(options.getTargets());
			request.setLifetime(parseLifetimeString(options.getLifetimeString()));

			VOMSACService acService = new DefaultVOMSACService();

			AttributeCertificate ac = acService.getVOMSAttributeCertificate(
					cred, request);

			acs.add(ac);
		}

		return acs;
	}

	private X509Credential lookupCredential(ProxyInitOptions options) {

		PasswordFinder pf = null;

		if (options.isReadPasswordFromStdin())
			pf = PasswordFinders.getInputStreamPasswordFinder(System.in);
		else
			// FIXME: Require explictly the console password finder?
			pf = PasswordFinders.getDefault();

		return UserCredentials.loadCredentials(pf);
	}

}
