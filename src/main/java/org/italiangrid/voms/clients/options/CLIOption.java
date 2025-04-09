// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.options;

import org.apache.commons.cli.Option;

public interface CLIOption {

  public Option getOption();

  public String getLongOptionName();

}
