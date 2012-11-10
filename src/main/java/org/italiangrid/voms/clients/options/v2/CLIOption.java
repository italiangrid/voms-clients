package org.italiangrid.voms.clients.options.v2;

import org.apache.commons.cli.Option;

public interface CLIOption {

	public Option getOption();
	public String getLongOptionName();
	
}
