// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintStream;

import eu.emi.security.authn.x509.helpers.PasswordSupplier;
import org.italiangrid.voms.VOMSError;

public class InputStreamPasswordFinder implements PasswordSupplier {

  InputStream is;
  PrintStream os;

  String promptMessage;

  public InputStreamPasswordFinder(String prompt, InputStream is,
    OutputStream os) {

    this.promptMessage = prompt;
    this.is = is;
    this.os = new PrintStream(os);
  }

  public char[] getPassword() {

    try {

      if (promptMessage != null) {
        os.print(promptMessage);
        os.flush();
      }

      BufferedReader reader = new BufferedReader(new InputStreamReader(is));
      String passwordLine = reader.readLine();
      if (passwordLine != null)
        return passwordLine.toCharArray();
      return null;

    } catch (IOException e) {

      throw new VOMSError("Error reading password from input stream: "
        + e.getMessage(), e);
    }
  }

}
