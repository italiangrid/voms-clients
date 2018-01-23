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
