package com.solweaver.greetings.dto;

public class DashboardNotificationDTO {

	private String eventType;
	
	private Long userEventId;
	
	private String description;
	
	private Long eventId;

	public String getEventType() {
		return eventType;
	}

	public void setEventType(String eventType) {
		this.eventType = eventType;
	}

	public Long getUserEventId() {
		return userEventId;
	}

	public void setUserEventId(Long userEventId) {
		this.userEventId = userEventId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}
	
}
