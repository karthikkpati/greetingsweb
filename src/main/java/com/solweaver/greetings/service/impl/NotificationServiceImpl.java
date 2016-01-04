package com.solweaver.greetings.service.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.EventDAO;
import com.solweaver.greetings.dao.LoginActivityDAO;
import com.solweaver.greetings.dao.NotificationTemplateDAO;
import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dao.UserEventDAO;
import com.solweaver.greetings.dao.UserNotificationDAO;
import com.solweaver.greetings.dto.DashboardNotificationDTO;
import com.solweaver.greetings.dto.DashboardNotificationRequest;
import com.solweaver.greetings.dto.DashboardNotificationResponse;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.GetNotificationTemplateRequest;
import com.solweaver.greetings.dto.GetNotificationTemplateResponse;
import com.solweaver.greetings.dto.NotificationRequest;
import com.solweaver.greetings.dto.NotificationResponse;
import com.solweaver.greetings.dto.NotificationTemplateDTO;
import com.solweaver.greetings.dto.UserNotificationDTO;
import com.solweaver.greetings.dto.UserNotificationRequest;
import com.solweaver.greetings.dto.UserNotificationResponse;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.EventStatus;
import com.solweaver.greetings.model.InviteStatus;
import com.solweaver.greetings.model.LoginActivity;
import com.solweaver.greetings.model.NotificationTemplate;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.model.UserEventType;
import com.solweaver.greetings.model.UserNotification;
import com.solweaver.greetings.service.INotificationService;
import com.solweaver.greetings.service.IUserService;
import com.solweaver.greetings.utils.EntityDtoUtils;
import com.solweaver.greetings.utils.GenericUtils;

@Service
public class NotificationServiceImpl implements INotificationService{

	@Autowired
	private IUserService userService;
	
	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private LoginActivityDAO loginActivityDAO;
	
	@Autowired
	private NotificationTemplateDAO notificationTemplateDAO;
	
	@Autowired
	private UserNotificationDAO userNotificationDAO;
	
	@Autowired
	private UserEventDAO userEventDAO;
	
	@Autowired
	private EventDAO eventDAO;
	
	@Override
	@Transactional
	public UserNotificationResponse fetchUserNotifications(UserNotificationRequest userNotificationRequest){
		UserNotificationResponse userNotificationResponse = new UserNotificationResponse();
		User user = userDAO.findActiveUserById(userNotificationRequest.getUserId());
		
		if(user == null){
			GenericUtils.buildErrorDetail(userNotificationResponse, GenericEnum.USER_DOESNT_EXIST);
			return userNotificationResponse;
		}
		
		List<UserNotification> userNotifications = userNotificationDAO.findByUser(user);
		List<UserNotificationDTO> userNotificationDTOs = EntityDtoUtils.getUserNotificationDTOList(userNotifications);
		userNotificationResponse.setUserNotificationDTOList(userNotificationDTOs);
		GenericUtils.buildErrorDetail(userNotificationResponse, GenericEnum.Success);
		return userNotificationResponse;
	}
	
	@Override
	@Transactional
	public NotificationResponse pushNotification(NotificationRequest notificationRequest){
		NotificationResponse notificationResponse = new NotificationResponse();
		
		User user = userDAO.findActiveUserById(notificationRequest.getUserId());
		
		if(user == null){
			GenericUtils.buildErrorDetail(notificationResponse, GenericEnum.USER_DOESNT_EXIST);
			return notificationResponse;
		}
		
		List<LoginActivity> loggedInDevices = loginActivityDAO.findAllLoggedInDevices(user);
	
		if(notificationRequest.getNotificationName() == null){
			GenericUtils.buildErrorDetail(notificationResponse, GenericEnum.NOTIFICATION_NAME_EMPTY);
			return notificationResponse;
		}
		
		NotificationTemplate notificationTemplate = notificationTemplateDAO.getNotificationTemplateByName(notificationRequest.getNotificationName());
		if(notificationTemplate == null){
			GenericUtils.buildErrorDetail(notificationResponse, GenericEnum.NOTIFICATION_TEMPLATE_DOESNT_EXIST);
			return notificationResponse;
		}
		StringBuffer notificationMessage = new StringBuffer("Notification : ");
		notificationMessage.append(notificationTemplate.getContent());
		notificationMessage.append(" \n sent to ");
		
		if(loggedInDevices != null && loggedInDevices.size() > 0){
			for(LoginActivity loginActivity : loggedInDevices){
				notificationMessage.append(loginActivity.getDeviceId());
				 notificationMessage.append(", " );
				//TODO - Call parse api and push notification to device
				 UserNotification userNotification = new UserNotification();
				 userNotification.setUser(loginActivity.getLoggedInUser());
				 userNotification.setDeviceId(loginActivity.getDeviceId());
				 userNotification.setNotificationTemplate(notificationTemplate);
				 userNotificationDAO.makePersistent(userNotification);
			}
			notificationMessage.substring(notificationMessage.length()-2, notificationMessage.length()-1);
		}
		
		notificationMessage.append("devices");
		GenericUtils.buildErrorDetail(notificationResponse, GenericEnum.Success, notificationMessage.toString());
		return notificationResponse;
	}

