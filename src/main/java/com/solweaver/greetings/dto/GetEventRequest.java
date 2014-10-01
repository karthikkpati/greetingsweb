package com.solweaver.greetings.dto;

import javax.validation.constraints.NotNull;


public class GetEventRequest extends BaseRequest{

	@NotNull(message="UserId cannot be null")
	private Long userId;

	private Long eventId;
	
	private String eventStatus;

	private boolean getUserDetails;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public boolean isGetUserDetails() {
		return getUserDetails;
	}

	public void setGetUserDetails(boolean getUserDetails) {
		this.getUserDetails = getUserDetails;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

}
