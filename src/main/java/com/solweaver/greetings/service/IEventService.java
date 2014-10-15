package com.solweaver.greetings.service;

import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventCreationResponse;
import com.solweaver.greetings.dto.EventUpdateRequest;
import com.solweaver.greetings.dto.EventUpdateResponse;
import com.solweaver.greetings.dto.GetEventRequest;
import com.solweaver.greetings.dto.GetEventResponse;


public interface IEventService {

	public EventCreationResponse createEvent(EventCreationRequest eventCreationRequest);

	public GetEventResponse getEvents(GetEventRequest getEventRequest);

	public EventUpdateResponse updateEvent(EventUpdateRequest eventUpdateRequest);
}
