package org.italiangrid.voms.clients.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSError;

public class TimeUtils {

	public static final int parseLifetimeInHours(String lifetimeString)
			throws ParseException {

		int hours = Integer.parseInt(lifetimeString);

		long timeIntevalInSeconds = TimeUnit.HOURS.toSeconds(hours);
		return (int) timeIntevalInSeconds;

	}

	public static final int parseLifetimeInHoursAndMinutes(
			String acLifetimeProperty) throws ParseException {

		if (!acLifetimeProperty.contains(":"))
			throw new VOMSError("Illegal format for lifetime property.");
		
		String[] tokens = acLifetimeProperty.split(":");

		int hours = Integer.parseInt(tokens[0]);
		int minutes = Integer.parseInt(tokens[1]);

		long timeIntevalInSeconds = TimeUnit.HOURS.toSeconds(hours)
				+ TimeUnit.MINUTES.toSeconds(minutes);

		return (int) timeIntevalInSeconds;

	}

	public static String getACValidityAsString(VOMSAttribute attr) {

		String validityString = "00:00";

		Date now = new Date();

		long timeDiff = attr.getNotAfter().getTime() - now.getTime();

		if (timeDiff > 0) {
			Date validity = new Date(timeDiff);
			SimpleDateFormat df = new SimpleDateFormat("hh:mm:ss");
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			validityString = df.format(validity);
		}

		return validityString;
	}

	private TimeUtils() {
	}

}
