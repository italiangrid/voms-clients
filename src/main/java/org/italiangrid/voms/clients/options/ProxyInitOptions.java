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

public enum ProxyInitOptions implements CLIOption {

  AC_LIFETIME("vomslife"),
  VALIDITY("valid"),
  CERT_LOCATION("cert"),
  ENABLE_STDIN_PWD("pwstdin"),
  FAIL_ON_WARN("failonwarn"),
  FQANS_ORDERING("order"),
  IGNORE_WARNINGS("ignorewarn"),
  KEY_SIZE("bits"),
  KEY_LOCATION("key"),
  LEGACY_PROXY("old"),
  LIMITED_PROXY("limited"),
  OPTIONS_FILE("conf"),
  PATHLEN_CONSTRAINT("path_length"),
  PROXY_LOCATION("out"),
  PROXY_LIFETIME_IN_HOURS("hours"),
  PROXY_NOREGEN("noregen"),
  PROXY_VERSION("proxyver"),
  QUIET_MODE("quiet"),
  RFC_PROXY("rfc"),
  SKIP_AC_VERIFICATION("dont_verify_ac"),
  TARGET_HOSTNAME("target"),
  TIMEOUT("timeout"),
  TRUSTED_CERT_LOCATION("certdir"),
  VERIFY_CERT("verify"),
  VOMS_COMMAND("voms"),
  VOMSDIR("vomsdir"),
  VOMSES_LOCATION("vomses"),
  SKIP_INTEGRITY_CHECKS("skip_chain_integrity_checks"),
  SKIP_HOSTNAME_CHECKS("skip_hostname_checks");

  private Option option;

  private ProxyInitOptions(String longOpt) {

    option = VOMSCLIOptionBuilder.buildOption(longOpt,
      CLIOptionsBundle.proxyInit);
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
