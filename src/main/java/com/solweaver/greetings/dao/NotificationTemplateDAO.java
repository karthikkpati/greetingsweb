package com.solweaver.greetings.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.NotificationTemplate;

@Repository
public class NotificationTemplateDAO extends BaseDAO<NotificationTemplate, Long > {

	public NotificationTemplate getNotificationTemplateByName(String templateName){
		NotificationTemplate notificationTemplate = null;
		List<NotificationTemplate> notificationTemplateList = null;
		notificationTemplateList = findByCriteria(Restrictions.and(Restrictions.eq("templateName", templateName), Restrictions.eq("active", true)));
		if(notificationTemplateList != null && notificationTemplateList.size() > 0){
			notificationTemplate = notificationTemplateList.get(0);
		}
		return notificationTemplate;
	}
}
