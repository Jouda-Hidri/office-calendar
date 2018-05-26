package com.office.app.domain;

import java.util.Date;

public class Meeting {

	Date submissionTime;
	String employeeId;
	Date startTime;
	int duration;

	public Meeting() {

	}

	public Meeting(Date submissionTime, String employeeId, Date startTime, int duration) {
		this.submissionTime = submissionTime;
		this.employeeId = employeeId;
		this.startTime = startTime;
		this.duration = duration;
	}

	public Date getSubmissionTime() {
		return submissionTime;
	}

	public void setSubmissionTime(Date submissionTime) {
		this.submissionTime = submissionTime;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public int getDuration() {
		return duration;
	}

	public void setDuration(int duration) {
		this.duration = duration;
	}

}
