package com.solweaver.greetings.utils;

import java.util.Date;

import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.UserDTO;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.InviteStatus;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.model.UserEventType;

public class EntityDtoUtils {
	
	public static UserDTO getUserDTO(User user){
		UserDTO userDTO = null;
		if(user != null){
			userDTO = new UserDTO();
			if(user.getDateOfBirth() != null){
				userDTO.setDateOfBirth(user.getDateOfBirth().toString());
			}
			userDTO.setEmailId(user.getEmail());
			userDTO.setFirstName(user.getFirstName());
			userDTO.setLastName(user.getLastName());
			userDTO.setGender(user.getGender().name());
			userDTO.setUserId(user.getId());
			userDTO.setUserStatus(user.getUserStatus().name());
		}
		return userDTO;
	}
	
	public static Event getEvent(EventCreationRequest eventCreationRequest){
		Event event = null;
		
		if(eventCreationRequest != null){
			event = new Event();
			event.setDescription(eventCreationRequest.getEventDescription());
			event.setEnableReminder(eventCreationRequest.isEnableReminder());
			event.setEventDate(eventCreationRequest.getEventDate());
			event.setVideoSubmissionDate(eventCreationRequest.getVideoSubmissionDate());
			event.setEventName(eventCreationRequest.getEventName());
			event.setFromMessage(eventCreationRequest.getFromMessage());
		}
		return event;
	}

	public static UserEvent getUserEvent(User user, Event event, InviteStatus inviteeStatus, UserEventType userEventType){
		UserEvent userEvent = new UserEvent();
		Date date = new Date();
		userEvent.setEvent(event);
		userEvent.setInviteStatus(inviteeStatus);
		userEvent.setUser(user);
		userEvent.setUserEventType(userEventType);
		userEvent.setCreationTime(date);
		userEvent.setModifiedTime(date);
		return userEvent;
	}
}
