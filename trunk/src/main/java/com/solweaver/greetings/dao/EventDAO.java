package com.solweaver.greetings.dao;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.EventStatus;
import com.solweaver.greetings.model.UserEvent;

@Repository
public class EventDAO extends BaseDAO<Event, Long > {

	public List<Event> findEventsByUserId(Long userId, Long eventId, boolean isUserEvent, EventStatus eventStatus) {

		List<Event> eventList = null;

		Criteria eventCriteria = getSession().createCriteria(Event.class);

		if(eventStatus != null){
			eventCriteria.add(Restrictions.eq("eventStatus", eventStatus));
		}
		
		if(eventId != null){
			eventCriteria.add(Restrictions.eq("id", eventId));
		}
		
		eventCriteria.createCriteria("userEventList")
				.add(Restrictions.eq("user.id", userId));
		
		if(isUserEvent){
			eventCriteria.setFetchMode("userEventList", FetchMode.JOIN);
		}
			
		eventList = eventCriteria.list();
		
		return eventList;
	}

	public Event findByEventAndUserId(Long eventId, Long userId) {
		Event event = null;

		List<UserEvent> userEventList = null;
		Criteria eventCriteria = getSession().createCriteria(UserEvent.class);

		if(eventId != null){
			eventCriteria.add(Restrictions.eq("event.id", eventId));
		}
		
		if(userId != null){
			eventCriteria.add(Restrictions.eq("user.id", userId));
		}
		
		userEventList = eventCriteria.list();
		
		if(userEventList != null && userEventList.size()>0){
			event = userEventList.get(0).getEvent();
		}
		return event;
	}

}
