package com.solweaver.greetings.dto;


public class UserNotificationDTO {

	private Long userNotificationId;

	private NotificationTemplateDTO notificationTemplateDTO;
	
	private UserDTO userDTO;
	
	private String deviceId;

	public Long getUserNotificationId() {
		return userNotificationId;
	}

	public void setUserNotificationId(Long userNotificationId) {
		this.userNotificationId = userNotificationId;
	}

	public NotificationTemplateDTO getNotificationTemplateDTO() {
		return notificationTemplateDTO;
	}

	public void setNotificationTemplateDTO(
			NotificationTemplateDTO notificationTemplateDTO) {
		this.notificationTemplateDTO = notificationTemplateDTO;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}
	
}
