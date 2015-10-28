package com.solweaver.greetings.dto;

public class NotificationRequest extends BaseRequest{

	private Long userId;

	private String notificationName;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getNotificationName() {
		return notificationName;
	}

	public void setNotificationName(String notificationName) {
		this.notificationName = notificationName;
	}

}
