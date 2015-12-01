package com.solweaver.greetings.dto;

public class NotificationTemplateDTO {

	private Long notificationTemplateId;
	
	private String templateName;

	private String content;
	
	private String description;

	public Long getNotificationTemplateId() {
		return notificationTemplateId;
	}

	public void setNotificationTemplateId(Long notificationTemplateId) {
		this.notificationTemplateId = notificationTemplateId;
	}

	public String getTemplateName() {
		return templateName;
	}

	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
}
