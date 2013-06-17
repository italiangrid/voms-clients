package org.italiangrid.voms.clients.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.VOMSValidators;
import org.italiangrid.voms.ac.VOMSACParser;
import org.italiangrid.voms.asn1.VOMSACUtils;
import org.italiangrid.voms.clients.ProxyInfoParams;
import org.italiangrid.voms.clients.ProxyInfoParams.PrintOption;
import org.italiangrid.voms.clients.strategies.ProxyInfoStrategy;
import org.italiangrid.voms.clients.util.MessageLogger;
import org.italiangrid.voms.clients.util.OpensslNameUtilities;
import org.italiangrid.voms.clients.util.TimeUtils;
import org.italiangrid.voms.clients.util.VOMSAttributesPrinter;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;

import eu.emi.security.authn.x509.helpers.proxy.ExtendedProxyType;
import eu.emi.security.authn.x509.helpers.proxy.ProxyHelper;
import eu.emi.security.authn.x509.impl.CertificateUtils;
import eu.emi.security.authn.x509.impl.FormatMode;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.proxy.ProxyUtils;

public class DefaultVOMSProxyInfoBehaviour implements ProxyInfoStrategy {

	private PEMCredential proxyCredential;

	private VOMSACParser acParser = null;

	private final String[] keyUsagesValues = { "Digital Signature",
		"Non Repudiation", "Key Encipherment", "Data Encipherment",
		"Key Agreement", "Key CertSign", "CRL Sign", "Encipher Only",
		"Decipher Only" };

	ArrayList<String> proxyKeyUsageList = new ArrayList<String>();

	private final MessageLogger logger;

	public DefaultVOMSProxyInfoBehaviour(MessageLogger logger,
		InitListenerAdapter listenerAdapter) {

		this.logger = logger;

	}

	@Override
	public void printProxyInfo(ProxyInfoParams params) {

		List<VOMSAttribute> attributes = new ArrayList<VOMSAttribute>();
		
		X509Certificate[] proxyChain = null;

		String proxyFilePath = VOMSProxyPathBuilder.buildProxyPath();

		String envProxyPath = System.getenv("X509_USER_PROXY");

		if (envProxyPath != null)
			proxyFilePath =envProxyPath;
		
		if (params.getProxyFile() != null)
			proxyFilePath = params.getProxyFile();

		FileInputStream proxyInputStream = null;
		
		try {
		
			proxyInputStream = new FileInputStream(proxyFilePath);
		
		} catch (FileNotFoundException e) {
			
			throw new VOMSError("Proxy not found: " + e.getMessage(), e);
		}

		try {
			
			proxyCredential = new PEMCredential(proxyInputStream, (char[]) null);
		
		} catch (Exception e) {
			
			throw new VOMSError("Proxy not found: " + e.getMessage(), e);
		}

		File proxyFile = new File(proxyFilePath);

		proxyChain = proxyCredential.getCertificateChain();

		acParser = VOMSValidators.newParser();
		attributes = acParser.parse(proxyChain);

		resolveProxyKeyUsage();

		if (params.containsOption(PrintOption.ALL_OPTIONS)
			&& !params.containsOption(PrintOption.CHAIN)) {

			printProxyStandardInfo(proxyFile);

			printAC(attributes);

			logger.printMessage("");
		}

		if (params.isEmpty())
			printProxyStandardInfo(proxyFile);

		checkProxyBasicOptions(params, attributes, proxyFile, proxyChain);
		checkVOMSOptions(params, attributes, proxyChain, proxyFile);
		checkValidityOptions(params, proxyChain);

	}

	/*
	 * Groups of options for checking the proxy validity
	 */
	private void checkValidityOptions(ProxyInfoParams params,
		X509Certificate[] proxyChain) {

		if (params.containsOption(PrintOption.PROXY_STRENGTH_VALIDITY)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			if (!getKeySize(proxyChain[0]).equals(params.getKeyLength()))
				throw new VOMSError("Proxy key size is not valid");
		}

		if (params.containsOption(PrintOption.PROXY_EXISTS)) {
			try {
				try {
					proxyChain[0].checkValidity();
				} catch (CertificateNotYetValidException e) {
					throw new VOMSError("Proxy not found: " + e.getMessage(), e);
				}
			} catch (CertificateExpiredException e) {
				throw new VOMSError(
					"The current proxy is not valid: " + e.getMessage(), e);
			}
		}

		if (params.containsOption(PrintOption.PROXY_TIME_VALIDITY)) {
			int period = 0;
			try {
				period = TimeUtils
					.parseLifetimeInHoursAndMinutes(params.getValidTime());
			} catch (ParseException e) {
				throw new VOMSError("Wrong validity format, required 'hh:mm': "
					+ e.getMessage(), e);
			}

			if (!checkTimeValidity(
				TimeUtils.getTimeLeft(proxyChain[0].getNotAfter()), period))
				throw new VOMSError("Proxy not valid for the specified period");
		}

		if (params.containsOption(PrintOption.PROXY_HOURS_VALIDITY)) {
			int period = 0;
			try {
				period = TimeUtils.parseLifetimeInHours(params.getValidHours());
			} catch (ParseException e) {
				throw new VOMSError("Wrong validity format, required 'hh': "
					+ e.getMessage(), e);
			}

			if (!checkTimeValidity(
				TimeUtils.getTimeLeft(proxyChain[0].getNotAfter()), period))
				throw new VOMSError("Proxy not valid for the specified period");
		}

	}

