// SPDX-FileCopyrightText: 2006 Istituto Nazionale di Fisica Nucleare
//
// SPDX-License-Identifier: Apache-2.0

package org.italiangrid.voms.clients.util;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.italiangrid.voms.VOMSError;

public class TimeUtils {

  private static int getTimeIntervalInSeconds(int hours, int minutes) {

    if (hours < 0)
      throw new VOMSError("Number of hours must be a positive integer.");

    if (minutes < 0)
      throw new VOMSError("Number of minutes must be a positive integer.");

    if (minutes > 59)
      throw new VOMSError("Number of minutes must be in the range 0-59.");

    long timeIntervalInSeconds = TimeUnit.HOURS.toSeconds(hours)
      + TimeUnit.MINUTES.toSeconds(minutes);

    if (timeIntervalInSeconds > Integer.MAX_VALUE) {
      String msg = String.format(
        "The requested lifetime is too long. The maximum value is %d hours.",
        TimeUnit.SECONDS.toHours(Integer.MAX_VALUE));
      throw new VOMSError(msg);
    }

    return (int) timeIntervalInSeconds;

  }

  public static final int parseLifetimeInHours(String lifetimeString)
    throws ParseException {

    int hours = Integer.parseInt(lifetimeString);

    return getTimeIntervalInSeconds(hours, 0);

  }

  public static final int parseLifetimeInHoursAndMinutes(
    String acLifetimeProperty) throws ParseException {

    if (!acLifetimeProperty.contains(":"))
      throw new VOMSError("Illegal format for lifetime property.");

    String[] tokens = acLifetimeProperty.split(":");

    int hours = Integer.parseInt(tokens[0]);
    int minutes = Integer.parseInt(tokens[1]);

    return getTimeIntervalInSeconds(hours, minutes);

  }

  /*
   * Returns time in the custom format HH:mm:ss (e.g.: a value of more than 24
   * hours is allowed for the field HH )
   */
  public static final String getFormattedTime(long timeleft) {

    String formattedTime = null;

    if (timeleft <= 0)
      formattedTime = String.format("%02d:%02d:%02d", 0, 0, 0);
    else {
      final long hours = TimeUnit.MILLISECONDS.toHours(timeleft);

      final long minutes = TimeUnit.MILLISECONDS.toMinutes(timeleft
        - TimeUnit.HOURS.toMillis(hours));

      final long seconds = TimeUnit.MILLISECONDS.toSeconds(timeleft
        - TimeUnit.HOURS.toMillis(hours) - TimeUnit.MINUTES.toMillis(minutes));

      formattedTime = String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    return formattedTime;
  }

  public static final long getTimeLeft(Date end) {

    Date now = new Date();

    final long expireTime = end.getTime();
    final long currentTime = now.getTime();

    long timeleft = (expireTime - currentTime);

    if (timeleft <= 0)
      timeleft = 0;

    return timeleft;
  }

  public static final String getValidityAsString(Date endDate) {

    final long timeDiff = getTimeLeft(endDate);

    return getFormattedTime(timeDiff);
  }

  private TimeUtils() {

  }

}
