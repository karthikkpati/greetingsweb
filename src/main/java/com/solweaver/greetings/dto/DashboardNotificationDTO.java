package com.solweaver.greetings.dto;

import java.util.Date;

public class DashboardNotificationDTO {

	private String eventType;
	
	private Long userEventId;
	
	private String eventDescription;
	
	private String description;
	
	private Long eventId;
	
	private UserDTO recipientDTO;
	
	/**
	 * Date on which event should be sent
	 */
	private Date eventDate;

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

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public UserDTO getRecipientDTO() {
		return recipientDTO;
	}

	public void setRecipientDTO(UserDTO recipientDTO) {
		this.recipientDTO = recipientDTO;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}
	
}
