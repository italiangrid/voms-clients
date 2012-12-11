package org.italiangrid.voms.clients.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
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
