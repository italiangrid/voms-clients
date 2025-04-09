// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.italiangrid.voms.VOMSError;

/**
 * A utility class that determines the effective user id of user running the JVM
 * process.
 * 
 * @author andreaceccanti
 *
 */
public class EffectiveUserIdProvider {

  private static final String EUID_ENV_VAR = "EUID";
  private static final String EUID_SYSTEM_PROPERTY = "effectiveUserId";
  private static final String EUID_COMMAND = "id -u";

  private static String getEUIDStringFromEnv() {

    return System.getenv(EUID_ENV_VAR);

  }

  private static String getEUIDStringFromSystemProperty() {

    return System.getProperty(EUID_SYSTEM_PROPERTY);
  }

  private static String getEUIDStringFromIdCommand() {

    String euidString = null;

    ProcessBuilder pb = new ProcessBuilder(EUID_COMMAND.split(" "));
    try {
      Process p = pb.start();
      int exitStatus = p.waitFor();

      if (exitStatus != 0)
        throw new VOMSError(
          "Cannot resolve the user effective id: error invoking the '"
            + EUID_COMMAND + "' os command!");

      BufferedReader r = new BufferedReader(new InputStreamReader(
        p.getInputStream()));

      euidString = r.readLine();
      if (euidString == null)
        throw new VOMSError(
          "Cannot resolve the user effective id: nothing was found in '"
            + EUID_COMMAND + "' standard output!");

      return euidString;

    } catch (IOException e) {
      throw new VOMSError("Cannot resolve the user effective id: "
        + e.getMessage(), e);

    } catch (InterruptedException e) {
      return null;
    }
  }

  public static int getEUID() {

    int euid = -1;

    String euidString = getEUIDStringFromEnv();

    if (euidString == null)
      euidString = getEUIDStringFromSystemProperty();

    if (euidString == null)
      euidString = getEUIDStringFromIdCommand();

    if (euidString != null) {
      euid = Integer.parseInt(euidString);
      return euid;
    }

    throw new VOMSError("Cannot resolve the user effective id!");

  }

}
