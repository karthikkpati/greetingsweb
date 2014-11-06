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
import com.solweaver.greetings.dto.EventReminderRequest;
import com.solweaver.greetings.dto.EventReminderResponse;
import com.solweaver.greetings.dto.EventRespondRequest;
import com.solweaver.greetings.dto.EventUpdateRequest;
import com.solweaver.greetings.dto.EventUpdateResponse;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.GetEventRequest;
import com.solweaver.greetings.dto.GetEventResponse;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.EventStatus;
import com.solweaver.greetings.model.InviteStatus;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.model.UserEventType;
import com.solweaver.greetings.model.UserStatus;
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
		
		UserEvent userEvent = EntityDtoUtils.getUserEvent(user, event, InviteStatus.Pending, UserEventType.Invitee);
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
		
		List<Event> eventList = eventDAO.findEventsByUserId(user.getId(), getEventRequest.getEventId(), getEventRequest.isGetUserDetails(), eventStatus);
		
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
}
