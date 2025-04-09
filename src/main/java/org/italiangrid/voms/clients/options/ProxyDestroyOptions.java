// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.options;

import org.apache.commons.cli.Option;

public enum ProxyDestroyOptions implements CLIOption {

  HELP("help"), USAGE("usage"), VERSION("version"), DEBUG("debug"), QUIET(
    "quiet"), FILE("file"), DRY("dry"), CONF("conf");

  private Option option;

  private ProxyDestroyOptions(String longOpt) {

    option = VOMSCLIOptionBuilder.buildOption(longOpt,
      CLIOptionsBundle.proxyInfo);
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
