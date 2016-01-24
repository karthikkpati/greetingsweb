package com.solweaver.greetings.dto;

public class EventCreationResponse extends BaseResponse{

	private EventDTO eventDTO;

	public EventDTO getEventDTO() {
		return eventDTO;
	}

	public void setEventDTO(EventDTO eventDTO) {
		this.eventDTO = eventDTO;
	}
	
}
