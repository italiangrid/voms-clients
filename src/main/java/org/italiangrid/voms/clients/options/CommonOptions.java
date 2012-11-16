package org.italiangrid.voms.clients.options;

import org.apache.commons.cli.Option;

public enum CommonOptions implements CLIOption{

	HELP("help"),
	USAGE("usage"),
	VERSION("version"),
	DEBUG("debug"),
	CONF("conf");
	
	private Option option;
	
	private CommonOptions(String longOpt){
		option = VOMSCLIOptionBuilder.buildOption(longOpt, CLIOptionsBundle.common);
	}

	@Override
	public Option getOption() {
		return option;
	}

	@Override
	public String getLongOptionName() {
		
		return option.getLongOpt();
	}
}
