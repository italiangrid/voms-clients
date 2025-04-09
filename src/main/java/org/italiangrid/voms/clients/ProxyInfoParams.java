// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients;

import java.util.EnumSet;

/**
 * This class represents the parameters that drive the {@link VomsProxyInfo}
 * command when inspecting a VOMS proxy.
 * 
 * @author Daniele Andreotti
 * 
 */

public class ProxyInfoParams {

  public enum PrintOption {

    SUBJECT,

    ISSUER,

    PROXY_EXISTS,

    AC_EXISTS,

    CHAIN,

    IDENTITY,

    TYPE,

    TIMELEFT,

    KEYSIZE,

    ALL_OPTIONS,

    TEXT,

    PROXY_PATH,

    VONAME,

    FQAN,

    ACSUBJECT,

    ACISSUER,

    ACTIMELEFT,

    ACSERIAL,

    SERVER_URI,

    KEYUSAGE,

    PROXY_TIME_VALIDITY,

    PROXY_HOURS_VALIDITY,

    PROXY_STRENGTH_VALIDITY;
  }

  private final EnumSet<PrintOption> setOfPrintOptions = EnumSet
    .noneOf(PrintOption.class);

  public final EnumSet<PrintOption> acOptions = EnumSet.of(
    PrintOption.AC_EXISTS, PrintOption.ACISSUER, PrintOption.ACSERIAL,
    PrintOption.ACSUBJECT, PrintOption.ACTIMELEFT, PrintOption.SERVER_URI,
    PrintOption.VONAME, PrintOption.FQAN);

  /**
   * Name of the proxy certificate file.
   */

  private String proxyFile;

  private boolean verifyAC = true;

  private String ACVO;

  private String keyLength;

  private String validTime;

  private String validHours;

  public String getProxyFile() {

    return proxyFile;
  }

  public void setProxyFile(String proxyFile) {

    this.proxyFile = proxyFile;
  }

  public boolean isVerifyAC() {

    return verifyAC;
  }

  public void setVerifyAC(boolean verifyAC) {

    this.verifyAC = verifyAC;
  }

  public void addPrintOption(PrintOption opt) {

    setOfPrintOptions.add(opt);
  }

  public boolean containsOption(PrintOption opt) {

    return setOfPrintOptions.contains(opt);
  }

  public boolean hasACOptions() {

    for (PrintOption p : acOptions)
      if (setOfPrintOptions.contains(p))
        return true;

    return false;
  }

  public String getACVO() {

    return ACVO;
  }

  public void setACVO(String aCVO) {

    ACVO = aCVO;
  }

  public String getKeyLength() {

    return keyLength;
  }

  public void setKeyLength(String keyLength) {

    this.keyLength = keyLength;
  }

  public String getValidTime() {

    return validTime;
  }

  public void setValidTime(String validTime) {

    this.validTime = validTime;
  }

  public String getValidHours() {

    return validHours;
  }

  public void setValidHours(String validHours) {

    this.validHours = validHours;
  }

  public boolean isEmpty() {

    return setOfPrintOptions.isEmpty();
  }

  public int getNumberOfOptions() {

    return setOfPrintOptions.size();

  }

}
