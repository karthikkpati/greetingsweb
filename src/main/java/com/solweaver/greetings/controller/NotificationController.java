package com.solweaver.greetings.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.solweaver.greetings.dto.DashboardNotificationRequest;
import com.solweaver.greetings.dto.DashboardNotificationResponse;
import com.solweaver.greetings.dto.GetNotificationTemplateRequest;
import com.solweaver.greetings.dto.GetNotificationTemplateResponse;
import com.solweaver.greetings.dto.NotificationRequest;
import com.solweaver.greetings.dto.NotificationResponse;
import com.solweaver.greetings.dto.UserNotificationRequest;
import com.solweaver.greetings.dto.UserNotificationResponse;
import com.solweaver.greetings.service.INotificationService;

@Controller
public class NotificationController {

	@Autowired
	private INotificationService notificationService;

	@RequestMapping(value = "/notification", method = RequestMethod.POST)
	public @ResponseBody
	NotificationResponse pushNotification(
			@Valid @RequestBody NotificationRequest notificationRequest)
			throws IOException {
		return notificationService.pushNotification(notificationRequest);
	}
	
	@RequestMapping(value = "/usernotification", method = RequestMethod.POST)
	public @ResponseBody
	UserNotificationResponse userNotifications(
			@Valid @RequestBody UserNotificationRequest userNotificationRequest)
			throws IOException {
		return notificationService.fetchUserNotifications(userNotificationRequest);
	}
	
	@RequestMapping(value = "/notificationtemplates", method = RequestMethod.POST)
	public @ResponseBody
	GetNotificationTemplateResponse notificationTemplates(
			@Valid @RequestBody GetNotificationTemplateRequest getNotificationTemplateRequest)
			throws IOException {
		return notificationService.fetchNotificationTemplates(getNotificationTemplateRequest);
	}
	
	@RequestMapping(value = "/dashboard/notification", method = RequestMethod.POST)
	public @ResponseBody
	DashboardNotificationResponse dashboardNotifications(
			@Valid @RequestBody DashboardNotificationRequest dashboardNotificationRequest)
			throws IOException {
		return notificationService.fetchDashboardNotifications(dashboardNotificationRequest);
	}
}
