package com.solweaver.greetings.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserNotification;

@Repository
public class UserNotificationDAO extends BaseDAO<UserNotification, Long > {

	public List<UserNotification> findByUser(User user){
		List<UserNotification> userNotificationList = null;
		userNotificationList = findByCriteria(Restrictions.eq("user.id", user.getId()));
		return userNotificationList;
	}
}
