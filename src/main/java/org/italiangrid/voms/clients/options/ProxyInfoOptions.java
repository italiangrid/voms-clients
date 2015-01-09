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