	private void printProxyChain(X509Certificate[] chain) {

		logger.printMessage("=== Proxy Chain Information ===");
		for (X509Certificate c : chain) {
			logger.printMessage(CertificateUtils.format(c, FormatMode.FULL));
			try {

				if (ProxyUtils.isProxy(c)) {
					List<AttributeCertificate> attrs = VOMSACUtils
						.getACsFromCertificate(c);
					if (!attrs.isEmpty())
						logger.printMessage("VOMS extensions: yes.");
					else
						logger.printMessage("VOMS extensions: no.");
				}

			} catch (IOException e) {
				// Swallow exception

			}
			logger.printMessage("");
		}
	}

	/*
	 * Proxy basic options
	 */
	private void checkProxyBasicOptions(ProxyInfoParams params,
		List<VOMSAttribute> listVOMSAttributes, File proxyFilePath,
		X509Certificate[] proxyChain) {

		if (params.containsOption(PrintOption.TYPE)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			logger.printMessage(proxyTypeAsString(proxyChain[0]));
		}

		if (params.containsOption(PrintOption.SUBJECT)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			logger.printMessage(OpensslNameUtilities
				.getOpensslSubjectString(proxyChain[0].getSubjectX500Principal()));
		}

		if (params.containsOption(PrintOption.ISSUER)
			|| params.containsOption(PrintOption.IDENTITY)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			logger.printMessage(OpensslNameUtilities
				.getOpensslSubjectString(proxyChain[0].getIssuerX500Principal()));
		}

		if (params.containsOption(PrintOption.PROXY_PATH)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			logger.printMessage(proxyFilePath.getAbsolutePath());
		}

		if (params.containsOption(PrintOption.CHAIN)) {

			printProxyChain(proxyChain);

			logger.printMessage("=== Proxy Information ===");
			printProxyStandardInfo(proxyFilePath);

			if (params.containsOption(PrintOption.ALL_OPTIONS)) {
				printAC(listVOMSAttributes);
			}

			logger.printMessage("");
		}

		if (params.containsOption(PrintOption.TEXT)) {
			if (!params.containsOption(PrintOption.ALL_OPTIONS)
				&& !params.containsOption(PrintOption.CHAIN)) {
				printProxyStandardInfo(proxyFilePath);
				logger.printMessage("");
			}

			int chainLength = 1;
			if (params.containsOption(PrintOption.CHAIN))
				chainLength = proxyChain.length;
			for (int i = chainLength - 1; i >= 0; i--) {
				logger.printMessage("Certificate:");
				logger.printMessage(CertificateUtils.format(proxyChain[i],
					FormatMode.FULL));
				logger.printMessage("");
			}
		}

		if (params.containsOption(PrintOption.KEYSIZE)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			logger.printMessage(getKeySize(proxyChain[0]));
		}

		if (params.containsOption(PrintOption.KEYUSAGE)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			tabularFormatted("key usage", getProxyKeyUsages());
		}

		if (params.containsOption(PrintOption.TIMELEFT)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			Date endDate = proxyCredential.getCertificate().getNotAfter();

			logger.printMessage(TimeUtils.getValidityAsString(endDate));
		}

	}

