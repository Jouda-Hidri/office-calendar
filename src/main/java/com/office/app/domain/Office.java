package com.office.app.domain;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Office {

	Date startTime;
	Date endTime;

	List<Meeting> meetings = new ArrayList<Meeting>();

	public Office() {

	}

	public Office(Date startTime, Date endTime) {
		this.startTime = startTime;
		this.endTime = endTime;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public List<Meeting> getMeetings() {
		return meetings;
	}

	public void setMeetings(List<Meeting> meetings) {
		this.meetings = meetings;
	}
	
	public void addMeeting(Meeting meeting) {
		if(meetings == null) {
			meetings = new ArrayList<Meeting>();
		}
		meetings.add(meeting);
	}

}
