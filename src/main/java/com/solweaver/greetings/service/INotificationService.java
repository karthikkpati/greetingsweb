package com.solweaver.greetings.service;

import com.solweaver.greetings.dto.NotificationRequest;
import com.solweaver.greetings.dto.NotificationResponse;


public interface INotificationService {

	public NotificationResponse pushNotification(
			NotificationRequest notificationRequest);
}
