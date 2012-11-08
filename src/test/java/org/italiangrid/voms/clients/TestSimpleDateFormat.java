package org.italiangrid.voms.clients;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.junit.Assert;
import org.junit.Test;

public class TestSimpleDateFormat {

	

	@Test
	public void test() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("hh:mm");
		
		String[] correctValues = {"16:00", // 16 hours
				"4:00", // 4 hours
				"4:12", // 4 hours and 12 minutes
				"4:3" // 4 hours and 3 minutes
				};
		
		long[] lengthInSeconds = {TimeUnit.HOURS.toSeconds(16),
				TimeUnit.HOURS.toSeconds(4),
				TimeUnit.HOURS.toSeconds(4)+TimeUnit.MINUTES.toSeconds(12),
				TimeUnit.HOURS.toSeconds(4)+TimeUnit.MINUTES.toSeconds(3)
		};
		
		for (int i=0; i < correctValues.length; i++){
			
			Calendar c = Calendar.getInstance();
			Date d = sdf.parse(correctValues[i]);
			c.setTime(d);
			
			long calculatedInterval = TimeUnit.HOURS.toSeconds(c.get(Calendar.HOUR_OF_DAY))+
					TimeUnit.MINUTES.toSeconds(c.get(Calendar.MINUTE));
			
			Assert.assertTrue(lengthInSeconds[i] == calculatedInterval);
		}
	}

}
