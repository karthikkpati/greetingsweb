package com.solweaver.greetings.dto;

import java.util.List;

public class GetEventResponse extends BaseResponse{

	private List<EventDTO> eventDTOList;

	public List<EventDTO> getEventDTOList() {
		return eventDTOList;
	}

	public void setEventDTOList(List<EventDTO> eventDTOList) {
		this.eventDTOList = eventDTOList;
	}

}
