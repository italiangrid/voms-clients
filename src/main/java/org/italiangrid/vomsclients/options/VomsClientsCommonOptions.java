package org.italiangrid.vomsclients.options;


/**
 * Common VOMS clients command-line options
 * 
 * @author Daniele Andreotti
 */


public enum VomsClientsCommonOptions implements VomsCliOption {


	/*
	 * Options formats: with or without argument and with or without short-option<br>
	 * 
	 * A short-option is represented by a single char
	 */

	HELP("help", "Displays helps and exits"),

	USAGE("usage", "Displays helps and exits"),

	VERSION("version", "Displays version"),

	DEBUG("debug", "Enables extra debug output");


	private final String opt;
	private final String longOpt;
	private final String description;
	private final boolean hasArg;
	private final String argDescription;

	private VomsClientsCommonOptions(String longOpt, String description) {
		this.opt = null;
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = false;
		this.argDescription = "";
	}

	private VomsClientsCommonOptions(char opt, String longOpt, String description) {
		this.opt = Character.toString(opt);
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = false;
		this.argDescription = "";
	}

	private VomsClientsCommonOptions(String longOpt, String description, String argDescription) {
		this.opt = null;
		this.longOpt = longOpt;
		this.description = description;
		this.hasArg = true;
		this.argDescription = argDescription;
	}

	private VomsClientsCommonOptions(char opt, String longOpt, String description, String argDescription) {
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
