package org.italiangrid.voms.clients;

import java.util.EnumSet;

/**
 * This class represents the parameters that drive the {@link ProxyInfoStrategy}
 * when querying a VOMS proxy.
 * 
 * @author Daniele Andreotti
 * 
 */

public class ProxyInfoParams {

	public enum PrintOption {

		SUBJECT,

		ISSUER,

		PROXY_EXISTS,

		AC_EXISTS,

		CHAIN,

		IDENTITY,

		TYPE,

		TIMELEFT,

		KEYSIZE,

		ALL_OPTIONS,

		TEXT,

		PROXY_PATH,

		VONAME,

		FQAN,

		ACSUBJECT,

		ACISSUER,

		ACTIMELEFT,

		ACSERIAL,

		SERVER_URI,

		KEYUSAGE,

		PROXY_TIME_VALIDITY,

		PROXY_HOURS_VALIDITY,

		PROXY_STRENGTH_VALIDITY;
	}

	private final EnumSet<PrintOption> setOfPrintOptions = EnumSet
			.noneOf(PrintOption.class);
	/**
	 * Name of the proxy certificate file.
	 */

	private String proxyFile;

	private boolean verifyAC = true;

	private String ACVO;

	private String keyLength;

	public String getProxyFile() {
		return proxyFile;
	}

	public void setProxyFile(String proxyFile) {
		this.proxyFile = proxyFile;
	}

	public boolean isVerifyAC() {
		return verifyAC;
	}

	public void setVerifyAC(boolean verifyAC) {
		this.verifyAC = verifyAC;
	}

	public void addPrintOption(PrintOption opt) {
		setOfPrintOptions.add(opt);
	}

	public boolean containsOption(PrintOption opt) {
		return setOfPrintOptions.contains(opt);
	}

	public String getACVO() {
		return ACVO;
	}

	public void setACVO(String aCVO) {
		ACVO = aCVO;
	}

	public String getKeyLength() {
		return keyLength;
	}

	public void setKeyLength(String keyLength) {
		this.keyLength = keyLength;
	}

}
