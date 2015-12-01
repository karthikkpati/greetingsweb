package com.solweaver.greetings.dto;

import java.util.List;

public class UserNotificationResponse extends BaseResponse{

	private List<UserNotificationDTO> userNotificationDTOList;

	public List<UserNotificationDTO> getUserNotificationDTOList() {
		return userNotificationDTOList;
	}

	public void setUserNotificationDTOList(
			List<UserNotificationDTO> userNotificationDTOList) {
		this.userNotificationDTOList = userNotificationDTOList;
	}
	
}
