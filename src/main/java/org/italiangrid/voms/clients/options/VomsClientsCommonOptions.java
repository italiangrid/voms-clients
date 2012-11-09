package org.italiangrid.voms.clients.options;

/**
 * Common VOMS clients command-line options
 * 
 * @author Daniele Andreotti
 */

public enum VomsClientsCommonOptions implements VomsCliOption {

	/*
	 * Options formats: with or without argument and with or without
	 * short-option<br>
	 * 
	 * A short-option is represented by a single char
	 */

	HELP("help", "Displays helps and exits"),

	USAGE("usage", "Displays helps and exits"),

	VERSION("version", "Displays version"),

	DEBUG("debug", "Enables extra debug output");

	private final String shortOption;
	private final String longOption;
	private final String description;
	private final boolean hasArg;
	private final String argDescription;

	private VomsClientsCommonOptions(String shortOption, String longOption,
			String description, boolean hasArg, String argDescription) {
		this.shortOption = shortOption;
		this.longOption = longOption;
		this.description = description;
		this.hasArg = hasArg;
		this.argDescription = argDescription;
	}

	private VomsClientsCommonOptions(String longOption, String description) {
		this(null, longOption, description, false, null);

	}

	private VomsClientsCommonOptions(char shortOption, String longOption,
			String description) {
		this(Character.toString(shortOption), longOption, description, false,
				null);
	}

	private VomsClientsCommonOptions(String longOption, String description,
			String argDescription) {
		this(null, longOption, description, true, argDescription);
	}

	private VomsClientsCommonOptions(char shortOption, String longOption,
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
