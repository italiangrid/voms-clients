package org.italiangrid.voms.clients.util;

import java.text.ParseException;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.italiangrid.voms.VOMSAttribute;
import org.italiangrid.voms.VOMSError;

public class TimeUtils {

	
	private static int getTimeIntervalInSeconds(int hours, int minutes){
		
		if (hours < 0)
			throw new VOMSError("Number of hours must be a positive integer.");
		
		if (minutes < 0)
			throw new VOMSError("Number of minutes must be a positive integer.");
		
		if (minutes > 59)
			throw new VOMSError("Number of minutes must be in the range 0-59");
		
		long timeIntervalInSeconds = TimeUnit.HOURS.toSeconds(hours) + TimeUnit.MINUTES.toSeconds(minutes);
		
		if (timeIntervalInSeconds > Integer.MAX_VALUE){
			String msg = String.format("The requested lifetime is too long. The maximum value is %d hours.", TimeUnit.SECONDS.toHours(Integer.MAX_VALUE));
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

		long hours = TimeUnit.MILLISECONDS.toHours(timeleft);

		long minutes = TimeUnit.MILLISECONDS.toMinutes(timeleft
				- TimeUnit.HOURS.toMillis(hours));

		long seconds = TimeUnit.MILLISECONDS.toSeconds(timeleft
				- TimeUnit.HOURS.toMillis(hours)
				- TimeUnit.MINUTES.toMillis(minutes));

		return (String.format("%02d:%02d:%02d", hours, minutes, seconds));
	}

	public static String getACValidityAsString(VOMSAttribute attr) {

		Date now = new Date();

		long timeDiff = attr.getNotAfter().getTime() - now.getTime();

		return getFormattedTime(timeDiff);
	}

	private TimeUtils() {
	}

}
