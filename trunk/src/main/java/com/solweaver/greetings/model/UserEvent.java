package com.solweaver.greetings.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserEvent extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name="event_id")
	private Event event;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	private UserEventType userEventType;
	
	private String inviteeLink;

	private String recordedLink;
	
	private InviteStatus inviteStatus;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public UserEventType getUserEventType() {
		return userEventType;
	}

	public void setUserEventType(UserEventType userEventType) {
		this.userEventType = userEventType;
	}

	public String getInviteeLink() {
		return inviteeLink;
	}

	public void setInviteeLink(String inviteeLink) {
		this.inviteeLink = inviteeLink;
	}

	public String getRecordedLink() {
		return recordedLink;
	}

	public void setRecordedLink(String recordedLink) {
		this.recordedLink = recordedLink;
	}

	public InviteStatus getInviteStatus() {
		return inviteStatus;
	}

	public void setInviteStatus(InviteStatus inviteStatus) {
		this.inviteStatus = inviteStatus;
	}
	
}
