package com.solweaver.greetings.dto;

import java.util.List;

public class GetNotificationTemplateResponse extends BaseResponse{

	private List<NotificationTemplateDTO> notificationTemplateDTOList;

	public List<NotificationTemplateDTO> getNotificationTemplateDTOList() {
		return notificationTemplateDTOList;
	}

	public void setNotificationTemplateDTOList(
			List<NotificationTemplateDTO> notificationTemplateDTOList) {
		this.notificationTemplateDTOList = notificationTemplateDTOList;
	}
	
}
