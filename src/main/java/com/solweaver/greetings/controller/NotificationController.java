package com.solweaver.greetings.controller;

import java.io.IOException;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.solweaver.greetings.dto.NotificationRequest;
import com.solweaver.greetings.dto.NotificationResponse;
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
}
