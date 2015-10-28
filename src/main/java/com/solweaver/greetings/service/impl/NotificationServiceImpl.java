package com.solweaver.greetings.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.LoginActivityDAO;
import com.solweaver.greetings.dao.NotificationTemplateDAO;
import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.NotificationRequest;
import com.solweaver.greetings.dto.NotificationResponse;
import com.solweaver.greetings.model.LoginActivity;
import com.solweaver.greetings.model.NotificationTemplate;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.service.INotificationService;
import com.solweaver.greetings.service.IUserService;
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
			}
		}
				
		notificationMessage.append("devices");
		GenericUtils.buildErrorDetail(notificationResponse, GenericEnum.Success, notificationMessage.toString());
		return notificationResponse;
	}

}
