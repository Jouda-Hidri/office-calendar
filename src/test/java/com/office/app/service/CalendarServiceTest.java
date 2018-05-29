package com.office.app.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.junit.Test;

import com.office.app.domain.Meeting;
import com.office.app.domain.Office;

import junit.framework.TestCase;

public class CalendarServiceTest extends TestCase {
	SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	CalendarService service = new CalendarService();

	@Test
	public void testChecMeetings() throws ParseException {
		DateFormat df = new SimpleDateFormat("HHmm", Locale.ENGLISH);
		Office office = new Office(df.parse("0900"), df.parse("1730"));
		Date submissionTime1 = formatter1.parse("2015-08-17 10:17:06");
		Date submissionTime2 = formatter1.parse("2015-08-16 12:34:56");
		Date submissionTime3 = formatter1.parse("2015-08-16 09:28:23");
		Date submissionTime4 = formatter1.parse("2015-08-17 11:23:45");
		Date submissionTime5 = formatter1.parse("2015-08-15 17:29:12");
		Date startTime1 = formatter2.parse("2015-08-21 09:00");
		Date startTime2 = formatter2.parse("2015-08-21 09:00");
		Date startTime3 = formatter2.parse("2015-08-22 14:00");
		Date startTime4 = formatter2.parse("2015-08-22 16:00");
		Date startTime5 = formatter2.parse("2015-08-21 16:00");
		Meeting meeting1 = new Meeting(submissionTime1, "1", startTime1, 2);
		Meeting meeting2 = new Meeting(submissionTime2, "2", startTime2, 2);
		Meeting meeting3 = new Meeting(submissionTime3, "3", startTime3, 2);
		Meeting meeting4 = new Meeting(submissionTime4, "3", startTime4, 1);
		Meeting meeting5 = new Meeting(submissionTime5, "3", startTime5, 3);
		List<Meeting> meetings = new ArrayList<Meeting>();
		meetings.add(meeting1);
		meetings.add(meeting2);
		meetings.add(meeting3);
		meetings.add(meeting4);
		meetings.add(meeting5);
		office.setMeetings(meetings);
		List<Meeting> expected = new ArrayList<Meeting>();
		expected.add(meeting2);
		expected.add(meeting3);
		expected.add(meeting4);

		Office result = service.checkMeetings(office);

		assertEquals(expected.size(), result.getMeetings().size());
	}

	@Test
	public void testGetMeetingsDistinctBySubmissionTime() throws ParseException {
		Date submissionTime1 = formatter1.parse("2015-08-17 10:17:06"); // same submission time
		Date submissionTime2 = formatter1.parse("2015-08-17 10:17:06"); // same submission time
		Date submissionTime3 = formatter1.parse("2015-08-17 10:17:00"); // different submission time
		Date startTime = formatter2.parse("2015-08-21 08:00");
		int duration = 2;
		Meeting meeting1 = new Meeting(submissionTime1, "1", startTime, duration);
		Meeting meeting2 = new Meeting(submissionTime2, "2", startTime, duration);
		Meeting meeting3 = new Meeting(submissionTime3, "3", startTime, duration);
		List<Meeting> meetings = new ArrayList<Meeting>();
		meetings.add(meeting1);
		meetings.add(meeting2);
		meetings.add(meeting3);

		List<Meeting> result = service.getMeetingsDistinctBySubmissionTime(meetings);

		assertEquals(2, result.size());
	}

	@Test
	public void testIsOutsideOfOfficeHours() throws ParseException {
		DateFormat df = new SimpleDateFormat("HHmm", Locale.ENGLISH);
		Office office = new Office(df.parse("0900"), df.parse("1730"));
		Date submissionTime = formatter1.parse("2015-08-17 10:17:06");
		Date startTime1 = formatter2.parse("2015-08-21 08:00");
		Date startTime2 = formatter2.parse("2015-08-21 17:00");
		Date startTime3 = formatter2.parse("2015-08-21 12:00");
		int duration = 2;
		Meeting meeting1 = new Meeting(submissionTime, "1", startTime1, duration);
		Meeting meeting2 = new Meeting(submissionTime, "2", startTime2, duration);
		Meeting meeting3 = new Meeting(submissionTime, "3", startTime3, duration);

		boolean result1 = service.isOutsideOfficeHours(office, meeting1);
		boolean result2 = service.isOutsideOfficeHours(office, meeting2);
		boolean result3 = service.isOutsideOfficeHours(office, meeting3);

		assertTrue(result1);
		assertTrue(result2);
		assertFalse(result3);
	}

	@Test
	public void testIsConflict() throws ParseException {
		Date submissionTime = formatter1.parse("2015-08-17 10:17:06");
		Date startTime1 = formatter2.parse("2015-08-21 09:00");
		Date startTime2 = formatter2.parse("2015-08-21 10:00");
		Date startTime3 = formatter2.parse("2015-08-21 11:00");
		Date startTime4 = formatter2.parse("2015-08-21 12:00");
		Date startTime5 = formatter2.parse("2015-08-21 9:00");
		int duration = 2;
		Meeting m1 = new Meeting(submissionTime, "1", startTime1, duration);
		Meeting m2 = new Meeting(submissionTime, "2", startTime2, duration);
		Meeting m3 = new Meeting(submissionTime, "3", startTime3, duration);
		Meeting m4 = new Meeting(submissionTime, "4", startTime4, duration);
		Meeting m5 = new Meeting(submissionTime, "5", startTime5, duration);

		boolean result1 = service.isConflict(m1, m2);
		boolean result2 = service.isConflict(m1, m3);
		boolean result3 = service.isConflict(m1, m4);
		boolean result4 = service.isConflict(m1, m5);

		assertTrue(result1);
		assertFalse(result2);
		assertFalse(result3);
		assertTrue(result4);
	}
}
