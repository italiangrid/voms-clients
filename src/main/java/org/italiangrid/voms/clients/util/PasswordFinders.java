// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.util;

import java.io.InputStream;
import java.io.OutputStream;

import eu.emi.security.authn.x509.helpers.PasswordSupplier;

public class PasswordFinders {

  public static final String PROMPT_MESSAGE = "Enter GRID pass phrase for this identity:";

  public static PasswordSupplier getDefault() {

    if (System.console() != null)
      return new ConsolePasswordFinder(PROMPT_MESSAGE);

    return new InputStreamPasswordFinder(PROMPT_MESSAGE, System.in, System.out);
  }

  public static PasswordSupplier getConsolePasswordFinder() {

    return new ConsolePasswordFinder(PROMPT_MESSAGE);
  }

  public static PasswordSupplier getInputStreamPasswordFinder(InputStream is,
    OutputStream os) {

    return new InputStreamPasswordFinder(PROMPT_MESSAGE, is, os);
  }

  public static PasswordSupplier getNoPromptInputStreamPasswordFinder(
    InputStream is, OutputStream os) {

    return new InputStreamPasswordFinder(null, is, os);
  }

}
