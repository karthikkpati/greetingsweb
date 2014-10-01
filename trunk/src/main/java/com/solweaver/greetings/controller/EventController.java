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
import com.solweaver.greetings.dto.GetEventRequest;
import com.solweaver.greetings.dto.GetEventResponse;
import com.solweaver.greetings.model.Channel;
import com.solweaver.greetings.service.IEventService;

@Controller
public class EventController {

	@Autowired
	private IEventService eventService;
	
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
	

}

