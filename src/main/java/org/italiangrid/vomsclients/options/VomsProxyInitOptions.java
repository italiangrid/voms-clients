package org.italiangrid.vomsclients.options;


/**
 * VomsProxyInit client command-line options
 * 
 * @author Daniele Andreotti
 */

public enum VomsProxyInitOptions implements VomsCliOption {


	/*
	 * Options formats: with or without argument and with or without short-option<br>
	 * 
	 * A short-option is represented by a single char
	 */


	QUIET_MODE('q', "quiet", "Quiet mode, minimal output"),

	VERIFY_CERT("verify", "Verifies certificate to make proxy for"),

	ENABLE_STDIN_PWD("pwstdin", "Allows passphrase from stdin"),

	LIMITED_PROXY("limited", "Creates a limited proxy"),

	PROXY_AC_VALIDITY("valid", "Proxy and AC are valid for h hours and m minutes (defaults to 12:00)", "h:m"),

	PROXY_VALIDITY("hours", "Proxy is valid for H hours (default:12)", "H"),

	KEY_LENGTH('b', "bits", "Number of bits in key {512|1024|2048|4096}"),

	CERT_LOCATION("cert", "Nonstandard location of user certificate", "certfile"),

	KEY_LOCATION('k', "key", "Non standard location of user key", "keyfile"),

	TRUSTED_CERT_LOCATION("certdir", "Non standard location of trusted cert dir", "certdir"),

	PROXY_FILENAME("out", "Non standard location of new proxy cert", "proxyfile"),

	VOMS_SERVER("voms",
			"Specify voms server. :command is optional,and is used to ask for specific attributes (e.g: roles)",
			"voms<:command>"),

	ATTRIBUTES_ORDER("order", "Specify ordering of attributes", "group<:role>"),

	AC_SERVER("target", "Targets the AC against a specific hostname", "hostname"),

	VOMS_PSEUDOCERT("vomslife",
			"Try to get a VOMS pseudocert valid for h hours and m minutes (default to value of '-valid').", "h:m"),

	INCLUDE_FILE("include", "Include the contents of the specified file", "file"),

	OPTIONS_FILE("conf", "Read options from <file>", "file"),

	VOMSES_LOCATION("vomses", "Non standard location of configuration files", "file"),

	POLICY_FILE("policy", "File containing policy to store in the ProxyCertInfo extension", "policyfile"),

	POLICY_LANGUAGE("pl", "OID string for the policy language", "hostname"),

	PATH_LENGTH("path_length", "Allow a chain of at most l proxies to be generated from this ones", "l"),

	GLOBUS_VERSION('g', "globus", "Globus version. (MajorMinor)", "version"),

	PROXY_VERSION("proxyver", "proxyver"),

	PROXY_NOREGEN('n', "noregen", "Use existing proxy certificate to connect to server and sign the new proxy"),

	SPLIT_OUTPUT('s', "separate", "Saves the informations returned by the server on file <file>", "file"),

	IGNORE_WARNINGS("ignorewarn", "Ignore warnings"),

	FAIL_ON_WARN('f', "failonwarn", "Treat warnings as errors"),

	SHOW_ATTRIBUTES("list", "Show all available attributes"),

	RFC_TYPE_PROXY('r', "rfc", "Creates RFC 3820 compliant proxy (synonymous with '-proxyver 4')"),

	GT2_TYPE_PROXY("old", "Creates GT2 compliant proxy (synonymous with '-proxyver 2')"),

	REQUEST_TIMEOUT("timeout", "Timeout for server connections, in seconds", "num"),

	AC_FILE("includeac", "Get AC from file", "file"),

	SKIP_AC("dont_verify_ac", "Skips AC verification");


	private final String opt;
	private final String longOpt;
	private final String description;
	private final boolean hasArg;
	private final String argDescription;

	private VomsProxyInitOptions(String longOpt, String description) {
		this.opt = null;
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = false;
		this.argDescription = "";
	}

	private VomsProxyInitOptions(char opt, String longOpt, String description) {
		this.opt = Character.toString(opt);
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = false;
		this.argDescription = "";
	}

	private VomsProxyInitOptions(String longOpt, String description, String argDescription) {
		this.opt = null;
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = true;
		this.argDescription = argDescription;
	}

	private VomsProxyInitOptions(char opt, String longOpt, String description, String argDescription) {
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
