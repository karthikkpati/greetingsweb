package com.solweaver.greetings.service;

import com.solweaver.greetings.dto.DashboardNotificationRequest;
import com.solweaver.greetings.dto.DashboardNotificationResponse;
import com.solweaver.greetings.dto.GetNotificationTemplateRequest;
import com.solweaver.greetings.dto.GetNotificationTemplateResponse;
import com.solweaver.greetings.dto.NotificationRequest;
import com.solweaver.greetings.dto.NotificationResponse;
import com.solweaver.greetings.dto.UserNotificationRequest;
import com.solweaver.greetings.dto.UserNotificationResponse;


public interface INotificationService {

	public NotificationResponse pushNotification(
			NotificationRequest notificationRequest);

	public UserNotificationResponse fetchUserNotifications(
			UserNotificationRequest userNotificationRequest);

	public GetNotificationTemplateResponse fetchNotificationTemplates(
			GetNotificationTemplateRequest getNotificationTemplateRequest);

	public DashboardNotificationResponse fetchDashboardNotifications(
			DashboardNotificationRequest dashboardNotificationRequest);
}
