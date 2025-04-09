// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.util;

import java.io.Console;

import eu.emi.security.authn.x509.helpers.PasswordSupplier;
import org.italiangrid.voms.VOMSError;

/**
 * 
 * @author andreaceccanti
 * 
 */
public class ConsolePasswordFinder implements PasswordSupplier {

  private String promptMessage;

  public ConsolePasswordFinder(String prompt) {

    this.promptMessage = prompt;
  }

  public char[] getPassword() {

    Console console = System.console();

    if (console == null)
      throw new VOMSError(
        "Error obtaining password from console: no console found for this JVM!");

    return console.readPassword(promptMessage);
  }

}
