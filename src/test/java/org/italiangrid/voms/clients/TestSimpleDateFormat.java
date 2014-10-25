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
