package com.solweaver.greetings.dto;

import java.util.Date;
import java.util.List;

public class EventDTO {

	private Long userId;
	
	private String eventName;

	private boolean enableReminder;

	/**
	 * Date on which event should be sent
	 */
	private Date eventDate;
	
	/**
	 * Date by which invitees should submit their video
	 */
	private Date videoSubmissionDate;
	
	/**
	 * user who created this event
	 */
	private UserDTO createdBy;
	
	/**
	 * Event description
	 */
	private String description;
	
	/**
	 * Event Status
	 */
	private String eventStatus;
	
	/**
	 * User for which the event is created
	 */
	private UserDTO recipientUser;
	
	private String createdByRecordedLink;
	
	private String fromMessage;

	private List<UserEventDTO> userEventDTOList;
	
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

	public boolean isEnableReminder() {
		return enableReminder;
	}

	public void setEnableReminder(boolean enableReminder) {
		this.enableReminder = enableReminder;
	}

	public Date getEventDate() {
		return eventDate;
	}

	public void setEventDate(Date eventDate) {
		this.eventDate = eventDate;
	}

	public Date getVideoSubmissionDate() {
		return videoSubmissionDate;
	}

	public void setVideoSubmissionDate(Date videoSubmissionDate) {
		this.videoSubmissionDate = videoSubmissionDate;
	}

	public UserDTO getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(UserDTO createdBy) {
		this.createdBy = createdBy;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(String eventStatus) {
		this.eventStatus = eventStatus;
	}

	public UserDTO getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUser(UserDTO recipientUser) {
		this.recipientUser = recipientUser;
	}

	public String getCreatedByRecordedLink() {
		return createdByRecordedLink;
	}

	public void setCreatedByRecordedLink(String createdByRecordedLink) {
		this.createdByRecordedLink = createdByRecordedLink;
	}

	public String getFromMessage() {
		return fromMessage;
	}

	public void setFromMessage(String fromMessage) {
		this.fromMessage = fromMessage;
	}

	public List<UserEventDTO> getUserEventDTOList() {
		return userEventDTOList;
	}

	public void setUserEventDTOList(List<UserEventDTO> userEventDTOList) {
		this.userEventDTOList = userEventDTOList;
	}

}
