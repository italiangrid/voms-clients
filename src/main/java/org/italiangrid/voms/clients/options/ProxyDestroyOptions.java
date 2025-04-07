// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.options;

import org.apache.commons.cli.Option;

/**
 * Enumeration representing the command-line options for the proxy destruction command. This enum
 * defines various options that can be used when executing the proxy destroy command.
 *
 * <p>
 * Each option is associated with an {@link Option} object built using the
 * {@link VOMSCLIOptionBuilder} utility.
 * </p>
 *
 * <p>
 * Copyright (c) Istituto Nazionale di Fisica Nucleare, 2006-2014.
 * </p>
 *
 * @author Istituto Nazionale di Fisica Nucleare
 * @version 1.0
 */
public enum ProxyDestroyOptions implements CLIOption {

  /** Help option, displays usage information. */
  HELP("help"),

  /** Usage option, provides details on how to use the command. */
  USAGE("usage"),

  /** Version option, displays version information. */
  VERSION("version"),

  /** Debug option, enables debug mode for detailed logs. */
  DEBUG("debug"),

  /** Quiet option, suppresses non-essential output. */
  QUIET("quiet"),

  /** File option, specifies a proxy file to be destroyed. */
  FILE("file"),

  /** Dry-run option, simulates the command without executing it. */
  DRY("dry"),

  /** Configuration file option, specifies a configuration file. */
  CONF("conf");

  /** The Apache Commons CLI {@link Option} representation of the command-line option. */
  private Option option;

  /**
   * Constructs a ProxyDestroyOptions enum with the specified long option name.
   *
   * @param longOpt the long option name
   */
  private ProxyDestroyOptions(String longOpt) {

    option = VOMSCLIOptionBuilder.buildOption(longOpt, CLIOptionsBundle.proxyInfo);
  }

  /**
   * Returns the associated Apache Commons CLI {@link Option} object.
   *
   * @return the CLI option
   */
  @Override
  public Option getOption() {

    return option;
  }

  /**
   * Returns the long option name of this command-line option.
   *
   * @return the long option name
   */
  @Override
  public String getLongOptionName() {

    return option.getLongOpt();
  }
}
