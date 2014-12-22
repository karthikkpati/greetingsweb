package com.solweaver.greetings.dto;


public class RecipientEventDTO {

	private Long eventId;
	
	private Long userId;
	
	private String eventName;

	private String description;
	
	private String recipientMessage;
	
	private String videoUrl;

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getRecipientMessage() {
		return recipientMessage;
	}

	public void setRecipientMessage(String recipientMessage) {
		this.recipientMessage = recipientMessage;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

}
