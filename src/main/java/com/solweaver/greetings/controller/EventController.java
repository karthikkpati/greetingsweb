package com.solweaver.greetings.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventCreationResponse;
import com.solweaver.greetings.dto.EventDeleteRequest;
import com.solweaver.greetings.dto.EventDeleteResponse;
import com.solweaver.greetings.dto.EventUpdateRequest;
import com.solweaver.greetings.dto.EventUpdateResponse;
import com.solweaver.greetings.dto.GetEventRequest;
import com.solweaver.greetings.dto.GetEventResponse;
import com.solweaver.greetings.dto.GetThemeRequest;
import com.solweaver.greetings.dto.GetThemeResponse;
import com.solweaver.greetings.model.Channel;
import com.solweaver.greetings.service.IEventService;
import com.solweaver.greetings.service.IThemeService;

@Controller
public class EventController {

	@Autowired
	private IEventService eventService;
	
	@Autowired
	private IThemeService themeService;
	
	@RequestMapping(value="/event/create", method=RequestMethod.POST)
	public @ResponseBody EventCreationResponse createEvent(@Valid @RequestBody EventCreationRequest eventCreationRequest) throws IOException{
		return eventService.createEvent(eventCreationRequest);
	}
	
	@RequestMapping(value="/event/viewAll", method=RequestMethod.POST)
	public @ResponseBody GetEventResponse getEvents(@Valid @RequestBody GetEventRequest getEventRequest) throws IOException{
		return eventService.getEvents(getEventRequest);
	}
	
	@RequestMapping(value="/event/viewAll1", method=RequestMethod.GET)
	public @ResponseBody GetEventRequest getEvents() throws IOException{
		GetEventRequest getEventRequest = new GetEventRequest();
		getEventRequest.setChannel(Channel.Ios.name());
		getEventRequest.setUserId(1L);
		return getEventRequest;
	}
	
	@RequestMapping(value="/event/create1", method=RequestMethod.GET)
	public @ResponseBody EventCreationRequest registerUser1() throws IOException{
		EventCreationRequest eventCreationRequest = new EventCreationRequest();
		eventCreationRequest.setChannel(Channel.Ios.name());
		List<String> emailInviteeList = new ArrayList<String>();
		emailInviteeList.add("abcd@abcd1.com");
		emailInviteeList.add("abcd@abcd2.com");
		emailInviteeList.add("abcd@abcd3.com");
		eventCreationRequest.setEmailInviteeList(emailInviteeList);
		eventCreationRequest.setEnableReminder(true);
		eventCreationRequest.setEventDate(new Date());
		eventCreationRequest.setEventDescription("Event Description");
		eventCreationRequest.setEventName("Dummy Event Name");
		eventCreationRequest.setFromMessage("Test From Message");
		eventCreationRequest.setReceiverEmail("receiver@receiver.com");
		eventCreationRequest.setUserId(1L);
		eventCreationRequest.setVideoSubmissionDate(new Date());
		return eventCreationRequest;
	}

	@RequestMapping(value="/event/update", method=RequestMethod.POST)
	public @ResponseBody EventUpdateResponse updateEvent(@Valid @RequestBody EventUpdateRequest eventUpdateRequest) throws IOException{
		return eventService.updateEvent(eventUpdateRequest);
	}

	@RequestMapping(value="/event/delete", method=RequestMethod.POST)
	public @ResponseBody EventDeleteResponse deleteEvent(@Valid @RequestBody EventDeleteRequest eventDeleteRequest) throws IOException{
		return eventService.deleteEvent(eventDeleteRequest);
	}

	@RequestMapping(value="/getThemes", method=RequestMethod.POST)
	public @ResponseBody GetThemeResponse getThemes(@Valid @RequestBody GetThemeRequest getThemeRequest) throws IOException{
		return themeService.getThemes(getThemeRequest);
	}
}
