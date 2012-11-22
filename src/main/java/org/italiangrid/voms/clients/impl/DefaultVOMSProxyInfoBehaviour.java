package org.italiangrid.voms.clients.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSError;
import org.italiangrid.voms.VOMSValidators;
import org.italiangrid.voms.ac.VOMSACValidator;
import org.italiangrid.voms.ac.ValidationResultListener;
import org.italiangrid.voms.ac.impl.DefaultVOMSValidator;
import org.italiangrid.voms.clients.ProxyInfoParams;
import org.italiangrid.voms.clients.ProxyInfoParams.PrintOption;
import org.italiangrid.voms.clients.strategies.ProxyInfoStrategy;
import org.italiangrid.voms.clients.util.MessageLogger;
import org.italiangrid.voms.clients.util.TimeUtils;
import org.italiangrid.voms.clients.util.VOMSAttributesPrinter;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
import org.italiangrid.voms.store.VOMSTrustStore;
import org.italiangrid.voms.store.VOMSTrustStoreStatusListener;
import org.italiangrid.voms.store.impl.DefaultVOMSTrustStore;
import org.italiangrid.voms.util.CertificateValidatorBuilder;
import eu.emi.security.authn.x509.ValidationErrorListener;
import eu.emi.security.authn.x509.helpers.pkipath.AbstractValidator;
import eu.emi.security.authn.x509.helpers.proxy.ProxyHelper;
import eu.emi.security.authn.x509.impl.CertificateUtils;
import eu.emi.security.authn.x509.impl.FormatMode;
import eu.emi.security.authn.x509.impl.PEMCredential;
import eu.emi.security.authn.x509.proxy.ProxyType;

public class DefaultVOMSProxyInfoBehaviour implements ProxyInfoStrategy {

	private PEMCredential proxyCredential;

	VOMSTrustStoreStatusListener listenerTrust;
	ValidationResultListener listenerValidationResult;

	private VOMSACValidator acValidator = null;

	private final String[] keyUsagesValues = { "Digital Signature",
			"Non Repudiation", "Key Encipherment", "Data Encipherment",
			"Key Agreement", "Key CertSign", "CRL Sign", "Encipher Only",
			"Decipher Only" };

	ArrayList<String> proxyKeyUsageList = new ArrayList<String>();

	private final MessageLogger logger;

	public DefaultVOMSProxyInfoBehaviour(MessageLogger logger,
			InitListenerAdapter listenerHelper) {

		this.logger = logger;
		this.listenerTrust = listenerHelper;
		this.listenerValidationResult = listenerHelper;

	}

	private void initValidator(ProxyInfoParams params) {

		if (acValidator == null) {

			String trustAnchorsDir = DefaultVOMSValidator.DEFAULT_TRUST_ANCHORS_DIR;

			VOMSTrustStore trust = new DefaultVOMSTrustStore(listenerTrust);

			ValidationErrorListener certChainValidationErrorListener = null;

			AbstractValidator certChainValidator = CertificateValidatorBuilder
					.buildCertificateValidator(trustAnchorsDir,
							certChainValidationErrorListener);

			acValidator = VOMSValidators.newValidator(trust,
					certChainValidator, listenerValidationResult);

		}
	}

