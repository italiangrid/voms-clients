// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.options;

import org.apache.commons.cli.Option;

public enum ProxyInfoOptions implements CLIOption {

  PROXY_FILENAME("file"), PROXY_EXISTS("exists"), AC_EXISTS("acexists"), OPTIONS_FILE(
    "conf"), PRINT_CHAIN("chain"), PRINT_SUBJECT("subject"), PRINT_ISSUER(
    "issuer"), PRINT_IDENTITY("identity"), PRINT_TYPE("type"), PRINT_TIMELEFT(
    "timeleft"), PRINT_KEYSIZE("strength"), PRINT_ALL_OPTIONS("all"), PRINT_TEXT(
    "text"), PRINT_PROXY_PATH("path"), PRINT_VONAME("vo"), PRINT_FQAN("fqan"), PRINT_ACSUBJECT(
    "acsubject"), PRINT_ACISSUER("acissuer"), PRINT_ACTIMELEFT("actimeleft"), PRINT_ACSERIAL(
    "serial"), PRINT_SERVER_URI("uri"), PRINT_KEYUSAGE("keyusage"), PROXY_TIME_VALIDITY(
    "valid"), PROXY_HOURS_VALIDITY("hours"), PROXY_STRENGTH_VALIDITY("bits");

  private Option option;

  private ProxyInfoOptions(String longOpt) {

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
