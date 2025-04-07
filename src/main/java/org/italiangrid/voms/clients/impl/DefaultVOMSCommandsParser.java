// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.italiangrid.voms.clients.strategies.VOMSCommandsParsingStrategy;

/**
 * Default implementation of the {@link VOMSCommandsParsingStrategy} interface.
 * This class is responsible for parsing VOMS commands into a structured format.
 */
public class DefaultVOMSCommandsParser implements VOMSCommandsParsingStrategy {

  /**
   * The character used to separate VO names from FQANs in command strings.
   */
  public static final String COMMAND_SEPARATOR = ":";

  /**
   * The string representing a request for all available attributes.
   */
  public static final String ALL_COMMAND_STRING = "all";

  /**
   * Parses a list of VOMS commands and organizes them into a map.
   *
   * @param commands A list of strings representing VOMS commands, where each
   *                 command follows the format "vo[:fqan]".
   * @return A map where the keys are VO names and the values are lists of
   *         requested FQANs. If "all" is specified, no FQANs are added.
   *         Returns {@code null} if the input list is {@code null}, or an
   *         empty map if the input list is empty.
   */
  @Override
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

        if (cmdTokens.length > 1 && !cmdTokens[1].equals(ALL_COMMAND_STRING))
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