	@Override
	public void printProxyInfo(ProxyInfoParams params) {

		List<VOMSAttribute> listVOMSAttributes = new ArrayList<VOMSAttribute>();
		X509Certificate[] proxyChain = null;

		if (params.getProxyFile() == null)
			params.setProxyFile(VOMSProxyPathBuilder.buildProxyPath());

		FileInputStream inputProxyFileName = null;
		try {
			inputProxyFileName = new FileInputStream(params.getProxyFile());
		} catch (FileNotFoundException e) {
			throw new VOMSError("Proxy not found: " + e.getMessage(), e);
		}

		try {
			proxyCredential = new PEMCredential(inputProxyFileName, null);
		} catch (Exception e) {
			throw new VOMSError("Proxy not found: " + e.getMessage(), e);
		}

		File proxyFilePath = new File(params.getProxyFile());

		proxyChain = proxyCredential.getCertificateChain();

		initValidator(params);

		listVOMSAttributes = acValidator.parse(proxyChain);

		resolveProxyKeyUsage();

		if (!params.containsOption(PrintOption.SKIP_AC)) {

			acValidator.validate(proxyChain);
			acValidator.shutdown();

		}

		if (params.containsOption(PrintOption.ALL_OPTIONS)) {

			printProxyStandardInfo(proxyFilePath);

			Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

			MessageLogger logger = new MessageLogger();

			while (it.hasNext()) {

				VOMSAttribute attribute = it.next();
				VOMSAttributesPrinter.printVOMSAttributes(logger,
						MessageLogger.MessageLevel.INFO, attribute);

			}
		}

		if (params.isEmpty()
				|| (params.getNumberOfOptions() == 1 && params
						.containsOption(PrintOption.SKIP_AC))) {
			printProxyStandardInfo(proxyFilePath);
		}

		checkProxyBasicOptions(params, proxyFilePath, proxyChain);
		checkVOMSOptions(params, listVOMSAttributes, proxyChain, proxyFilePath);
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
				throw new VOMSError("The current proxy is not valid: "
						+ e.getMessage(), e);
			}
		}

		if (params.containsOption(PrintOption.PROXY_TIME_VALIDITY)) {
			int period = 0;
			try {
				period = TimeUtils.parseLifetimeInHoursAndSeconds(params
						.getValidTime());
			} catch (ParseException e) {
				throw new VOMSError("Wrong validity format, required 'hh:mm': "
						+ e.getMessage(), e);
			}

			if (!checkTimeValidity(getTimeLeft(proxyChain[0].getNotAfter()),
					period))
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

			if (!checkTimeValidity(getTimeLeft(proxyChain[0].getNotAfter()),
					period))
				throw new VOMSError("Proxy not valid for the specified period");
		}

	}

	/*
	 * Proxy basic options
	 */
	private void checkProxyBasicOptions(ProxyInfoParams params,
			File proxyFilePath, X509Certificate[] proxyChain) {

		if (params.containsOption(PrintOption.TYPE)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			logger.printMessage(convertProxyType(ProxyHelper.getProxyType(
					proxyCredential.getCertificate()).toString()));
		}

		if (params.containsOption(PrintOption.SUBJECT)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			logger.printMessage(getDNFormat(proxyChain[0].getSubjectDN()
					.toString()));
		}

		if (params.containsOption(PrintOption.ISSUER)
				|| params.containsOption(PrintOption.IDENTITY)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			logger.printMessage(getDNFormat(proxyChain[0].getIssuerDN()
					.toString()));
		}

		if (params.containsOption(PrintOption.PROXY_PATH)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			logger.printMessage(proxyFilePath.getAbsolutePath());
		}

		if (params.containsOption(PrintOption.TEXT)) {
			printProxyStandardInfo(proxyFilePath);
			logger.printMessage(CertificateUtils.format(proxyChain,
					FormatMode.MEDIUM));
		}

		if (params.containsOption(PrintOption.CHAIN)) {
			logger.printMessage(CertificateUtils.format(proxyChain,
					FormatMode.FULL));
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

			logger.printMessage(getFormattedTime((getTimeLeft(endDate))));
		}

	}

	/*
	 * Proxy VOMS options
	 */
	private void checkVOMSOptions(ProxyInfoParams params,
			List<VOMSAttribute> listVOMSAttributes,
			X509Certificate[] proxyChain, File proxyFilePath) {

		if (params.containsOption(PrintOption.ACSUBJECT)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			logger.printMessage(getDNFormat(proxyCredential.getCertificate()
					.getIssuerDN().toString()));

		}

		if (params.containsOption(PrintOption.ACTIMELEFT)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

			while (it.hasNext())
				logger.printMessage(getFormattedTime(getTimeLeft(it.next()
						.getVOMSAC().getNotAfter())));
		}

		if (params.containsOption(PrintOption.ACISSUER)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

			while (it.hasNext())
				logger.printMessage(getDNFormat(it.next().getAACertificates()[0]
						.getSubjectDN().toString()));

		}

		if (params.containsOption(PrintOption.ACSERIAL)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

			while (it.hasNext())
				logger.printMessage(it.next().getVOMSAC().getSerialNumber()
						.toString());

		}

		if (params.containsOption(PrintOption.AC_EXISTS)) {
			Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

			while (it.hasNext()) {
				if (!it.next().getVO().equals(params.getACVO()))
					throw new VOMSError("AC not found for VO "
							+ params.getACVO());
			}
		}

		if (params.containsOption(PrintOption.VONAME)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

			while (it.hasNext())
				logger.printMessage(it.next().getVO());
		}

		if (params.containsOption(PrintOption.FQAN)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {

			Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

			List<String> fqans = null;

			while (it.hasNext()) {
				fqans = it.next().getFQANs();

				for (String fqan : fqans)
					logger.printMessage(fqan);
			}
		}

		if (params.containsOption(PrintOption.SERVER_URI)
				&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
			Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

			while (it.hasNext()) {
				VOMSAttribute tmp = it.next();
				logger.printMessage(tmp.getHost() + ":" + tmp.getPort());
			}

		}

	}

	/*
	 * Extracts the list of KeyUsage from the proxy
	 */
	private void resolveProxyKeyUsage() {

		boolean[] keyUsages = proxyCredential.getCertificate().getKeyUsage();

		int index = 0;

		for (boolean key : keyUsages) {

			if (key)
				proxyKeyUsageList.add(keyUsagesValues[index]);
			index++;

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

	private void printProxyStandardInfo(File proxyFilePath) {

		tabularFormatted("subject", getDNFormat(proxyCredential
				.getCertificate().getSubjectDN().toString()));

		tabularFormatted("issuer", getDNFormat(proxyCredential.getCertificate()
				.getIssuerDN().toString()));

		tabularFormatted("identity", getDNFormat(proxyCredential
				.getCertificate().getIssuerDN().toString()));

		tabularFormatted(
				"type",
				convertProxyType(ProxyHelper.getProxyType(
						proxyCredential.getCertificate()).toString()));

		tabularFormatted("strength",
				getKeySize(proxyCredential.getCertificate()));

		tabularFormatted("path", proxyFilePath.getAbsolutePath());

		tabularFormatted("timeleft",
				getFormattedTime(getTimeLeft(proxyCredential.getCertificate()
						.getNotAfter())));

		tabularFormatted("key usage", getProxyKeyUsages());

	}

	private String convertProxyType(String type) {

		String newType = null;

		if (type.equals(ProxyType.LEGACY.name()))
			newType = "LEGACYproxy";
		else if (type.equals(ProxyType.RFC3820.name()))
			newType = "RFCproxy";
		else if (type.equals(ProxyType.DRAFT_RFC.name()))
			newType = "DRAFTproxy";

		return newType;
	}

	private String getDNFormat(String DN) {

		return (new String("/" + DN.replace(',', '/')));

	}

	private boolean checkTimeValidity(long certTimeLeft, int period) {

		long msPeriod = period * 1000;

		if (certTimeLeft < msPeriod)
			return false;
		else
			return true;

	}

	private long getTimeLeft(Date end) {

		Date today = new Date();

		long expireTime = end.getTime();
		long currentTime = today.getTime();

		long timeleft = (expireTime - currentTime);

		if (timeleft <= 0)
			timeleft = 0;

		return timeleft;
	}

	private String getFormattedTime(long timeleft) {

		SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
		df.setTimeZone(TimeZone.getTimeZone("GMT"));

		return (df.format(timeleft));
	}

	private String getKeySize(X509Certificate chain) {
		RSAKey rsaKey = (RSAKey) (chain.getPublicKey());

		return (Integer.toString(rsaKey.getModulus().bitLength()));

	}

	private void tabularFormatted(String name, String value) {

		logger.printMessage(String.format("%-9s %s %s", name, ":", value));
	}

}
