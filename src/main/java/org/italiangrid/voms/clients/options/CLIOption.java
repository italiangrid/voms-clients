package org.italiangrid.voms.clients.options;

import org.apache.commons.cli.Option;

public interface CLIOption {

	public Option getOption();
	public String getLongOptionName();
	
}
