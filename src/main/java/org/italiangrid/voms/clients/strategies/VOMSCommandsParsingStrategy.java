package org.italiangrid.voms.clients.strategies;

import java.util.List;
import java.util.Map;

public interface VOMSCommandsParsingStrategy {

	public Map<String, List<String>> parseCommands(List<String> commands);

}
