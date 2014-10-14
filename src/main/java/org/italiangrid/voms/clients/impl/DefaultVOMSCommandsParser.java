/**
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2006-2014.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.italiangrid.voms.clients.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.italiangrid.voms.clients.strategies.VOMSCommandsParsingStrategy;

public class DefaultVOMSCommandsParser implements VOMSCommandsParsingStrategy {

	public static final String COMMAND_SEPARATOR = ":";
	public static final String ALL_COMMAND_STRING = "all";

	public Map<String, List<String>> parseCommands(List<String> commands) {

		if (commands == null)
			return null;

		if (commands.isEmpty())
			return Collections.emptyMap();

		Map<String, List<String>> commandsMap = new LinkedHashMap<String, List<String>>();

		for (String cmd : commands) {

			String[] cmdTokens = cmd.split(COMMAND_SEPARATOR);
			String vo = cmdTokens[0];

			if (commandsMap.containsKey(vo)) {

				if (cmdTokens.length > 1
						&& !cmdTokens[1].equals(ALL_COMMAND_STRING))
					commandsMap.get(vo).add(cmdTokens[1]);
			} else {

				List<String> requestedFQANs = new ArrayList<String>();

				if (cmdTokens.length > 1 && !cmdTokens[1].equals("all"))
					requestedFQANs.add(cmdTokens[1]);

				commandsMap.put(vo, requestedFQANs);
			}
		}

		return commandsMap;
	}

}
