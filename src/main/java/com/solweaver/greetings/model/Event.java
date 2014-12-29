package com.solweaver.greetings.model;


import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Event extends BaseEntity{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;

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
	@ManyToOne(fetch=FetchType.EAGER)
	private User createdBy;
	
	/**
	 * Event description
	 */
	private String description;
	
	/**
	 * Event Status
	 */
	private EventStatus eventStatus;
	
	/**
	 * User for which the event is created
	 */
	@OneToOne(fetch=FetchType.EAGER)
	private User recipientUser;
	
	private String createdByRecordedLink;
	
	private String fromMessage;

	private String recipientMessage;
	
	private VideoStatus videoStatus;
	
	/**
	 * List of Invitees participating in this event
	 *//*
	@OneToMany(mappedBy="event")
	private List<Invitee> inviteeList;*/
	
	@OneToMany(cascade = CascadeType.ALL, mappedBy="event", orphanRemoval=true)
	private List<UserEvent> userEventList;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public EventStatus getEventStatus() {
		return eventStatus;
	}

	public void setEventStatus(EventStatus eventStatus) {
		this.eventStatus = eventStatus;
	}

	/*public User getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUser(User recipientUser) {
		this.recipientUser = recipientUser;
	}*/

	public String getCreatedByRecordedLink() {
		return createdByRecordedLink;
	}

	public void setCreatedByRecordedLink(String createdByRecordedLink) {
		this.createdByRecordedLink = createdByRecordedLink;
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

	public List<UserEvent> getUserEventList() {
		return userEventList;
	}

	public void setUserEventList(List<UserEvent> userEventList) {
		this.userEventList = userEventList;
	}

	public String getFromMessage() {
		return fromMessage;
	}

	public void setFromMessage(String fromMessage) {
		this.fromMessage = fromMessage;
	}

	public User getRecipientUser() {
		return recipientUser;
	}

	public void setRecipientUser(User recipientUser) {
		this.recipientUser = recipientUser;
	}

	public String getRecipientMessage() {
		return recipientMessage;
	}

	public void setRecipientMessage(String recipientMessage) {
		this.recipientMessage = recipientMessage;
	}

	public VideoStatus getVideoStatus() {
		return videoStatus;
	}

	public void setVideoStatus(VideoStatus videoStatus) {
		this.videoStatus = videoStatus;
	}

/*	public List<Invitee> getInviteeList() {
		return inviteeList;
	}

	public void setInviteeList(List<Invitee> inviteeList) {
		this.inviteeList = inviteeList;
	}*/
	
}
