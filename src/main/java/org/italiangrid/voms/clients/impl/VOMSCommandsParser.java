package org.italiangrid.voms.clients.impl;

import java.util.List;
import java.util.Map;

public interface VOMSCommandsParser {

	public Map<String, List<String>> parseCommands(List<String> commands);
	
}
