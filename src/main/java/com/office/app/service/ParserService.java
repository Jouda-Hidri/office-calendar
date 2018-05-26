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

	public Office readText(String text) throws ParseException {

		String[] lines = text.split(System.getProperty("line.separator"));
		
		if (lines.length > 0) {
			Office office = parseOfficeHours(lines[0]);
			for (int i = 1; i < lines.length; i += 2) {
				Meeting meeting = parseMeeting(lines[i], lines[i + 1]);
				office.addMeeting(meeting);
			}
			return office;
		}
		return null;

	}

	private Office parseOfficeHours(String string) throws ParseException {
		String[] datesStr = string.split(" "); //TODO use regex instead of split
		if (datesStr.length>1) {
		    DateFormat df = new SimpleDateFormat("HHmm", Locale.ENGLISH);
		    Date startTime =  df.parse(datesStr[0]);
		    Date endTime =  df.parse(datesStr[1]);
		    return new Office (startTime, endTime);
		}
		return null;
	}

	public Meeting parseMeeting(String string1, String string2) {
		SimpleDateFormat formatter1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat formatter2 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
		String[] line1 = string1.split(" "); //TODO use regex instead of split
		if(line1.length<3) {
			return null;
		}
		String submissionTimeStr = line1[0]+" "+line1[1];
		
		String[] line2 = string2.split(" ");
		if(line2.length<3) {
			return null;
		}
		String StartTimeStr = line2[0]+" "+line2[1];
		
	       try {
	            Date submissionTime = formatter1.parse(submissionTimeStr);
	            String employeeId = line1[2];
	            Date startTime = formatter2.parse(StartTimeStr);
	            int duration = Integer.parseInt(line2[2]);
	            return new Meeting(submissionTime, employeeId, startTime, duration);
	        } catch (ParseException e) {
	        }
	       return null;
	}

	public String writeText(Office office) {
		String output = new String();
		List<Meeting> meetings = office.getMeetings();
		meetings.sort((r1, r2) -> r1.getStartTime().compareTo(r2.getStartTime()));
		String currentDay = new String();
		DateFormat dayFormatter = new SimpleDateFormat("dd");
		DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
		for(Meeting meeting : meetings) {
			String meetingDay = dayFormatter.format(meeting.getStartTime());
			if(!meetingDay.equals(currentDay)) {
				currentDay = meetingDay;
				output += currentDay;
				output += "\n";
			}
	        String startTime = dateFormatter.format(meeting.getStartTime());
	        output += startTime;
	        output += "\n";
//	        String endTime = TODO
		}
		return output;
	}

}
