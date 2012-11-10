package org.italiangrid.voms.clients.options;

/**
 * VomsProxyInit client command-line options
 * 
 * @author Daniele Andreotti
 */

public enum VomsProxyInitOptions implements VomsCliOption {
	
	/*
	 * Options formats: with or without argument and with or without
	 * short-option<br>
	 * 
	 * A short-option is represented by a single char
	 */

	AC_LIFETIME(
			"vomslife",
			"Try to get a VOMS pseudocert valid for h hours and m minutes (default to value of '-valid').",
			"h:m"),

	AC_VALIDITY(
			"valid",
			"Proxy and AC are valid for h hours and m minutes (defaults to 12:00)",
			"h:m"),

	CERT_LOCATION("cert", "Nonstandard location of user certificate",
			"certfile"),

	ENABLE_STDIN_PWD("pwstdin", "Allows passphrase from stdin"),

	FAIL_ON_WARN('f', "failonwarn", "Treat warnings as errors"),

	FQANS_ORDERING("order", "Specify ordering of attributes", "group<:role>"),

	GLOBUS_VERSION('g', "globus", "Globus version. (MajorMinor)", "version"),

	IGNORE_WARNINGS("ignorewarn", "Ignore warnings"),

	INCLUDE_AC_FROM_FILE("includeac", "Get AC from file", "file"),

	INCLUDE_FILE("include", "Include the contents of the specified file",
			"file"),

	KEY_LENGTH('b', "bits", "Number of bits in key {512|1024|2048|4096}"),

	KEY_LOCATION('k', "key", "Non standard location of user key", "keyfile"),

	LEGAGY_PROXY("old",
			"Creates GT2 compliant proxy (synonymous with '-proxyver 2')"),

	LIMITED_PROXY("limited", "Creates a limited proxy"),

	LIST_ALL_ATTRIBUTES("list", "Show all available attributes"),

	OPTIONS_FILE("conf", "Read options from <file>", "file"),

	PATHLEN_CONSTRAINT(
			"path_length",
			"Allow a chain of at most l proxies to be generated from this ones",
			"l"),

	POLICY_FILE("policy",
			"File containing policy to store in the ProxyCertInfo extension",
			"policyfile"),

	POLICY_LANGUAGE("pl", "OID string for the policy language", "hostname"),

	PROXY_FILENAME("out", "Non standard location of new proxy cert",
			"proxyfile"),

	PROXY_LIFETIME_IN_HOURS("hours", "Proxy is valid for H hours (default:12)", "H"),

	PROXY_NOREGEN('n', "noregen",
			"Use existing proxy certificate to connect to server and sign the new proxy"),

	PROXY_VERSION("proxyver", "proxyver"),

	QUIET_MODE('q', "quiet", "Quiet mode, minimal output"),

	REQUEST_TIMEOUT("timeout", "Timeout for server connections, in seconds",
			"num"),

	RFC_PROXY('r', "rfc",
			"Creates RFC 3820 compliant proxy (synonymous with '-proxyver 4')"),

	SAVE_AC_IN_FILE('s', "separate",
			"Saves the informations returned by the server on file <file>",
			"file"),

	SKIP_AC_VERIFICATION("dont_verify_ac", "Skips AC verification"),

	TARGET_HOSTNAME("target", "Targets the AC against a specific hostname",
			"hostname"),

	TRUSTED_CERT_LOCATION("certdir",
			"Non standard location of trusted cert dir", "certdir"),

	VERIFY_CERT("verify", "Verifies certificate to make proxy for"),

	VOMS_COMMAND(
			"voms",
			"Specify voms server. :command is optional,and is used to ask for specific attributes (e.g: roles)",
			"voms<:command>"),

	VOMSES_LOCATION("vomses", "Non standard location of configuration files",
			"file");

	private final String argDescription;
	private final String description;
	private final boolean hasArg;
	private final String longOpt;
	private final String opt;

	private VomsProxyInitOptions(char opt, String longOpt, String description) {
		this(Character.toString(opt),longOpt,description, false, null);
	}
	
	
	private VomsProxyInitOptions(char opt, String longOpt, String description,
			String argDescription) {
		this(Character.toString(opt), longOpt, description, true, argDescription);
	}

	private VomsProxyInitOptions(String longOpt, String description) {
		this(null, longOpt, description, false, null);
	}

	private VomsProxyInitOptions(String longOpt, String description,
			String argDescription) {
		this(null,longOpt, description, true, argDescription);
	}

	private VomsProxyInitOptions(String shortOpt, String longOption, String description, boolean hasArg, String argDescription){
		this.opt = shortOpt;
		this.longOpt = longOption;
		this.description = description;
		this.hasArg = hasArg;
		this.argDescription = argDescription;
	}

	public String getArgDescription() {
		return argDescription;
	}

	public String getDescription() {
		return description;
	}

	public String getLongOpt() {
		return longOpt;
	}

	public String getName() {
		return longOpt;
	}

	public String getOpt() {
		return opt;
	}

	public boolean hasArg() {
		return hasArg;
	}

	
}
