package org.italiangrid.voms.clients.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.KeyStoreException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAKey;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TimeZone;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.ac.VOMSACParser;
import org.italiangrid.voms.ac.impl.DefaultVOMSACParser;
import org.italiangrid.voms.clients.ProxyInfoParams;
import org.italiangrid.voms.clients.ProxyInfoParams.PrintOption;
import org.italiangrid.voms.clients.strategies.ProxyInfoStrategy;
import org.italiangrid.voms.clients.util.VOMSProxyPathBuilder;
import eu.emi.security.authn.x509.impl.PEMCredential;

public class DefaultVOMSProxyInfoBehaviour implements ProxyInfoStrategy {

	private PEMCredential proxyCredential;

	private final ProxyInfoListenerAdapter listener;

	private final VOMSACParser acParser = new DefaultVOMSACParser();

	private final Map<String, String> infoMap = new LinkedHashMap<String, String>();

	public DefaultVOMSProxyInfoBehaviour(ProxyInfoListenerAdapter listener) {

		this.listener = listener;
	}

	@Override
	public void getProxyInfo(ProxyInfoParams params) {

		X509Certificate[] chain = null;
		List<VOMSAttribute> listVOMSAC = null;

		try {
			if (params.getProxyFile() == null)
				params.setProxyFile(VOMSProxyPathBuilder.buildProxyPath());

			proxyCredential = new PEMCredential(new FileInputStream(
					params.getProxyFile()), null);

			chain = proxyCredential.getCertificateChain();
			listVOMSAC = acParser.parse(proxyCredential.getCertificateChain());
			File proxyFilePath = new File(params.getProxyFile());

			if (params.containsOption(PrintOption.TYPE)) {
				listener.logInfoMessage(proxyCredential.getCertificate()
						.getType());
			}

			if (params.containsOption(PrintOption.PROXY_EXISTS)) {
				try {
					proxyCredential.getCertificate().checkValidity();
					// return 0;
				} catch (CertificateExpiredException e) {
					// return 1;
				} catch (CertificateNotYetValidException e) {
					// return 1;
				}

			}

			if (params.containsOption(PrintOption.PROXY_TIME_VALIDITY)) {
				try {
					proxyCredential.getCertificate().checkValidity();
					listener.logInfoMessage("0");
				} catch (CertificateExpiredException e) {
					listener.logInfoMessage("1");
				} catch (CertificateNotYetValidException e) {
					listener.logInfoMessage("1");
				}

			}

			if (params.containsOption(PrintOption.PROXY_HOURS_VALIDITY)) {
				try {
					proxyCredential.getCertificate().checkValidity();
					listener.logInfoMessage("0");
				} catch (CertificateExpiredException e) {
					listener.logInfoMessage("1");
				} catch (CertificateNotYetValidException e) {
					listener.logInfoMessage("1");
				}

			}

			if (params.containsOption(PrintOption.AC_EXISTS)) {
				Iterator<VOMSAttribute> it = listVOMSAC.iterator();

				while (it.hasNext()) {
					if (it.next().getVO().equals(params.getACVO())) {
						listener.logInfoMessage("0");
						// return 0;
					}
				}
				// return 1;
			}

			if (params.containsOption(PrintOption.PROXY_STRENGTH_VALIDITY)) {
				// if (getKeySize().equals(params.getKeyLength()))
				// return 0;
				// else
				// return 1;
			}

			if (params.containsOption(PrintOption.SUBJECT)) {
				listener.logInfoMessage(getDNFormat(proxyCredential
						.getCertificate().getSubjectDN().toString()));
			}

			if (params.containsOption(PrintOption.ISSUER)
					|| params.containsOption(PrintOption.IDENTITY)) {
				listener.logInfoMessage(getDNFormat(proxyCredential
						.getCertificate().getIssuerDN().toString()));
			}

			if (params.containsOption(PrintOption.PROXY_PATH)) {
				listener.logInfoMessage(proxyFilePath.getAbsolutePath());
			}

			if (params.containsOption(PrintOption.TEXT)) {
				listener.logInfoMessage(getDNFormat(proxyCredential
						.getCertificate().toString()));

			}

			if (params.containsOption(PrintOption.ALL_OPTIONS)) {

				infoMap.put("subject", getDNFormat(chain[0].getSubjectDN()
						.toString()));

				infoMap.put("issuer", getDNFormat(chain[0].getIssuerDN()
						.toString()));

				infoMap.put("identity", getDNFormat(chain[0].getIssuerDN()
						.toString()));

				infoMap.put("strength", getKeySize(chain[0]));

				infoMap.put("path", proxyFilePath.getAbsolutePath());

				infoMap.put("timeleft", getTimeLeft(chain[0].getNotAfter()));

				infoMap.put("keyusage", getTimeLeft(chain[0].getNotAfter()));

				printInfo();

				Iterator<VOMSAttribute> it = listVOMSAC.iterator();

				while (it.hasNext()) {

					VOMSAttribute attribute = it.next();

					listener.logInfoMessage("=== " + "VO " + attribute.getVO()
							+ " extension information" + " ===");

					printInfo();
				}
			}

			if (params.containsOption(PrintOption.CHAIN)) {

				listener.logInfoMessage("=== " + "Proxy Chain Information"
						+ " ===");

				for (int i = chain.length - 1; i > 0; i--) {
					infoMap.put("subject", getDNFormat(chain[i].getSubjectDN()
							.toString()));

					infoMap.put("issuer", getDNFormat(chain[i].getIssuerDN()
							.toString()));

					if (i != chain.length - 1)
						infoMap.put("type", chain[i].getType());

					infoMap.put("strength", getKeySize(chain[i]));

					infoMap.put("timeleft", getTimeLeft(chain[i].getNotAfter()));

					printInfo();

				}

				listener.logInfoMessage("=== " + "Proxy Information" + " ===");

				infoMap.put("subject", getDNFormat(chain[0].getSubjectDN()
						.toString()));

				infoMap.put("issuer", getDNFormat(chain[0].getIssuerDN()
						.toString()));

				infoMap.put("identity", getDNFormat(chain[0].getIssuerDN()
						.toString()));

				infoMap.put("strength", getKeySize(chain[0]));

				infoMap.put("path", proxyFilePath.getAbsolutePath());

				infoMap.put("timeleft", getTimeLeft(chain[0].getNotAfter()));

				printInfo();
			}

			if (params.containsOption(PrintOption.KEYSIZE)) {

				listener.logInfoMessage(getKeySize(chain[0]));
			}

			// if (params.containsOption(PrintOption.KEYUSAGE)) {
			//
			// listener.logInfoMessage(proxyCredential.getCertificate()
			// .getKeyUsage().toString());
			// }

			if (params.containsOption(PrintOption.TIMELEFT)) {

				Date endDate = proxyCredential.getCertificate().getNotAfter();

				listener.logInfoMessage(getTimeLeft(endDate));
			}
			if (params.containsOption(PrintOption.VONAME)) {

				Iterator<VOMSAttribute> it = listVOMSAC.iterator();

				while (it.hasNext())
					listener.logInfoMessage(it.next().getVO());
			}

			if (params.containsOption(PrintOption.ACTIMELEFT)) {

				Iterator<VOMSAttribute> it = listVOMSAC.iterator();

				while (it.hasNext())
					listener.logInfoMessage(getTimeLeft(it.next().getVOMSAC()
							.getNotAfter()));
			}

			if (params.containsOption(PrintOption.ACISSUER)) {

				Iterator<VOMSAttribute> it = listVOMSAC.iterator();

				while (it.hasNext())
					listener.logInfoMessage(getDNFormat(it.next()
							.getAACertificates()[0].getSubjectDN().toString()));

			}

			if (params.containsOption(PrintOption.ACSUBJECT)) {

				listener.logInfoMessage(getDNFormat(proxyCredential
						.getCertificate().getIssuerDN().toString()));

			}

			if (params.containsOption(PrintOption.ACSERIAL)) {

				Iterator<VOMSAttribute> it = listVOMSAC.iterator();

				while (it.hasNext())
					listener.logInfoMessage(it.next().getVOMSAC()
							.getSerialNumber().toString());
				// getAACertificates()[0] .getSerialNumber().toString());

			}

			if (params.containsOption(PrintOption.FQAN)) {

				Iterator<VOMSAttribute> it = listVOMSAC.iterator();

				List<String> fqans = null;
				while (it.hasNext()) {
					fqans = it.next().getFQANs();
				}

				Iterator<String> it2 = fqans.iterator();

				while (it2.hasNext()) {
					listener.logInfoMessage(it2.next());
				}

			}

			if (params.containsOption(PrintOption.SERVER_URI)) {

				Iterator<VOMSAttribute> it = listVOMSAC.iterator();

				while (it.hasNext()) {
					VOMSAttribute tmp = it.next();
					listener.logInfoMessage(tmp.getHost() + ":" + tmp.getPort());
				}

			}

		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (CertificateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			listener.notifyProxyNotFound();
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String getDNFormat(String DN) {

		return (new String("/" + DN.replace(',', '/')));

	}

	private String getTimeLeft(Date end) {

		SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");

		df.setTimeZone(TimeZone.getTimeZone("GMT"));

		Date today = new Date();

		long expireTime = end.getTime();
		long currentTime = today.getTime();

		long timeleft = (expireTime - currentTime);

		if (timeleft < 0)
			timeleft = 0;

		String time = df.format(timeleft);

		return time;
	}

	private String getKeySize(X509Certificate chain) {
		RSAKey rsaKey = (RSAKey) (chain.getPublicKey());

		return (Integer.toString(rsaKey.getModulus().bitLength()));

	}

	private void printInfo() {

		Formatter formatter = new Formatter();

		Iterator<Entry<String, String>> it = infoMap.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, String> entry = it.next();
			listener.logInfoMessage(String.format("%-15s %s %s",
					entry.getKey(), ":", entry.getValue()));
			// listener.logInfoMessage(entry.getKey() + "\t: " +
			// entry.getValue());
		}

		listener.logInfoMessage("");
		infoMap.clear();
	}
}