	/*
	 * Proxy VOMS options
	 */
	private void checkVOMSOptions(ProxyInfoParams params,
		List<VOMSAttribute> attributes, X509Certificate[] proxyChain,
		File proxyFilePath) {

		if (params.hasACOptions() && attributes.isEmpty())
			throw new VOMSError("No VOMS attributes found!");

		if (params.containsOption(PrintOption.ACSUBJECT)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			for (VOMSAttribute a : attributes)
				logger.printMessage(OpensslNameUtilities.getOpensslSubjectString(a
					.getHolder()));

		}

		if (params.containsOption(PrintOption.ACTIMELEFT)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			for (VOMSAttribute a : attributes)
				logger.printMessage(TimeUtils.getValidityAsString(a.getVOMSAC()
					.getNotAfter()));
		}

		if (params.containsOption(PrintOption.ACISSUER)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			for (VOMSAttribute a : attributes)
				logger.printMessage(OpensslNameUtilities.getOpensslSubjectString(a
					.getAACertificates()[0].getSubjectX500Principal()));

		}

		if (params.containsOption(PrintOption.ACSERIAL)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			for (VOMSAttribute a : attributes)
				logger.printMessage(a.getVOMSAC().getSerialNumber().toString());

		}

		if (params.containsOption(PrintOption.AC_EXISTS)) {

			boolean foundRequestedAC = false;

			for (VOMSAttribute a : attributes) {
				if (params.getACVO().equals(a.getVO())) {
					foundRequestedAC = true;
					break;
				}
			}

			if (!foundRequestedAC)
				throw new VOMSError("AC not found for VO " + params.getACVO());

		}

		if (params.containsOption(PrintOption.VONAME)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			for (VOMSAttribute a : attributes)
				logger.printMessage(a.getVO());
		}

		if (params.containsOption(PrintOption.FQAN)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			for (VOMSAttribute a : attributes) {
				for (String f : a.getFQANs())
					logger.printMessage(f);
			}

		}

		if (params.containsOption(PrintOption.SERVER_URI)
			&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			for (VOMSAttribute a : attributes) {
				logger.formatMessage("%s:%s\n", a.getHost(), a.getPort());
			}
		}

	}

	/*
	 * Extracts the list of KeyUsage from the proxy
	 */
	private void resolveProxyKeyUsage() {

		boolean[] keyUsages = proxyCredential.getCertificate().getKeyUsage();

		if (keyUsages != null) {
			int index = 0;

			for (boolean key : keyUsages) {

				if (key)
					proxyKeyUsageList.add(keyUsagesValues[index]);
				index++;

			}
		}

	}

	/*
	 * Returns a formatted list of KeyUsage
	 */
	private String getProxyKeyUsages() {

		StringBuilder usage = new StringBuilder();

		Iterator<String> it = proxyKeyUsageList.iterator();

		if (it.hasNext())
			usage.append(it.next());

		while (it.hasNext()) {
			usage.append(", " + it.next());
		}

		return usage.toString();
	}

	private void printAC(List<VOMSAttribute> listVOMSAttributes) {

		for (VOMSAttribute a : listVOMSAttributes) {
			VOMSAttributesPrinter.printVOMSAttributes(logger,
				MessageLogger.MessageLevel.INFO, a);

		}
	}

	private void printProxyStandardInfo(File proxyFilePath) {

		String subject = OpensslNameUtilities
			.getOpensslSubjectString(proxyCredential.getCertificate()
				.getSubjectX500Principal());
		String issuer = OpensslNameUtilities
			.getOpensslSubjectString(proxyCredential.getCertificate()
				.getIssuerX500Principal());
		String holder = OpensslNameUtilities.getOpensslSubjectString(ProxyUtils
			.getOriginalUserDN(proxyCredential.getCertificateChain()));

		tabularFormatted("subject", subject);
		tabularFormatted("issuer", issuer);
		tabularFormatted("identity", holder);

		tabularFormatted("type",
			proxyTypeAsString(proxyCredential.getCertificate()));

		tabularFormatted("strength", getKeySize(proxyCredential.getCertificate()));

		tabularFormatted("path", proxyFilePath.getAbsolutePath());

		tabularFormatted("timeleft", TimeUtils.getValidityAsString(proxyCredential
			.getCertificate().getNotAfter()));

		tabularFormatted("key usage", getProxyKeyUsages());

	}

	private String proxyTypeAsString(X509Certificate proxyCert) {

		ExtendedProxyType pt = ProxyHelper.getProxyType(proxyCert);
		boolean limited;
		try {
			limited = ProxyHelper.isLimited(proxyCert);
		} catch (IOException e) {
			throw new VOMSError("Error checking proxy policy:" + e.getMessage(), e);
		}

		String typeString = null;

		switch (pt) {

		case LEGACY:
			typeString = String.format("%s legacy globus proxy", limited ? "limited"
				: "full");
			break;
		case DRAFT_RFC:
			typeString = String.format("Proxy draft (pre-RFC) %s proxy",
				limited ? "limited" : "impersonation");
			break;
		case RFC3820:
			typeString = String.format("RFC3820 compliant %s proxy",
				limited ? "limited" : "impersonation");
			break;
		case NOT_A_PROXY:
			typeString = "EEC";

		}

		return typeString;
	}

	private boolean checkTimeValidity(long certTimeLeft, int period) {

		long msPeriod = period * 1000;

		if (certTimeLeft < msPeriod)
			return false;
		else
			return true;

	}

	private String getKeySize(X509Certificate chain) {

		RSAKey rsaKey = (RSAKey) (chain.getPublicKey());

		return (Integer.toString(rsaKey.getModulus().bitLength()));

	}

	private void tabularFormatted(String name, String value) {

		logger.printMessage(String.format("%-9s %s %s", name, ":", value));
	}

}
