package org.italiangrid.voms.clients.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class LifetimeIntervalParser {

	public static final int parseLifetimeFromString(String acLifetimeProperty)
			throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");

		Calendar c = Calendar.getInstance();
		Date d;

		d = sdf.parse(acLifetimeProperty.trim());
		c.setTime(d);

		long timeIntevalInSeconds = TimeUnit.HOURS.toSeconds(c
				.get(Calendar.HOUR_OF_DAY))
				+ TimeUnit.MINUTES.toSeconds(c.get(Calendar.MINUTE));

		return (int) timeIntevalInSeconds;

	}
	
	private LifetimeIntervalParser() {}

}
