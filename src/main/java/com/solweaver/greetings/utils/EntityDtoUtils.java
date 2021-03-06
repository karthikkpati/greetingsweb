package com.solweaver.greetings.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.solweaver.greetings.dto.CategoryDTO;
import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventDTO;
import com.solweaver.greetings.dto.NotificationTemplateDTO;
import com.solweaver.greetings.dto.ThemeDTO;
import com.solweaver.greetings.dto.UserDTO;
import com.solweaver.greetings.dto.UserEventDTO;
import com.solweaver.greetings.dto.UserNotificationDTO;
import com.solweaver.greetings.model.Category;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.EventStatus;
import com.solweaver.greetings.model.InviteStatus;
import com.solweaver.greetings.model.NotificationTemplate;
import com.solweaver.greetings.model.Theme;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.model.UserEventType;
import com.solweaver.greetings.model.UserNotification;

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
			if(user.getSocialAuthProvider() != null){
				userDTO.setSocialAuthProvider(user.getSocialAuthProvider().name().toUpperCase());
			}
			userDTO.setCountryCode(user.getCountryCode());
			userDTO.setPhoneNumber(user.getPhoneNumber());
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
			event.setRecipientMessage(eventCreationRequest.getRecipientMessage());
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

	public static List<EventDTO> getEventDTOList(List<Event> eventList, boolean isUserEvent, User user, String videoUrl) {
		List<EventDTO> eventDTOList = null;
		if(eventList != null && eventList.size() > 0){
			eventDTOList = new ArrayList<EventDTO>();
			for(Event event:eventList){
				EventDTO eventDTO = getEventDTO(event, isUserEvent, user, videoUrl);
				if(eventDTO != null){
					eventDTO.setEventId(event.getId());
					eventDTOList.add(eventDTO);
				}
			}
		}
		return eventDTOList;
	}
	
	public static EventDTO getEventDTO(Event event, boolean isUserEvent, User user, String context){
		EventDTO eventDTO = null;
		boolean addEvent = true;
		if(event.getRecipientUser() != null && event.getRecipientUser().getId().equals(user.getId())){
			if(!event.getEventStatus().equals(EventStatus.Completed)){
				addEvent = false;
			}
		}
		if(addEvent){
			eventDTO = new EventDTO();
			eventDTO.setCreatedByRecordedLink(event.getCreatedByRecordedLink());
			eventDTO.setDescription(event.getDescription());
			eventDTO.setEnableReminder(event.isEnableReminder());
			eventDTO.setEventDate(event.getEventDate());
			eventDTO.setEventName(event.getEventName());
			eventDTO.setRecipientMessage(event.getRecipientMessage());
			if(event.getEventStatus() != null){
				eventDTO.setEventStatus(event.getEventStatus().name());
			}
			
			if(isUserEvent){
				List<UserEvent> userEventList = event.getUserEventList();
				List<UserEventDTO> userEventDTOList = null;
				if(userEventList != null && userEventList.size() > 0){
					userEventDTOList = new ArrayList<UserEventDTO>();
					for(UserEvent userEvent : userEventList){
						UserEventDTO userEventDTO = getUserEventDTO(userEvent);
						userEventDTOList.add(userEventDTO);
						if(userEventDTO != null && userEventDTO.getUserDTO() != null){
							if(userEventDTO.getUserDTO().getUserId() !=null && userEventDTO.getUserDTO().getUserId().equals(user.getId())){
								eventDTO.setUserEventType(userEventDTO.getUserEventType());
							}
						}
					}
				}
				eventDTO.setUserEventDTOList(userEventDTOList);
			}
			eventDTO.setFromMessage(event.getFromMessage());
			eventDTO.setVideoSubmissionDate(event.getVideoSubmissionDate());
			
			Category eventCategory = event.getCategory();
			if(eventCategory != null){
				CategoryDTO categoryDTO = new CategoryDTO();
				categoryDTO.setCategoryDescription(eventCategory.getDescription());
				categoryDTO.setCategoryId(eventCategory.getId());
				categoryDTO.setCategoryName(eventCategory.getCategoryName());
				eventDTO.setCategoryDTO(categoryDTO);
			}
			
			if(context != null && event.getEventStatus().equals(EventStatus.Completed)){
				String videoUrl = context+ "/streamMp4Final.mp4" + "?eventId="+event.getId()+"&userId="+user.getId();
				String thumbNail = context + "/thumbNail.jpeg" + "?eventId="+event.getId()+"&userId="+user.getId()+"&fileName=thumbNail.jpg";
				eventDTO.setThumbNail(thumbNail);
				eventDTO.setVideoUrl(videoUrl);
			}
			
			UserDTO createdByUser = getUserDTO(event.getCreatedBy());
			eventDTO.setCreatedBy(createdByUser);
			
			UserDTO recipientUser = getUserDTO(event.getRecipientUser());
			eventDTO.setRecipientUser(recipientUser);
		}
		
		return eventDTO;
	}
	
	public static EventDTO getEventDTO(Event event, boolean isUserEvent, User user){
		return getEventDTO(event, isUserEvent, user, null);
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
		userEventDTO.setMessage(userEvent.getMessage());
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
		if(theme.getSubscriptionType() != null){
			themeDTO.setSubscriptionType(theme.getSubscriptionType().name());
		}
		return themeDTO;
	}

	public static CategoryDTO getCategoryDTO(Category category){
		CategoryDTO categoryDTO = new CategoryDTO();
		categoryDTO.setCategoryId(category.getId());
		categoryDTO.setCategoryName(category.getCategoryName());
		categoryDTO.setCategoryDescription(category.getDescription());
		return categoryDTO;
	}
	
	public static List<CategoryDTO> getCategoryDTOList(List<Category> categoryList){
		List<CategoryDTO> categoryDTOList = null;
		if(categoryList != null && categoryList.size() > 0){
			categoryDTOList = new ArrayList<CategoryDTO>();
			for(Category category : categoryList){
				categoryDTOList.add(getCategoryDTO(category));
			}
		}
		return categoryDTOList;
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

	public static List<UserNotificationDTO> getUserNotificationDTOList(
			List<UserNotification> userNotifications) {
		List<UserNotificationDTO> userNotificationDTOs = new ArrayList<UserNotificationDTO>();
		if(userNotifications != null){
			for(UserNotification userNotification : userNotifications){
				userNotificationDTOs.add(getUserNotificationDTO(userNotification));
			}
		}
		return userNotificationDTOs;
	}

	public static UserNotificationDTO getUserNotificationDTO(
			UserNotification userNotification) {
		UserNotificationDTO userNotificationDTO = new UserNotificationDTO();
		userNotificationDTO.setDeviceId(userNotification.getDeviceId());
		userNotificationDTO.setNotificationTemplateDTO(getNotificationTemplateDTO(userNotification.getNotificationTemplate()));
		userNotificationDTO.setUserNotificationId(userNotification.getId());
		return userNotificationDTO;
	}
	
	public static NotificationTemplateDTO getNotificationTemplateDTO(NotificationTemplate notificationTemplate){
		NotificationTemplateDTO notificationTemplateDTO = new NotificationTemplateDTO();
		notificationTemplateDTO.setContent(notificationTemplate.getContent());
		notificationTemplateDTO.setDescription(notificationTemplate.getDescription());
		notificationTemplateDTO.setNotificationTemplateId(notificationTemplate.getId());
		notificationTemplateDTO.setTemplateName(notificationTemplate.getTemplateName());
		return notificationTemplateDTO;
	}

	public static List<NotificationTemplateDTO> getNotificationTemplateDTOList(
			List<NotificationTemplate> notificationTemplateList) {
		List<NotificationTemplateDTO> notificationTemplateDTOs = new ArrayList<NotificationTemplateDTO>();
		if(notificationTemplateList != null){
			for(NotificationTemplate notificationTemplate : notificationTemplateList){
				notificationTemplateDTOs.add(getNotificationTemplateDTO(notificationTemplate));
			}
		}
		return notificationTemplateDTOs;
	}
	
}
