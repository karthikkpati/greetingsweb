package com.solweaver.greetings.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.EventDAO;
import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dao.UserEventDAO;
import com.solweaver.greetings.dto.BaseResponse;
import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventCreationResponse;
import com.solweaver.greetings.dto.EventDTO;
import com.solweaver.greetings.dto.EventDeleteRequest;
import com.solweaver.greetings.dto.EventDeleteResponse;
import com.solweaver.greetings.dto.EventErrorEnum;
import com.solweaver.greetings.dto.EventReminderRequest;
import com.solweaver.greetings.dto.EventReminderResponse;
import com.solweaver.greetings.dto.EventRespondRequest;
import com.solweaver.greetings.dto.EventUpdateRequest;
import com.solweaver.greetings.dto.EventUpdateResponse;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.GetEventRequest;
import com.solweaver.greetings.dto.GetEventResponse;
import com.solweaver.greetings.dto.GetRecipientEventRequest;
import com.solweaver.greetings.dto.GetRecipientEventResponse;
import com.solweaver.greetings.dto.RecipientEventDTO;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.EventStatus;
import com.solweaver.greetings.model.GenericConstants;
import com.solweaver.greetings.model.InviteStatus;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.model.UserEventType;
import com.solweaver.greetings.model.UserStatus;
import com.solweaver.greetings.model.VideoStatus;
import com.solweaver.greetings.service.IEventService;
import com.solweaver.greetings.service.IVelocityService;
import com.solweaver.greetings.utils.EntityDtoUtils;
import com.solweaver.greetings.utils.GenericUtils;

@Service
public class EventServiceImpl implements IEventService{

	@Autowired
	private IVelocityService velocityService;
	
	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private UserEventDAO userEventDAO;
	
	@Override
	@Transactional
	public EventCreationResponse createEvent(
			EventCreationRequest eventCreationRequest) {
		EventCreationResponse eventCreationResponse = new EventCreationResponse();
		User user = userDAO.findById(eventCreationRequest.getUserId(), false);
		
		if(user == null || !user.getUserStatus().equals(UserStatus.Active)){
			GenericUtils.buildErrorDetail(eventCreationResponse, GenericEnum.INVALID_USER);
			return eventCreationResponse;
		}

		Event event = EntityDtoUtils.getEvent(eventCreationRequest);
		
		List<String> emailInviteeList = eventCreationRequest.getEmailInviteeList();
		List<UserEvent> emailInviteeUserEventList = new ArrayList<UserEvent>();
		
		if(emailInviteeList != null && !emailInviteeList.isEmpty()){
			for(String emailInvitee : emailInviteeList){
				if(!emailInvitee.isEmpty()){
					if(eventCreationRequest.getReceiverEmail() != null && !eventCreationRequest.getReceiverEmail().isEmpty() && eventCreationRequest.getReceiverEmail().equalsIgnoreCase(emailInvitee)){
						GenericUtils.buildErrorDetail(eventCreationResponse, GenericEnum.INVALID_INVITEE_RECIPIENT);
						return eventCreationResponse;
					}
					User emailInviteeUser = userDAO.findExistingUserByEmail(emailInvitee);
					if(emailInviteeUser == null){
						emailInviteeUser = new User();
						emailInviteeUser.setEmail(emailInvitee);
						emailInviteeUser.setUserStatus(UserStatus.InActive);
						userDAO.makePersistent(emailInviteeUser);
					}
					UserEvent userEvent = EntityDtoUtils.getUserEvent(emailInviteeUser, event, InviteStatus.Pending, UserEventType.Invitee);
					emailInviteeUserEventList.add(userEvent);
				}
			}
		}
		
		event.setCreatedBy(user);
		event.setEventStatus(EventStatus.Active);
		
		UserEvent userEvent = EntityDtoUtils.getUserEvent(user, event, InviteStatus.Accepted, UserEventType.Inviter);
		emailInviteeUserEventList.add(userEvent);
		
		if(eventCreationRequest.getReceiverEmail() != null){
			User recipientUser = userDAO.findExistingUserByEmail(eventCreationRequest.getReceiverEmail());
			
			if(recipientUser == null){
				recipientUser = new User();
				recipientUser.setEmail(eventCreationRequest.getReceiverEmail());
				recipientUser.setUserStatus(UserStatus.InActive);
				userDAO.makePersistent(recipientUser);
			}
			UserEvent recipientUserEvent = EntityDtoUtils.getUserEvent(recipientUser, event, InviteStatus.Pending, UserEventType.RECIPIENT);
			emailInviteeUserEventList.add(recipientUserEvent);
			event.setRecipientUser(recipientUser);
		}
		
		if(emailInviteeUserEventList != null){
			event.setUserEventList(emailInviteeUserEventList);
		}
		eventDAO.makePersistent(event);
		eventCreationResponse.setEventID(event.getId());
		GenericUtils.buildErrorDetail(eventCreationResponse, GenericEnum.Success);
		return eventCreationResponse;
	}

