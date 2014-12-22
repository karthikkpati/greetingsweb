package com.solweaver.greetings.dto;

import javax.validation.constraints.NotNull;


public class GetRecipientEventRequest extends BaseRequest{

	@NotNull(message="UserId cannot be null")
	private Long userId;

	private Long eventId;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

}
