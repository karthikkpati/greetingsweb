package com.solweaver.greetings.service;

import com.solweaver.greetings.dto.BaseResponse;
import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventCreationResponse;
import com.solweaver.greetings.dto.EventDeleteRequest;
import com.solweaver.greetings.dto.EventDeleteResponse;
import com.solweaver.greetings.dto.EventReminderRequest;
import com.solweaver.greetings.dto.EventReminderResponse;
import com.solweaver.greetings.dto.EventRespondRequest;
import com.solweaver.greetings.dto.EventUpdateRequest;
import com.solweaver.greetings.dto.EventUpdateResponse;
import com.solweaver.greetings.dto.GetEventRequest;
import com.solweaver.greetings.dto.GetEventResponse;
import com.solweaver.greetings.model.UserEvent;


public interface IEventService {

	public EventCreationResponse createEvent(EventCreationRequest eventCreationRequest);

	public GetEventResponse getEvents(GetEventRequest getEventRequest);

	public EventUpdateResponse updateEvent(EventUpdateRequest eventUpdateRequest);

	public EventDeleteResponse deleteEvent(EventDeleteRequest eventDeleteRequest);

	public UserEvent getUserEvent(Long userId, Long eventId);

	public EventReminderResponse remind(EventReminderRequest eventReminderRequest);

	public GetEventResponse getEventDetails(GetEventRequest getEventRequest);

	public BaseResponse respondEvent(
			EventRespondRequest eventRespondRequest);
}
