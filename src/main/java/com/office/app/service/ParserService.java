package com.office.app.service;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import com.office.app.domain.Meeting;
import com.office.app.domain.Office;

public class ParserService {

	/**
	 * Parses the input text into an office: parse the office hours and then parse
	 * the meetings and add them to the office meetings list. In case the input is
	 * null or empty or the parsed office is null, return null.
	 * 
	 * @param text
	 * @return office
	 * @throws parseException
	 */

	public Office readText(String text) throws ParseException {
		if (text == null) {
			return null;
		}

		String[] lines = text.split(System.getProperty("line.separator"));

		if (lines.length > 0) {
			// parse office hours
			Office office = parseOfficeHours(lines[0]);
			if (office == null) {
				return null;
			}
			// iterate the remainder of the input
			for (int i = 1; i < lines.length; i += 2) {
				// parse the meeting
				Meeting meeting = parseMeeting(lines[i], lines[i + 1]);
				if (meeting != null) {
					// add meeting to the office meetings list
					office.addMeeting(meeting);
				}
			}
			return office;
		}
		return null;

	}

	/**
	 * Parses a string and creates a new office object. Return null in case the
	 * given string is null or has less than 2 elements.
	 * 
	 * @param string
	 * @return office
	 * @throws ParseException
	 */
	private Office parseOfficeHours(String string) throws ParseException {
		if (string == null) {
			return null;
		}
		String[] officeHours = string.split(" "); // TODO use regex instead of split
		if (officeHours.length > 1) {
			DateFormat df = new SimpleDateFormat("HHmm", Locale.ENGLISH);
			Date startTime = df.parse(officeHours[0]);
			Date endTime = df.parse(officeHours[1]);
			return new Office(startTime, endTime);
		}
		return null;
	}

	/**
	 * Parse the given strings and create a new meeting object. Return null in case
	 * the given strings are null or have less than 3 elements.
	 * 
	 * @param string1
	 * @param string2
	 * @return meeting
	 * @throws ParseException
	 */
	public Meeting parseMeeting(String string1, String string2) throws ParseException {
		// check the parameters
		if (string1 == null || string2 == null) {
			return null;
		}

		// extract 1st line
		String[] line1 = string1.split(" "); // TODO use regex instead of split
		if (line1.length < 3) {
			return null;
		}
		String submissionTimeStr = line1[0] + " " + line1[1];

		// extract 2nd line
		String[] line2 = string2.split(" ");
		if (line2.length < 3) {
			return null;
		}
		String StartTimeStr = line2[0] + " " + line2[1];

		// init the date formatters
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");

		// parse variables
		Date submissionTime = formatter1.parse(submissionTimeStr);
		String employeeId = line1[2];
		Date startTime = formatter2.parse(StartTimeStr);
		int duration = Integer.parseInt(line2[2]);
		return new Meeting(submissionTime, employeeId, startTime, duration);
	}

	/**
	 * Sort all the meetings of the given office based on their start time, iterate
	 * all the meetings and extend the output message by the meeting times grouped
	 * by the meeting days
	 * 
	 * @param office
	 * @return text
	 */
	public String createCalendar(Office office) {
		// TODO it is also possible to use Calendar to get day, hour, minute etc.
		DateFormat dayFormatter = new SimpleDateFormat("dd");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat timeFormatter = new SimpleDateFormat("HH:mm");
		// get the list of meetings and sort them
		List<Meeting> meetings = office.getMeetings();
		meetings.sort((r1, r2) -> r1.getStartTime().compareTo(r2.getStartTime()));
		// init variables for the for-loop
		String output = new String();
		String currentDay = new String();
		// iterate all the meetings
		for (Meeting meeting : meetings) {
			// init all the times, dates ...
			String meetingDay = dayFormatter.format(meeting.getStartTime());
			String meetingDate = dateFormatter.format(meeting.getStartTime());
			String startTime = timeFormatter.format(meeting.getStartTime());
			// calculate end time based on start time and duration
			String endTime = timeFormatter //
					.format(Date//
							.from(meeting.getStartTime()//
									.toInstant()//
									.plusSeconds(3600 * meeting.getDuration())));
			// check if the current day is a new day and if yes add it to output
			if (!meetingDay.equals(currentDay)) {
				currentDay = meetingDay;
				output += meetingDate;
				output += "\n";
			}
			// extend the output by the meeting times
			output += startTime;
			output += " ";
			output += endTime;
			output += " ";
			output += meeting.getEmployeeId();
			output += "\n";
		}
		return output;
	}
}