	@Override
	public GetNotificationTemplateResponse fetchNotificationTemplates(
			GetNotificationTemplateRequest getNotificationTemplateRequest) {
		GetNotificationTemplateResponse getNotificationTemplateResponse = new GetNotificationTemplateResponse();
		List<NotificationTemplate> notificationTemplateList = notificationTemplateDAO.getAllNotificationTemplates();
		List<NotificationTemplateDTO> notificationTemplateDTOList = EntityDtoUtils.getNotificationTemplateDTOList(notificationTemplateList);
		getNotificationTemplateResponse.setNotificationTemplateDTOList(notificationTemplateDTOList);
		GenericUtils.buildErrorDetail(getNotificationTemplateResponse, GenericEnum.Success);
		return getNotificationTemplateResponse;
	}
	
	@Override
	@Transactional
	public DashboardNotificationResponse fetchDashboardNotifications(
			DashboardNotificationRequest dashboardNotificationRequest){
		DashboardNotificationResponse dashboardNotificationResponse = new DashboardNotificationResponse();
		List<DashboardNotificationDTO> dashboardNotificationDTOList = new ArrayList<DashboardNotificationDTO>();
		/*List<DashboardNotificationDTO> recipientDashboardNotificationList = new ArrayList<DashboardNotificationDTO>();
		List<DashboardNotificationDTO> inviterDashboardNotificationList = new ArrayList<DashboardNotificationDTO>();
		List<DashboardNotificationDTO> inviteeDashboardNotificationList = new ArrayList<DashboardNotificationDTO>();
		
		dashboardNotificationResponse.setRecipientDashboardNotificationDTOList(recipientDashboardNotificationList);
		dashboardNotificationResponse.setInviteeDashboardNotificationDTOList(inviteeDashboardNotificationList);
		dashboardNotificationResponse.setInviterDashboardNotificationDTOList(inviterDashboardNotificationList);
		*/
		dashboardNotificationResponse.setDashboardNotificationDTOList(dashboardNotificationDTOList);
		User user = userDAO.findActiveUserById(dashboardNotificationRequest.getUserId());
		if(user != null){
			List<UserEvent> userEventList = userEventDAO.findByUserId(user.getId());
			if(userEventList != null ){
				for(UserEvent userEvent : userEventList){
					Calendar cal = Calendar.getInstance();
					Date currentDate = cal.getTime();
			        cal.add(Calendar.DATE, -7);
			        Date eventDateMinusSeven = cal.getTime();
					if(userEvent.getEvent().getEventDate() == null || (userEvent.getEvent().getEventDate().after(currentDate) && userEvent.getEvent().getEventDate().before(eventDateMinusSeven))){
						DashboardNotificationDTO dashboardNotificationDTO = new DashboardNotificationDTO();
						Event event = userEvent.getEvent();
						String description = "";
						dashboardNotificationDTO.setEventId(userEvent.getEvent().getId());
						dashboardNotificationDTO.setEventType(userEvent.getUserEventType().toString());
						dashboardNotificationDTO.setUserEventId(userEvent.getId());
						dashboardNotificationDTO.setEventDescription(userEvent.getEvent().getDescription());
						dashboardNotificationDTO.setEventDate(userEvent.getEvent().getEventDate());
						dashboardNotificationDTO.setRecipientDTO(EntityDtoUtils.getUserDTO(userEvent.getEvent().getRecipientUser()));
						if(userEvent.getUserEventType().equals(UserEventType.Invitee)){
							if(event.getEventStatus().equals(EventStatus.Completed)){
								description = "Greeting Ready";
							}else if(userEvent.getInviteStatus().equals(InviteStatus.Uploaded)){
								description = "Video Uploaded";
							}else if(userEvent.getInviteStatus().equals(InviteStatus.Pending)){
								description = "You are invited to the event. Please click Accept to accept the invitation or Reject to decline the invitation";
							}else{
								description = "Please upload the Video";
							}
						}else if(userEvent.getUserEventType().equals(UserEventType.RECIPIENT) || (event.getRecipientUser() != null && event.getRecipientUser().equals(user))){
							if(event.getEventStatus().equals(EventStatus.Completed)){
								description = "You Received a Greeting";
							}
						}else if(userEvent.getUserEventType().equals(UserEventType.Inviter) || (event.getCreatedBy() != null && event.getCreatedBy().equals(user))){
							if(event.getEventStatus().equals(EventStatus.Completed)){
								description = "Greeting Sent";
							}else{
								List<UserEvent> eventUserEventList = event.getUserEventList();
								StringBuffer users = new StringBuffer("");
								for(UserEvent eventUserEvent : eventUserEventList){
									if(eventUserEvent.getInviteStatus().equals(InviteStatus.Uploaded)){
										users.append(userEvent.getUser().getFirstName()).append(" ").append(userEvent.getUser().getLastName()).append(",");
									}
								}
								if(users.length() > 0){
									description = users.append(" uploaded").toString();
								}
							}
						}
						if(!description.isEmpty()){
							dashboardNotificationDTOList.add(dashboardNotificationDTO);
						}
						dashboardNotificationDTO.setDescription(description);
					}
			
				}
			}
		}
		return dashboardNotificationResponse;
	}

}
