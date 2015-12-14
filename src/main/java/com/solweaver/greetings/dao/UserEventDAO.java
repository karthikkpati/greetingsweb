package com.solweaver.greetings.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.UserEvent;

@Repository
public class UserEventDAO extends BaseDAO<UserEvent, Long > {

	public UserEvent findByUserAndEvent(Long userId, Long eventId){
		UserEvent userEvent = null;
		List<UserEvent> userEventList = null;
		userEventList = findByCriteria(Restrictions.and(Restrictions.eq("event.id", eventId), Restrictions.eq("user.id", userId)));
		if(userEventList != null && userEventList.size() > 0){
			userEvent = userEventList.get(0);
		}
		return userEvent;
	}
	
	public List<UserEvent> findByUserId(Long userId){
		List<UserEvent> userEventList = null;
		userEventList = findByCriteria("creationTime", "desc", Restrictions.eq("user.id", userId));
		return userEventList;
	}
}
