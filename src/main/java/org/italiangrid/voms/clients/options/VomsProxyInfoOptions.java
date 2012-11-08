package org.italiangrid.voms.clients.options;

/**
 * VomsProxyInfo client command-line options
 * 
 * @author Daniele Andreotti
 */

public enum VomsProxyInfoOptions implements VomsCliOption {

	/*
	 * Options formats: with or without argument and with or without
	 * short-option<br>
	 * 
	 * A short-option is represented by a single char
	 */

	PROXY_FILENAME("out", "Non standard location of new proxy cert",
			"proxyfile"),

	SKIP_AC("dont_verify_ac", "Skips AC verification"),

	PROXY_EXISTS('e', "exists", "Returns 0 if valid proxy exists, 1 otherwise"),

	AC_EXISTS("acexists",
			"Returns 0 if AC exists corresponding to voname, 1 otherwise",
			"voname"),

	OPTIONS_FILE("conf", "Read options from <file>", "file"),

	PRINT_INCLUDED_FILE("include", "Print included file", "file"),

	PRINT_CHAIN("chain",
			"Prints information about the whol proxy chain (CA excluded)"),

	PRINT_SUBJECT("subject", "Prints Distinguished name (DN) of proxy subject",
			"proxyfile"),

	PRINT_ISSUER("issuer", "Prints the DN of proxy issuer (certificate signer)"),

	PRINT_IDENTITY("identity",
			"Prints the DN of the identity represented by the proxy"),

	PRINT_TYPE("type", "Prints the Type of proxy (full or limited)"),

	PRINT_TIMELEFT("timeleft", "Prints time (in seconds) until proxy expires"),

	PRINT_KEYSIZE("strength", "Key size (in bits)"),

	PRINT_ALL_OPTIONS("all", "All proxy options in a human readable format"),

	PRINT_TEXT("text", "Prints all of the certificate"),

	PRINT_PROXY_PATH('p', "path", "Prints the pathname of proxy file"),

	PRINT_VONAME("vo", "Prints the vo name"),

	PRINT_FQAN("fqan", "Prints attribute in FQAN format"),

	PRINT_ACSUBJECT("acsubject",
			"Prints the distinguished name (DN) of AC subject"),

	PRINT_ACISSUER("acissuer",
			"Prints the DN of AC issuer (certificate signer)"),

	PRINT_ACTIMELEFT("actimeleft", "Prints time (in seconds) until AC expires"),

	PRINT_ACSERIAL("serial", "Prints AC serial number"),

	PRINT_SERVER_URI("uri", "Prints server URI"),

	PRINT_KEYUSAGE('k', "keyusage", "Prints content of KeyUsage extension"),

	PROXY_TIME_VALIDITY("valid",
			"[option to -exists] time requirement for proxy to be valid", "H:M"),

	PROXY_HOURS_VALIDITY(
			"hours",
			"[option to -exists] time requirement for proxy to be valid (deprecated, use -valid instead)",
			"H"),

	PROXY_STRENGTH_VALIDITY('b', "bits",
			"[option to -exists] strength requirement for proxy to be valid",
			"B");

	private final String opt;
	private final String longOpt;
	private final String description;
	private final boolean hasArg;
	private final String argDescription;

	private VomsProxyInfoOptions(String longOpt, String description) {
		this.opt = null;
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = false;
		this.argDescription = "";
	}

	private VomsProxyInfoOptions(char opt, String longOpt, String description) {
		this.opt = Character.toString(opt);
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = false;
		this.argDescription = "";
	}

	private VomsProxyInfoOptions(String longOpt, String description,
			String argDescription) {
		this.opt = null;
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = true;
		this.argDescription = argDescription;
	}

	private VomsProxyInfoOptions(char opt, String longOpt, String description,
			String argDescription) {
		this.opt = Character.toString(opt);
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = true;
		this.argDescription = argDescription;
	}

	public String getName() {
		return longOpt;
	}

	public String getDescription() {
		return description;
	}

	public boolean hasArg() {
		return hasArg;
	}

	public String getOpt() {
		return opt;
	}

	public String getLongOpt() {
		return longOpt;
	}

	public String getArgDescription() {
		return argDescription;
	}

}
