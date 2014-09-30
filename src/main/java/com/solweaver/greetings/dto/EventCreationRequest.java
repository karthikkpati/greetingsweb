package com.solweaver.greetings.dto;

import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotNull;


public class EventCreationRequest extends BaseRequest{

	@NotNull(message="UserId cannot be null")
	private Long userId;

	private String eventName;

	private boolean enableReminder;

	private Date eventDate;
	
	private Date videoSubmissionDate;
	
	private String eventDescription;
	
	private String fromMessage;
	
	private List<String> emailInviteeList;
	
	private String receiverEmail;

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

	public String getEventDescription() {
		return eventDescription;
	}

	public void setEventDescription(String eventDescription) {
		this.eventDescription = eventDescription;
	}

	public String getFromMessage() {
		return fromMessage;
	}

	public void setFromMessage(String fromMessage) {
		this.fromMessage = fromMessage;
	}

	public List<String> getEmailInviteeList() {
		return emailInviteeList;
	}

	public void setEmailInviteeList(List<String> emailInviteeList) {
		this.emailInviteeList = emailInviteeList;
	}

	public String getReceiverEmail() {
		return receiverEmail;
	}

	public void setReceiverEmail(String receiverEmail) {
		this.receiverEmail = receiverEmail;
	}

}
