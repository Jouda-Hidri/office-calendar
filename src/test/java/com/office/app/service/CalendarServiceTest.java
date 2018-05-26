package com.office.app.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.office.app.domain.Meeting;

import junit.framework.TestCase;

public class CalendarServiceTest extends TestCase {
	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	CalendarService service = new CalendarService();

	@Test
	public void testIsConflict() throws ParseException {
		Date submissionTime = formatter1.parse("2015-08-17 10:17:06");
		Date startTime1 = formatter2.parse("2015-08-21 09:00");
		Date startTime2 = formatter2.parse("2015-08-21 10:00");
		Date startTime3 = formatter2.parse("2015-08-21 11:00");
		Date startTime4 = formatter2.parse("2015-08-21 12:00");
		int duration = 2;

		Meeting m1 = new Meeting(submissionTime, "1", startTime1, duration);
		Meeting m2 = new Meeting(submissionTime, "2", startTime2, duration);
		Meeting m3 = new Meeting(submissionTime, "3", startTime3, duration);
		Meeting m4 = new Meeting(submissionTime, "4", startTime4, duration);

		boolean result1 = service.isConflict(m1, m2);
		boolean result2 = service.isConflict(m1, m3);
		boolean result3 = service.isConflict(m1, m4);

		assertTrue(result1);
		assertFalse(result2);
		assertFalse(result3);
	}
}
