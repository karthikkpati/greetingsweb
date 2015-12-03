package com.solweaver.greetings.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.solweaver.greetings.dto.BaseResponse;
import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventCreationResponse;
import com.solweaver.greetings.dto.EventDeleteRequest;
import com.solweaver.greetings.dto.EventDeleteResponse;
import com.solweaver.greetings.dto.EventRespondRequest;
import com.solweaver.greetings.dto.EventUpdateRequest;
import com.solweaver.greetings.dto.EventUpdateResponse;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.GetCategoryRequest;
import com.solweaver.greetings.dto.GetCategoryResponse;
import com.solweaver.greetings.dto.GetEventRequest;
import com.solweaver.greetings.dto.GetEventResponse;
import com.solweaver.greetings.dto.GetRecipientEventRequest;
import com.solweaver.greetings.dto.GetRecipientEventResponse;
import com.solweaver.greetings.dto.GetThemeRequest;
import com.solweaver.greetings.dto.GetThemeResponse;
import com.solweaver.greetings.model.Channel;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.service.IEventService;
import com.solweaver.greetings.service.IThemeService;
import com.solweaver.greetings.service.IUserService;
import com.solweaver.greetings.service.IVelocityService;
import com.solweaver.greetings.utils.GenericUtils;

@Controller
public class EventController {

	@Autowired
	private IEventService eventService;
	
	@Autowired
	private IThemeService themeService;

	@Autowired
	private IVelocityService velocityService;
	
	@Autowired
	private IUserService userService;
	
	@RequestMapping(value="/event/create", method=RequestMethod.POST)
	public @ResponseBody EventCreationResponse createEvent(@Valid @RequestBody EventCreationRequest eventCreationRequest) throws IOException{
		EventCreationResponse eventCreationResponse = eventService.createEvent(eventCreationRequest);
		
		if(GenericUtils.isSuccess(eventCreationResponse)){
			try{
				User user = userService.findUserById(eventCreationRequest.getUserId());
				Map<String,Object> emailMap = new HashMap<String, Object>();
				emailMap.put("firstname", user.getFirstName());
				emailMap.put("lastname", user.getLastName());
				emailMap.put("eventname", eventCreationRequest.getEventName());
				velocityService.sendEmail(emailMap, "Event_Creation_Confirmation", user.getEmail());
			}catch (Exception e) {
				e.printStackTrace();
				GenericUtils.buildErrorDetail(eventCreationResponse, GenericEnum.Success, "Unable to send event creation confirmation");
			}
			
			try {
				velocityService.sendEmail(null, "Event_Creation_Invitee", eventCreationRequest.getEmailInviteeList());
			} catch (Exception e) {
				e.printStackTrace();
				GenericUtils.buildErrorDetail(eventCreationResponse, GenericEnum.Success, "Unable to send email to Invitees");
			}
		}
		
		return eventCreationResponse;
	}
	
	@RequestMapping(value="/event/viewAll", method=RequestMethod.POST)
	public @ResponseBody GetEventResponse getEvents(@Valid @RequestBody GetEventRequest getEventRequest,
			HttpServletRequest request) throws IOException{
		String mp4FinalUrl =  request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/streamMp4Final.mp4";
		return eventService.getEvents(getEventRequest, mp4FinalUrl);
	}
	
	@RequestMapping(value="/event/view", method=RequestMethod.POST)
	public @ResponseBody GetEventResponse getEvent(@Valid @RequestBody GetEventRequest getEventRequest,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		GetEventResponse getEventResponse = eventService.getEventDetails(getEventRequest);
		//String requestUrl = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/streamMp4Final.mp4?eventId="+getEventRequest.getEventId()+"&userId="+getEventRequest.getUserId();
	//	getEventResponse.setOutputVideo(requestUrl);
		return getEventResponse;
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
	
	@RequestMapping(value="/event/respond", method=RequestMethod.POST)
	public @ResponseBody BaseResponse respondEvent(@Valid @RequestBody EventRespondRequest eventRespondRequest) throws IOException{
		return eventService.respondEvent(eventRespondRequest);
	}

	@RequestMapping(value="/getThemes", method=RequestMethod.POST)
	public @ResponseBody GetThemeResponse getThemes(@Valid @RequestBody GetThemeRequest getThemeRequest) throws IOException{
		return themeService.getThemes(getThemeRequest);
	}
	
	@RequestMapping(value="/getCategories", method=RequestMethod.POST)
	public @ResponseBody GetCategoryResponse getThemes(@Valid @RequestBody GetCategoryRequest getCategoryRequest) throws IOException{
		return themeService.getCategories(getCategoryRequest);
	}
	
	@RequestMapping(value="/getRecipientEvents", method=RequestMethod.POST)
	public @ResponseBody GetRecipientEventResponse getRecipientVideo(@Valid @RequestBody GetRecipientEventRequest getRecipientEventRequest,
			HttpServletRequest request, HttpServletResponse response) throws IOException{
		String mp4FinalUrl =  request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/streamMp4Final.mp4";
		GetRecipientEventResponse getRecipientEventResponse = eventService.getRecipientEventDetails(getRecipientEventRequest, mp4FinalUrl);
		return getRecipientEventResponse;
	}
	
	@RequestMapping(value="/sendEmailToSender", method=RequestMethod.POST)
	public @ResponseBody BaseResponse sendEmailToSender() throws IOException{
		return eventService.sendRecipientEmail();
	}
	
	/*@RequestMapping(value="/event/remind", method=RequestMethod.POST)
	public @ResponseBody EventReminderRequest sendReminder(@Valid @RequestBody EventReminderRequest eventReminderRequest) throws IOException{
		return eventService.remind(eventReminderRequest);
	}*/
}

