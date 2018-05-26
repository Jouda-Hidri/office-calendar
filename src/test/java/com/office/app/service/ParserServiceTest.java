package com.office.app.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import com.office.app.domain.Meeting;
import com.office.app.domain.Office;

import junit.framework.TestCase;

public class ParserServiceTest extends TestCase {

	SimpleDateFormat formatter1 = new SimpleDateFormat("HHmm");
	SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	SimpleDateFormat formatter3 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	ParserService service = new ParserService();

	@Test
	public void testReadText() throws ParseException {
		String text = "0900 1730\n" + //
				"2015-08-17 10:17:06 EMP001\n" + //
				"2015-08-21 09:00 2\n" + //
				"2015-08-16 12:34:56 EMP002\n" + //
				"2015-08-21 09:00 2\n" + //
				"2015-08-16 09:28:23 EMP003\n" + //
				"2015-08-22 14:00 2\n" + //
				"2015-08-17 11:23:45 EMP004\n" + //
				"2015-08-22 16:00 1\n" + //
				"2015-08-15 17:29:12 EMP005\n" + //
				"2015-08-21 16:00 3";
		Date startTime = formatter1.parse("0900");
		Date endTime = formatter1.parse("1730");
		Office expected = new Office(startTime, endTime);
		expected.addMeeting(new Meeting());
		expected.addMeeting(new Meeting());
		expected.addMeeting(new Meeting());
		expected.addMeeting(new Meeting());
		expected.addMeeting(new Meeting());

		Office result = service.readText(text);

		assertEquals(expected.getStartTime(), result.getStartTime());
		assertEquals(expected.getEndTime(), result.getEndTime());
		assertEquals(expected.getMeetings().size(), result.getMeetings().size());
	}
	
	@Test
	public void testParseMeeting() throws ParseException {
		String string1 = "2015-08-17 10:17:06 EMP001";
		String string2 = "2015-08-21 09:00 2";
		Date submissionTime = formatter2.parse("2015-08-17 10:17:06");
		String employeeId = "EMP001";
		Date startTime = formatter3.parse("2015-08-21 09:00");
		int duration = 2;
		Meeting expected = new Meeting(submissionTime, employeeId, startTime, duration);
		
		Meeting result = service.parseMeeting(string1, string2);
		
		assertEquals(expected.getSubmissionTime(), result.getSubmissionTime());
		assertEquals(expected.getEmployeeId(), result.getEmployeeId());
		assertEquals(expected.getStartTime(), result.getStartTime());
		assertEquals(expected.getDuration(), result.getDuration());
	}
	
	@Test
	public void testWriteText() throws ParseException {
		Office office = new Office( formatter1.parse("0900"), //
				formatter1.parse("1730"));
		office.addMeeting(new Meeting(formatter2.parse("2015-08-17 10:17:06"), //
				"EMP001", //
				formatter3.parse("2015-08-21 09:00"), //
				2));
		office.addMeeting(new Meeting(formatter2.parse("2015-08-16 12:34:56"), //
				"EMP002", //
				formatter3.parse("2015-08-21 09:00"), //
				2));
		office.addMeeting(new Meeting(formatter2.parse("2015-08-16 09:28:23"), //
				"EMP003", //
				formatter3.parse("2015-08-22 14:00"), //
				2));
		office.addMeeting(new Meeting(formatter2.parse("2015-08-17 11:23:45"), //
				"EMP004", //
				formatter3.parse("2015-08-22 16:00"), //
				1));
		office.addMeeting(new Meeting(formatter2.parse("2015-08-15 17:29:12"), //
				"EMP005", //
				formatter3.parse("2015-08-21 16:00"), //
				3));
		String expected = "2015-08-21\n" +// 
//				"\n" + 
				"09:00 11:00 EMP002\n" + //
				"2015-08-22\n" + //
				"14:00 16:00 EMP003\n" + // 
				"16:00 17:00 EMP004";//
		
		String result = service.writeText(office);
		
		assertEquals(expected, result);
	}

}
