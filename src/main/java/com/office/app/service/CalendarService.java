package com.office.app.service;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import com.office.app.domain.Meeting;
import com.office.app.domain.Office;

public class CalendarService {

	/**
	 * Filters meeting requests of a given office based on their times: Get all the
	 * meeting requests of the given office, filter meetings with simultaneous
	 * submissions, sort them based on their submission time, iterate them and check
	 * each if it is outside of the office hours or has a conflict with a meeting
	 * submitted earlier. If not add it to the list of accepted meetings, then add
	 * this list to the office object that will be returned
	 * 
	 * @param office
	 * @return office
	 */
	public Office checkMeetings(Office office) {
		// get meetings distinct by submission time
		List<Meeting> receivedRequests = getMeetingsDistinctBySubmissionTime(office.getMeetings());
		// sort meetings by submission time
		receivedRequests.sort((r1, r2) -> r1.getSubmissionTime().compareTo(r2.getSubmissionTime()));
		// iterate all the meeting requests
		List<Meeting> acceptedRequests = new ArrayList<Meeting>();
		for (Meeting receivedRequest : receivedRequests) {
			// check if the meeting is outside of the office hours
			if (!isOutsideOfficeHours(office, receivedRequest)) {
				// check if there is a conflict with a previous meeting
				boolean conflict = false;
				// iterate all the meetings already accepted, and check if there is a conflict
				Iterator<Meeting> it = acceptedRequests.iterator();
				while (!conflict & it.hasNext()) {
					conflict = isConflict(it.next(), receivedRequest);
				}
				// if everything is fine, add to the list of accepted requests
				if (!conflict) {
					acceptedRequests.add(receivedRequest);
				}
			}
		}

		office.setMeetings(acceptedRequests);
		return office;
	}

	public List<Meeting> getMeetingsDistinctBySubmissionTime(List<Meeting> meetings) {
		// In case there is more than one meeting submitted at a time, I keep the last
		// one in the given list
		Map<Date, Meeting> meetingBySubmissionTime = new HashMap<Date, Meeting>();
		for (Meeting meeting : meetings) {
			meetingBySubmissionTime.put(meeting.getSubmissionTime(), meeting);
		}
		return new ArrayList<Meeting>(meetingBySubmissionTime.values());
	}

	/**
	 * Check if the meeting is outside of the office hours: if the meeting starts
	 * before the office start time return true and if the meeting ends after the
	 * office end time return true : calculate difference between start time of the
	 * meeting and the end time of the office and in case the difference is less
	 * than the meeting duration, return true. At the end return false
	 * 
	 * @param office
	 * @param meeting
	 * @return boolean
	 */
	public boolean isOutsideOfficeHours(Office office, Meeting meeting) {
		// init variables
		Calendar officeStartTimeCalendar = Calendar.getInstance();
		Calendar officeEndTimeCalendar = Calendar.getInstance();
		Calendar meetingStartTimeCalendar = Calendar.getInstance();
		officeStartTimeCalendar.setTime(office.getStartTime());
		officeEndTimeCalendar.setTime(office.getEndTime());
		meetingStartTimeCalendar.setTime(meeting.getStartTime());
		// get all times in minutes
		int officeStartTime = (officeStartTimeCalendar.get(Calendar.HOUR_OF_DAY) * 60)
				+ officeStartTimeCalendar.get(Calendar.MINUTE);
		int officeEndTime = (officeEndTimeCalendar.get(Calendar.HOUR_OF_DAY) * 60)
				+ officeEndTimeCalendar.get(Calendar.MINUTE);
		int metingStartTime = (meetingStartTimeCalendar.get(Calendar.HOUR_OF_DAY) * 60)
				+ meetingStartTimeCalendar.get(Calendar.MINUTE);
		int meetingDuration = meeting.getDuration() * 60;

		// check start time
		if (metingStartTime < officeStartTime) {
			return true;
		}

		// check end time
		if (officeEndTime - metingStartTime < meetingDuration) {
			return true;
		}
		return false;
	}

	/**
	 * Check if the given meetings overlap: check which meeting starts earlier. In
	 * case the meetings start at the same time return true. If the different
	 * between both start times is less than the duration of the earlier meeting,
	 * return true. At the end return false.
	 * 
	 * @param meeting1
	 * @param meeting2
	 * @return boolean
	 */
	public boolean isConflict(Meeting m1, Meeting m2) {
		Date firstDate = m1.getStartTime();
		int firstMeetingDuration = m1.getDuration();
		Date secondDate = m2.getStartTime();
		if (m1.getStartTime().equals(m2.getStartTime())) {
			return true;
		} else {
			if (m2.getStartTime().before(m1.getStartTime())) {
				firstDate = m2.getStartTime();
				firstMeetingDuration = m2.getDuration();
				secondDate = m1.getStartTime();
			}
		}
		// difference between the start time of the first meeting and the start time of
		// the second meeting
		long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

		if (diff >= firstMeetingDuration) {
			return false;
		}
		return true;
	}

}