	@Override
	@Transactional
	public GetEventResponse getEvents(GetEventRequest getEventRequest) {
		GetEventResponse getEventResponse = new GetEventResponse();
		User user = userDAO.findById(getEventRequest.getUserId(), false);
		if(user == null || !user.getUserStatus().equals(UserStatus.Active)){
			GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.INVALID_USER);
			return getEventResponse;
		}

		EventStatus eventStatus = null;

		if(getEventRequest.getEventStatus() != null){
			try{
				eventStatus = EventStatus.valueOf(getEventRequest.getEventStatus());
			}catch(Exception exception){
				GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.INVALID_EVENT_STATUS);
				return getEventResponse;
			}
		}
		
		InviteStatus inviteeStatus = null;
		
		if(getEventRequest.getInviteeStatus() != null){
			try{
				inviteeStatus = InviteStatus.valueOf(getEventRequest.getInviteeStatus());
			}catch(Exception exception){
				GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.INVALID_INVITEE_STATUS);
				return getEventResponse;
			}
		}
		
		UserEventType userEventType = null;
		
		if(getEventRequest.getUserEventType() != null){
			try{
				userEventType = UserEventType.valueOf(getEventRequest.getUserEventType());
			}catch(Exception exception){
				GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.INVALID_USER_EVENT);
				return getEventResponse;
			}
		}
				
		List<Event> eventList = eventDAO.findEventsByUserId(user.getId(), getEventRequest.getEventId(), getEventRequest.isGetUserDetails(), eventStatus, inviteeStatus, userEventType);
		
		List<EventDTO> eventDTOList = EntityDtoUtils.getEventDTOList(eventList, getEventRequest.isGetUserDetails());
		
		getEventResponse.setEventDTOList(eventDTOList);
		GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.Success);
		return getEventResponse;
	}

	@Override
	@Transactional
	public EventUpdateResponse updateEvent(EventUpdateRequest eventUpdateRequest) {
		EventUpdateResponse eventUpdateResponse = new EventUpdateResponse();
		User user = userDAO.findById(eventUpdateRequest.getUserId(), false);
		if(user == null || !user.getUserStatus().equals(UserStatus.Active)){
			GenericUtils.buildErrorDetail(eventUpdateResponse, GenericEnum.INVALID_USER);
			return eventUpdateResponse;
		}

		Event event = eventDAO.findByEventAndUserId(eventUpdateRequest.getEventId(), eventUpdateRequest.getUserId());
		
		if(event == null){
			GenericUtils.buildErrorDetail(eventUpdateResponse, GenericEnum.INVALID_EVENT);
			return eventUpdateResponse;
		}
		
		List<UserEvent> userEventList = event.getUserEventList();
		Map<Long, User> userEventMap = null;
		UserEvent recipieUserEvent = null;
		if(userEventList != null && userEventList.size() > 0){
			userEventMap= new HashMap<Long, User>();
			for(UserEvent userEvent : userEventList){
				User eventUser = userEvent.getUser();
				userEventMap.put(eventUser .getId(), eventUser);
				if(userEvent.getUserEventType().equals(UserEventType.RECIPIENT)){
					recipieUserEvent = userEvent;
				}
			}
		}
		List<String> emailInviteeList = eventUpdateRequest.getEmailInviteeList();
		List<UserEvent> emailInviteeUserEventList = event.getUserEventList();;
		
		if(emailInviteeUserEventList == null){
			emailInviteeUserEventList = new ArrayList<UserEvent>();
			event.setUserEventList(emailInviteeUserEventList);
		}
		
		if(emailInviteeList != null && !emailInviteeList.isEmpty()){
			for(String emailInvitee : emailInviteeList){
				if((recipieUserEvent != null && recipieUserEvent.getUser().getEmail().equalsIgnoreCase(emailInvitee)) || 
						(eventUpdateRequest.getReceiverEmail() != null && eventUpdateRequest.getReceiverEmail().equalsIgnoreCase(emailInvitee))){
					GenericUtils.buildErrorDetail(eventUpdateResponse, GenericEnum.INVALID_INVITEE_RECIPIENT);
					return eventUpdateResponse;
				}
				User emailInviteeUser = userDAO.findExistingUserByEmail(emailInvitee);
				if(emailInviteeUser == null){
					emailInviteeUser = new User();
					emailInviteeUser.setEmail(emailInvitee);
					emailInviteeUser.setUserStatus(UserStatus.InActive);
					userDAO.makePersistent(emailInviteeUser);
					UserEvent userEvent = EntityDtoUtils.getUserEvent(emailInviteeUser, event, InviteStatus.Pending, UserEventType.Invitee);
					emailInviteeUserEventList.add(userEvent);
				}else{
					if(userEventMap.get(emailInviteeUser.getId()) != null){
						/*GenericUtils.buildErrorDetail(eventUpdateResponse, GenericEnum.USER_EVENT_EXISTS, emailInviteeUser.getEmail()+" "+GenericEnum.USER_EVENT_EXISTS.message);
						return eventUpdateResponse;*/
						break;
					}else{
						UserEvent userEvent = EntityDtoUtils.getUserEvent(emailInviteeUser, event, InviteStatus.Pending, UserEventType.Invitee);
						emailInviteeUserEventList.add(userEvent);
					}
				}
			}
		}
		
		if(eventUpdateRequest.getReceiverEmail() != null){
			User recipientUser = event.getRecipientUser();
			
			if(recipientUser == null || !recipientUser.getEmail().equals(eventUpdateRequest.getReceiverEmail())){
				recipientUser = userDAO.findExistingUserByEmail(eventUpdateRequest.getReceiverEmail());
				if(recipientUser == null){
					recipientUser = new User();
					recipientUser.setEmail(eventUpdateRequest.getReceiverEmail());
					recipientUser.setUserStatus(UserStatus.InActive);
					userDAO.makePersistent(recipientUser);
				}
				
				UserEvent newRecipientUserEvent = EntityDtoUtils.getUserEvent(recipientUser, event, InviteStatus.Pending, UserEventType.RECIPIENT);
				emailInviteeUserEventList.add(newRecipientUserEvent);
				
				if(recipieUserEvent != null || (recipientUser != null && !recipientUser.getEmail().equals(eventUpdateRequest.getReceiverEmail()))){
					//userEventDAO.makeTransient(recipieUserEvent);
					emailInviteeUserEventList.remove(recipieUserEvent);
				}
				event.setRecipientUser(recipientUser);
			}
		}
		
		/*if(emailInviteeUserEventList != null){
			event.setUserEventList(emailInviteeUserEventList);
		}*/
		
		if(eventUpdateRequest.getEventDate() != null){
			event.setEventDate(eventUpdateRequest.getEventDate());
		}
		
		if(eventUpdateRequest.getEventDescription() != null){
			event.setDescription(eventUpdateRequest.getEventDescription());
		}
		
		if(eventUpdateRequest.getEventName() != null){
			event.setEventName(eventUpdateRequest.getEventName());
		}

		if(eventUpdateRequest.getFromMessage() != null){
			event.setFromMessage(eventUpdateRequest.getFromMessage());
		}
		
		if(eventUpdateRequest.getVideoSubmissionDate() != null){
			event.setVideoSubmissionDate(eventUpdateRequest.getVideoSubmissionDate());
		}
		
		event.setEnableReminder(eventUpdateRequest.isEnableReminder());
		eventDAO.merge(event);
		eventUpdateResponse.setEventID(event.getId());
		
		GenericUtils.buildErrorDetail(eventUpdateResponse, GenericEnum.Success);
		return eventUpdateResponse;
	}

	@Override
	@Transactional
	public EventDeleteResponse deleteEvent(EventDeleteRequest eventDeleteRequest) {
		EventDeleteResponse eventDeleteResponse = new EventDeleteResponse();
		Event event = eventDAO.findByEventAndUserId(eventDeleteRequest.getEventId(), eventDeleteRequest.getUserId());

		if(event != null){
			event.setEventStatus(EventStatus.Deleted);
			eventDAO.merge(event);
		}else{
			GenericUtils.buildErrorDetail(eventDeleteResponse, GenericEnum.INVALID_EVENT);
			return eventDeleteResponse;
		}
		
		GenericUtils.buildErrorDetail(eventDeleteResponse, GenericEnum.Success);
		return eventDeleteResponse;
	}

	@Override
	@Transactional
	public UserEvent getUserEvent(Long userId, Long eventId) {
		return userEventDAO.findByUserAndEvent(userId, eventId);
	}

	@Override
	@Transactional
	public EventReminderResponse remind(
			EventReminderRequest eventReminderRequest) {
		EventReminderResponse eventReminderResponse = new EventReminderResponse();
		UserEvent userEvent = userEventDAO.findByUserAndEvent(eventReminderRequest.getUserId(), eventReminderRequest.getEventId());
		if(userEvent == null){
			GenericUtils.buildErrorDetail(eventReminderResponse, GenericEnum.INVALID_USER_EVENT);
			return eventReminderResponse;
		}
		
		if(userEvent.getEvent().getEventDate().compareTo(new Date()) > 0){
			Map<String,Object> eventReminderMap = new HashMap<String, Object>();
			Event event = userEvent.getEvent();
			List<UserEvent> userEventList = event.getUserEventList();
		//	velocityService.sendEmail(eventReminderMap, "EventReminder", emailList);
		}
		return eventReminderResponse;
	}

	@Override
	public GetEventResponse getEventDetails(GetEventRequest getEventRequest) {
		GetEventResponse getEventResponse = new GetEventResponse();
		
		UserEvent userEvent = userEventDAO.findByUserAndEvent(getEventRequest.getUserId(), getEventRequest.getEventId());
		if(userEvent == null){
			GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.INVALID_USER_EVENT);
			return getEventResponse;
		}
		return null;
	}

	@Override
	@Transactional
	public BaseResponse respondEvent(
			EventRespondRequest eventRespondRequest) {
		BaseResponse getEventResponse = new BaseResponse();

		UserEvent userEvent = userEventDAO.findByUserAndEvent(eventRespondRequest.getUserId(), eventRespondRequest.getEventId());
		if(userEvent == null){
			GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.INVALID_USER_EVENT);
			return getEventResponse;
		}

		InviteStatus inviteeStatus = null;
		try{
			inviteeStatus = InviteStatus.valueOf(eventRespondRequest.getInviteeStatus());
		}catch(Exception exception){
			GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.INVALID_INVITEE_STATUS);
			return getEventResponse;
		}
		
		if(inviteeStatus.equals(InviteStatus.Accepted) || inviteeStatus.equals(InviteStatus.Rejected)){
			userEvent.setInviteStatus(inviteeStatus);
		}else{
			GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.INVALID_INVITEE_STATUS);
			return getEventResponse;
		}
		
		userEventDAO.merge(userEvent);
		GenericUtils.buildErrorDetail(getEventResponse, GenericEnum.Success);
		return getEventResponse;
	}

	@Override
	@Transactional
	public Event getEvent(Long eventId) {
		return eventDAO.findEventById(eventId);
	}

	@Override
	@Transactional
	public GetRecipientEventResponse getRecipientEventDetails(
			GetRecipientEventRequest getRecipientEventRequest, String mp4FinalUrl) {
		GetRecipientEventResponse getRecipientEventResponse = new GetRecipientEventResponse();
		Event event = null;
		User user = userDAO.findActiveUserById(getRecipientEventRequest.getUserId());
		if(user == null ){
			GenericUtils.buildErrorDetail(getRecipientEventResponse, GenericEnum.INVALID_USER);
			return getRecipientEventResponse;
		}
		
		if(getRecipientEventRequest.getEventId() != null){
			event = eventDAO.findEventById(getRecipientEventRequest.getEventId());
			if(event == null){
				GenericUtils.buildErrorDetail(getRecipientEventResponse, GenericEnum.INVALID_EVENT);
				return getRecipientEventResponse;
			}
		}

		List<RecipientEventDTO> recipientEventDTOList = new ArrayList<RecipientEventDTO>();
		RecipientEventDTO recipientEventDTO = null;
		if(event != null){
			recipientEventDTO = new RecipientEventDTO();
			recipientEventDTO.setDescription(event.getDescription());
			recipientEventDTO.setEventId(event.getId());
			recipientEventDTO.setEventName(event.getEventName());
			recipientEventDTO.setRecipientMessage(event.getRecipientMessage());
			recipientEventDTO.setVideoUrl(mp4FinalUrl+"?eventId="+event.getId()+"&userId="+getRecipientEventRequest.getUserId());
			recipientEventDTOList.add(recipientEventDTO);
		}else{
			List<Event> recipientevents = eventDAO.findEventsByRecipientUserId(user.getId());
			if(recipientevents != null){
				for(Event recipientEvent : recipientevents){
					recipientEventDTO = new RecipientEventDTO();
					recipientEventDTO.setDescription(recipientEvent.getDescription());
					recipientEventDTO.setEventId(recipientEvent.getId());
					recipientEventDTO.setEventName(recipientEvent.getEventName());
					recipientEventDTO.setRecipientMessage(recipientEvent.getRecipientMessage());
					recipientEventDTO.setVideoUrl(mp4FinalUrl+"?eventId="+recipientEvent.getId()+"&userId="+getRecipientEventRequest.getUserId());
					recipientEventDTOList.add(recipientEventDTO);
				}
			}
		}
		
		getRecipientEventResponse.setRecipientEventDTOList(recipientEventDTOList);
		GenericUtils.buildErrorDetail(getRecipientEventResponse, GenericEnum.Success);
		return getRecipientEventResponse;
	}
	
	@Override
	@Transactional
	public BaseResponse sendRecipientEmail(){
		BaseResponse baseResponse = new BaseResponse();
		List<Event> eventsList = eventDAO.findEventsToSend();
		Map<String,Object> emailMap = null;
		if(eventsList != null){
			System.out.println("Number of events to send email are : "+eventsList.size());
			for(Event event : eventsList){
				User recipientUser = event.getRecipientUser();
				if(event.getEventStatus().equals(EventStatus.Completed) && recipientUser != null){
					try{
						emailMap = new HashMap<String, Object>();
						if(recipientUser.getFirstName() != null ){
							emailMap.put(GenericConstants.FIRST_NAME, recipientUser.getFirstName());
						}
						if(recipientUser.getLastName() != null){
							emailMap.put(GenericConstants.LAST_NAME, recipientUser.getLastName());
						}
						List<UserEvent> userEventList = event.getUserEventList();
						StringBuffer inviteeList = new StringBuffer();
						for(UserEvent userEvent : userEventList){
							if(!userEvent.getUserEventType().equals(UserEventType.RECIPIENT) && userEvent.getInviteStatus().equals(InviteStatus.Accepted)){
								inviteeList.append(userEvent.getUser().getFirstName());
								inviteeList.append(" ");
								inviteeList.append(userEvent.getUser().getLastName());
								inviteeList.append(" , ");
							}
						}
						if(event.getVideoStatus().equals(VideoStatus.Completed)){
							emailMap.put(GenericConstants.SENDERS, inviteeList.toString());
						}
						velocityService.sendEmail(emailMap, "Event_Sender", recipientUser.getEmail());
					}catch (Exception e) {
						e.printStackTrace();
						GenericUtils.buildErrorDetail(baseResponse, GenericEnum.SENDER_EMAIL_FAILED, "Failed to Send email for event : "+event.getId());
					}
				}
			}
		}

		if(baseResponse.getErrorDetailList() != null && GenericUtils.isSuccess(baseResponse)){
			GenericUtils.buildErrorDetail(baseResponse, GenericEnum.Success);
		}
		return baseResponse;
	}
	
}
