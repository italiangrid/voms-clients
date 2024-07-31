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
