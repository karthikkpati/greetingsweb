package com.solweaver.greetings.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.EventDAO;
import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventCreationResponse;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.InviteStatus;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.model.UserEventType;
import com.solweaver.greetings.model.UserStatus;
import com.solweaver.greetings.service.IEventService;
import com.solweaver.greetings.utils.EntityDtoUtils;
import com.solweaver.greetings.utils.GenericUtils;

@Service
public class EventServiceImpl implements IEventService{

	@Autowired
	private EventDAO eventDAO;
	
	@Autowired
	private UserDAO userDAO;
	
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
		List<UserEvent> emailInviteeUserEventList = null;
		
		if(emailInviteeList != null && !emailInviteeList.isEmpty()){
			emailInviteeUserEventList = new ArrayList<UserEvent>();
			for(String emailInvitee : emailInviteeList){
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
		
		event.setCreatedBy(user);
		
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


}
