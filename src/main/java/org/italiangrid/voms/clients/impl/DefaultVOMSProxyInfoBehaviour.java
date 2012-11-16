package org.italiangrid.voms.clients.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.TimeZone;

import org.bouncycastle.asn1.x509.AttributeCertificate;
import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSACParser;
import org.italiangrid.voms.ac.impl.DefaultVOMSACParser;
import org.italiangrid.voms.ac.impl.DefaultVOMSValidator;
import org.italiangrid.voms.clients.ProxyInfoParams;
import org.italiangrid.voms.clients.ProxyInfoParams.PrintOption;
import org.italiangrid.voms.clients.strategies.ProxyInfoStrategy;
import org.italiangrid.voms.clients.util.MessageLogger;
import org.italiangrid.voms.clients.util.TimeUtils;
import org.italiangrid.voms.clients.util.VOMSACValidityChecker;
import org.italiangrid.voms.clients.util.VOMSAttributesPrinter;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
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

	private final ProxyInfoListenerAdapter listener;

	private final ValidationErrorListener certChainValidationErrorListener;

	private final VOMSACParser acParser;

	private AbstractValidator certChainValidator;

	private int returnCode;

	ArrayList<String> proxyKeyUsageList = new ArrayList<String>();

	public DefaultVOMSProxyInfoBehaviour(
			ProxyInfoListenerAdapter listenerAdapter) {

		this.listener = listenerAdapter;
		this.certChainValidationErrorListener = listenerAdapter;

		acParser = new DefaultVOMSACParser();
		returnCode = 0;

	}

	private void initCertChainValidator(ProxyInfoParams params) {

		if (certChainValidator == null) {
			String trustAnchorsDir = DefaultVOMSValidator.DEFAULT_TRUST_ANCHORS_DIR;

			certChainValidator = CertificateValidatorBuilder
					.buildCertificateValidator(trustAnchorsDir,
							certChainValidationErrorListener);
		}
	}

	@Override
	public void getProxyInfo(ProxyInfoParams params) {

		X509Certificate[] proxyChain = null;
		List<VOMSAttribute> listVOMSAttributes = null;

		initCertChainValidator(params);

		boolean printStandardMessage = true;

		try {
			if (params.getProxyFile() == null)
				params.setProxyFile(VOMSProxyPathBuilder.buildProxyPath());

			proxyCredential = new PEMCredential(new FileInputStream(
					params.getProxyFile()), null);

			proxyChain = proxyCredential.getCertificateChain();
			listVOMSAttributes = acParser.parse(proxyCredential
					.getCertificateChain());
			File proxyFilePath = new File(params.getProxyFile());

			resolveProxyKeyUsage();

			if (params.containsOption(PrintOption.TYPE)) {
				listener.logInfoMessage(convertProxyType(ProxyHelper
						.getProxyType(proxyCredential.getCertificate())
						.toString()));
			}

			if (!params.containsOption(PrintOption.SKIP_AC)) {
				try {
					printStandardMessage = true;
					List<AttributeCertificate> acs = new ArrayList<AttributeCertificate>();

					Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

					while (it.hasNext())
						acs.add(it.next().getVOMSAC().toASN1Structure());

					VOMSACValidityChecker.verifyACs(acs, listener, listener,
							certChainValidator);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			if (params.containsOption(PrintOption.PROXY_STRENGTH_VALIDITY)) {
				printStandardMessage = true;
				if (!getKeySize(proxyChain[0]).equals(params.getKeyLength()))
					setReturnCode(1);
			}

			if (params.containsOption(PrintOption.PROXY_EXISTS)) {
				try {
					proxyChain[0].checkValidity();
					printStandardMessage = false;
				} catch (CertificateExpiredException e) {
					setReturnCode(1);
				}
			}

			if (params.containsOption(PrintOption.PROXY_TIME_VALIDITY)) {
				printStandardMessage = false;
				int period = TimeUtils.parseLifetimeInHoursAndSeconds(params
						.getValidTime());

				setReturnCode(checkTimeValidity(
						getTimeLeft(proxyChain[0].getNotAfter()), period));
			}

			if (params.containsOption(PrintOption.PROXY_HOURS_VALIDITY)) {
				printStandardMessage = false;
				int period = TimeUtils.parseLifetimeInHours(params
						.getValidHours());

				setReturnCode(checkTimeValidity(
						getTimeLeft(proxyChain[0].getNotAfter()), period));
			}

			if (params.containsOption(PrintOption.AC_EXISTS)) {
				printStandardMessage = false;
				Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

				while (it.hasNext()) {
					if (!it.next().getVO().equals(params.getACVO()))
						setReturnCode(1);
					else
						setReturnCode(0);
				}
			}

			if (params.containsOption(PrintOption.SUBJECT)) {
				printStandardMessage = false;
				listener.logInfoMessage(getDNFormat(proxyChain[0]
						.getSubjectDN().toString()));
			}

			if (params.containsOption(PrintOption.ISSUER)
					|| params.containsOption(PrintOption.IDENTITY)) {

				printStandardMessage = false;
				listener.logInfoMessage(getDNFormat(proxyChain[0].getIssuerDN()
						.toString()));
			}

			if (params.containsOption(PrintOption.PROXY_PATH)) {
				printStandardMessage = false;
				listener.logInfoMessage(proxyFilePath.getAbsolutePath());
			}

			if (params.containsOption(PrintOption.TEXT)) {
				printStandardMessage = false;
				printProxyStandardInfo(proxyFilePath);
				listener.logInfoMessage(CertificateUtils.format(proxyChain,
						FormatMode.MEDIUM));
			}

			if (params.containsOption(PrintOption.ALL_OPTIONS)) {
				printStandardMessage = false;
				printProxyStandardInfo(proxyFilePath);
				tabularFormatted("keyusage", getProxyKeyUsages());

				Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

				MessageLogger logger = new MessageLogger();

				while (it.hasNext()) {

					VOMSAttribute attribute = it.next();

					VOMSAttributesPrinter.printVOMSAttributes(logger,
							MessageLogger.MessageLevel.INFO, attribute);

				}
			}

			if (params.containsOption(PrintOption.CHAIN)) {
				printStandardMessage = false;
				listener.logInfoMessage(CertificateUtils.format(proxyChain,
						FormatMode.FULL));

			}

			if (params.containsOption(PrintOption.KEYSIZE)) {
				printStandardMessage = false;
				listener.logInfoMessage(getKeySize(proxyChain[0]));
			}

			if (params.containsOption(PrintOption.KEYUSAGE)
					&& !params.containsOption(PrintOption.ALL_OPTIONS)) {
				printStandardMessage = false;
				printProxyStandardInfo(proxyFilePath);
				tabularFormatted("keyusage", getProxyKeyUsages());
			}

			if (params.containsOption(PrintOption.TIMELEFT)) {
				printStandardMessage = false;
				Date endDate = proxyCredential.getCertificate().getNotAfter();

				listener.logInfoMessage(getFormattedTime((getTimeLeft(endDate))));
			}
			if (params.containsOption(PrintOption.VONAME)) {
				printStandardMessage = false;
				Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

				while (it.hasNext())
					listener.logInfoMessage(it.next().getVO());
			}

			if (params.containsOption(PrintOption.ACTIMELEFT)) {
				printStandardMessage = false;
				Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

				while (it.hasNext())
					listener.logInfoMessage(getFormattedTime(getTimeLeft(it
							.next().getVOMSAC().getNotAfter())));
			}

			if (params.containsOption(PrintOption.ACISSUER)) {
				printStandardMessage = false;
				Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

				while (it.hasNext())
					listener.logInfoMessage(getDNFormat(it.next()
							.getAACertificates()[0].getSubjectDN().toString()));

			}

			if (params.containsOption(PrintOption.ACSUBJECT)) {
				printStandardMessage = false;
				listener.logInfoMessage(getDNFormat(proxyCredential
						.getCertificate().getIssuerDN().toString()));

			}

			if (params.containsOption(PrintOption.ACSERIAL)) {
				printStandardMessage = false;
				Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

				while (it.hasNext())
					listener.logInfoMessage(it.next().getVOMSAC()
							.getSerialNumber().toString());

			}

			if (params.containsOption(PrintOption.FQAN)) {
				printStandardMessage = false;
				Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

				List<String> fqans = null;

				while (it.hasNext()) {
					fqans = it.next().getFQANs();

					for (String fqan : fqans)
						listener.logInfoMessage(fqan);
				}
			}

			if (params.containsOption(PrintOption.SERVER_URI)) {
				printStandardMessage = false;
				Iterator<VOMSAttribute> it = listVOMSAttributes.iterator();

				while (it.hasNext()) {
					VOMSAttribute tmp = it.next();
					listener.logInfoMessage(tmp.getHost() + ":" + tmp.getPort());
				}

			}

			if (printStandardMessage) {
				printProxyStandardInfo(proxyFilePath);
			}

		} catch (KeyStoreException e) {
			setReturnCode(1);
			e.printStackTrace();
		} catch (CertificateException e) {
			setReturnCode(1);
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			listener.notifyProxyNotFound();
			e.printStackTrace();
			setReturnCode(1);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			setReturnCode(1);
			e.printStackTrace();
		}
	}

	private void resolveProxyKeyUsage() {

		boolean[] keyUsages = proxyCredential.getCertificate().getKeyUsage();

		String[] keyUsagesValues = { "digitalSignature", "nonRepudiation",
				"keyEncipherment", "dataEncipherment", "keyAgreement",
				"keyCertSign", "cRLSign", "encipherOnly", "decipherOnly" };

		int index = 0;

		for (boolean key : keyUsages) {

			if (key)
				proxyKeyUsageList.add(keyUsagesValues[index]);
			index++;

		}

	}

	private String getProxyKeyUsages() {

		String usage = "";

		for (String use : proxyKeyUsageList) {
			usage = usage + use + " ";
		}

		return usage;
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

	}

	private String convertProxyType(String type) {

		String newType = null;

		if (type.equals(ProxyType.LEGACY.name()))
			newType = "proxy";
		else if (type.equals(ProxyType.LEGACY.name()))
			newType = "proxy";
		else if (type.equals(ProxyType.DRAFT_RFC.name()))
			newType = "draft";

		return newType;
	}

	private String getDNFormat(String DN) {

		return (new String("/" + DN.replace(',', '/')));

	}

	private int checkTimeValidity(long certTimeLeft, int period) {

		int retVal = 0;

		long msPeriod = period * 1000;

		if (certTimeLeft < msPeriod)
			retVal = 1;

		return retVal;

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

		listener.logInfoMessage(String.format("%-9s %s %s", name, ":", value));
	}

	private void setReturnCode(int returnCode) {

		if (returnCode == 1)
			this.returnCode = returnCode;
	}

	@Override
	public int getExitCode() {
		return returnCode;
	}

}
