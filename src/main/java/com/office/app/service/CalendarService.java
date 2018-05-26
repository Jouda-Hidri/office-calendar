package com.office.app.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import com.office.app.domain.Meeting;
import com.office.app.domain.Office;

public class CalendarService {

	/**
	 * Among all the received request, duplicates will be removed based on the
	 * submission time
	 * 
	 * @param office
	 * @return office
	 */
	public Office removeDuplicates(Office office) {
		List<Meeting> receivedRequests = office.getMeetings();
		receivedRequests.sort((r1, r2) -> r1.getSubmissionTime().compareTo(r2.getSubmissionTime()));

		List<Meeting> acceptedRequests = new ArrayList<Meeting>();
		for (Meeting receivedRequest : receivedRequests) {
			boolean conflict = false;
			Iterator<Meeting> it = acceptedRequests.iterator();
			while (!conflict & it.hasNext()) {
				conflict = isConflict(it.next(), receivedRequest);
			}
			if (!conflict) {
				acceptedRequests.add(receivedRequest);
			}
		}
		
		office.setMeetings(acceptedRequests);
		return office;
	}

	public boolean isConflict(Meeting m1, Meeting m2) {
		Date firstDate = m1.getStartTime();
		int firstMeetingDuration = m1.getDuration();
		Date secondDate = m2.getStartTime();
		if (m1.getStartTime().equals(m2.getStartTime())) {
			//I admit duration is always > 0 hours
			return true;
		} else {
			if (m2.getStartTime().before(m1.getStartTime())) {
				firstDate = m2.getStartTime();
				firstMeetingDuration = m2.getDuration();
				secondDate = m1.getStartTime();
			}
		}

		long diffInMillies = Math.abs(secondDate.getTime() - firstDate.getTime());
		long diff = TimeUnit.HOURS.convert(diffInMillies, TimeUnit.MILLISECONDS);

		if (diff >= firstMeetingDuration) {
			return false;
		}
		return true;
	}

}
