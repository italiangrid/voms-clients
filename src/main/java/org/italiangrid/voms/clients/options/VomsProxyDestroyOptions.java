package org.italiangrid.voms.clients.options;

public enum VomsProxyDestroyOptions implements VomsCliOption {

	/*
	 * Options formats: with or without argument and with or without
	 * short-option<br>
	 * 
	 * A short-option is represented by a single char
	 */

	HELP('h', "help", "Displays usage"),

	USAGE('u', "usage", "Displays version"),

	VERSION('v', "version", "Displays version"),

	DEBUG('d', "debug", "Enables extra debug output"),

	QUIET('q', "quiet", "Quiet mode, minimal output"),

	FILE('f', "file", "Specifies proxy file name", "proxyfile"),

	DRY("dry", "Only go in dryrun mode"),

	CONF('c', "conf", "Load options from file <file>", "file");

	private final String shortOption;
	private final String longOption;
	private final String description;
	private final boolean hasArg;
	private final String argDescription;

	private VomsProxyDestroyOptions(String shortOption, String longOption,
			String description, boolean hasArg, String argDescription) {
		this.shortOption = shortOption;
		this.longOption = longOption;
		this.description = description;
		this.hasArg = hasArg;
		this.argDescription = argDescription;
	}

	private VomsProxyDestroyOptions(String longOption, String description) {
		this(null, longOption, description, false, null);

	}

	private VomsProxyDestroyOptions(char shortOption, String longOption,
			String description) {
		this(Character.toString(shortOption), longOption, description, false,
				null);
	}

	private VomsProxyDestroyOptions(String longOption, String description,
			String argDescription) {
		this(null, longOption, description, true, argDescription);
	}

	private VomsProxyDestroyOptions(char shortOption, String longOption,
			String description, String argDescription) {
		this(Character.toString(shortOption), longOption, description, true,
				argDescription);
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean hasArg() {
		return hasArg;
	}

	@Override
	public String getOpt() {
		return shortOption;
	}

	@Override
	public String getLongOpt() {
		return longOption;
	}

	@Override
	public String getArgDescription() {
		return argDescription;
	}

}
