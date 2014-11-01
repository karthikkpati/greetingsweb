package com.solweaver.greetings.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventDTO;
import com.solweaver.greetings.dto.ThemeDTO;
import com.solweaver.greetings.dto.UserDTO;
import com.solweaver.greetings.dto.UserEventDTO;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.InviteStatus;
import com.solweaver.greetings.model.Theme;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.model.UserEventType;

public class EntityDtoUtils {
	
	public static String recordedLink = "http://greetings.cloudapp.net/Greetings/download?";
	
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
			if(user.getGender() != null){
				userDTO.setGender(user.getGender().name());
			}
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

	public static List<EventDTO> getEventDTOList(List<Event> eventList, boolean isUserEvent) {
		List<EventDTO> eventDTOList = null;
		if(eventList != null && eventList.size() > 0){
			eventDTOList = new ArrayList<EventDTO>();
			for(Event event:eventList){
				EventDTO eventDTO = getEventDTO(event, isUserEvent);
				eventDTO.setEventId(event.getId());
				eventDTOList.add(eventDTO);
			}
		}
		return eventDTOList;
	}
	
	public static EventDTO getEventDTO(Event event, boolean isUserEvent){
		EventDTO eventDTO = new EventDTO();
		eventDTO.setCreatedByRecordedLink(event.getCreatedByRecordedLink());
		eventDTO.setDescription(event.getDescription());
		eventDTO.setEnableReminder(event.isEnableReminder());
		eventDTO.setEventDate(event.getEventDate());
		eventDTO.setEventName(event.getEventName());
		if(event.getEventStatus() != null){
			eventDTO.setEventStatus(event.getEventStatus().name());
		}
		
		if(isUserEvent){
			List<UserEvent> userEventList = event.getUserEventList();
			List<UserEventDTO> userEventDTOList = null;
			if(userEventList != null && userEventList.size() > 0){
				userEventDTOList = new ArrayList<UserEventDTO>();
				for(UserEvent userEvent : userEventList){
					userEventDTOList.add(getUserEventDTO(userEvent));
				}
			}
			eventDTO.setUserEventDTOList(userEventDTOList);
		}
		eventDTO.setFromMessage(event.getFromMessage());
		eventDTO.setVideoSubmissionDate(event.getVideoSubmissionDate());
		
		UserDTO createdByUser = getUserDTO(event.getCreatedBy());
		eventDTO.setCreatedBy(createdByUser);
		
		UserDTO recipientUser = getUserDTO(event.getRecipientUser());
		eventDTO.setRecipientUser(recipientUser);
		
		return eventDTO;
	}
	
	public static UserEventDTO getUserEventDTO(UserEvent userEvent){
		UserEventDTO userEventDTO = new UserEventDTO();
		userEventDTO.setUserDTO(getUserDTO(userEvent.getUser()));
		/*userEventDTO.setInviteeLink(userEvent.getInviteeLink());*/
		userEventDTO.setInviteStatus(userEvent.getInviteStatus().name());
		if(userEvent.getRecordedLink() != null){
			String userEventRecordedLink = recordedLink + "fileName="+userEvent.getRecordedLink()+"&eventId="+userEvent.getEvent().getId()+"&userId="+userEvent.getUser().getId();
			userEventDTO.setRecordedLink(userEventRecordedLink);
		}
		userEventDTO.setUserEventID(userEvent.getId());
		userEventDTO.setUserEventType(userEvent.getUserEventType().name());
		return userEventDTO;
	}
	
	public static ThemeDTO getThemeDTO(Theme theme){
		ThemeDTO themeDTO = new ThemeDTO();
		themeDTO.setDescription(theme.getDescription());
		themeDTO.setPath(theme.getPath());
		themeDTO.setThemeId(theme.getId());
		themeDTO.setThemeName(theme.getThemeName());
		return themeDTO;
	}
	
	public static List<ThemeDTO> getThemeDTOList(List<Theme> themesList){
		List<ThemeDTO> themeDTOList = null;
		if(themesList != null){
			themeDTOList = new ArrayList<ThemeDTO>();
			for(Theme theme : themesList){
				themeDTOList.add(getThemeDTO(theme));
			}
		}
		return themeDTOList;
	}
}