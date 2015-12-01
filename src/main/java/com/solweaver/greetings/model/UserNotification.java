package com.solweaver.greetings.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class UserNotification extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
	
	@ManyToOne
	@JoinColumn(name="notification_template_id")
	private NotificationTemplate notificationTemplate;
	
	@ManyToOne
	@JoinColumn(name="user_id")
	private User user;
	
	private String deviceId;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public NotificationTemplate getNotificationTemplate() {
		return notificationTemplate;
	}

	public void setNotificationTemplate(NotificationTemplate notificationTemplate) {
		this.notificationTemplate = notificationTemplate;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
