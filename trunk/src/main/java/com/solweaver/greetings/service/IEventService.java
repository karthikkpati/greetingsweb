package com.solweaver.greetings.service;

import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventCreationResponse;


public interface IEventService {

	public EventCreationResponse createEvent(EventCreationRequest eventCreationRequest);
}
