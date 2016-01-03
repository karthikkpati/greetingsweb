package com.solweaver.greetings.dao;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.DateUtils;
import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.EventStatus;
import com.solweaver.greetings.model.InviteStatus;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.model.UserEventType;

@Repository
public class EventDAO extends BaseDAO<Event, Long> {

	/*
	 * public List<Event> findEventsByUserId(Long userId, Long eventId, boolean
	 * isUserEvent, EventStatus eventStatus) {
	 * 
	 * }
	 */

	public List<Event> findEventsByUserId(Long userId, Long eventId,
			boolean isUserEvent, EventStatus eventStatus,
			InviteStatus inviteeStatus, UserEventType userEventType) {
		List<Event> eventList = null;

		Criteria eventCriteria = getSession().createCriteria(Event.class);

		if (eventStatus != null) {
			eventCriteria.add(Restrictions.eq("eventStatus", eventStatus));
		}

		eventCriteria.add(Restrictions.ne("eventStatus", EventStatus.Deleted));
		
		if (eventId != null) {
			eventCriteria.add(Restrictions.eq("id", eventId));
		}

		Criteria userEventCriteria = eventCriteria
				.createCriteria("userEventList");

		userEventCriteria.add(Restrictions.eq("user.id", userId));

		if (inviteeStatus != null) {
			userEventCriteria.add(Restrictions
					.eq("inviteStatus", inviteeStatus));
		}

		if (userEventType != null) {
			userEventCriteria.add(Restrictions.eq("userEventType",
					userEventType));
		}

		if (isUserEvent) {
			eventCriteria.setFetchMode("userEventList", FetchMode.JOIN);
		}

		eventList = eventCriteria.list();

		return eventList;
	}

	public Event findEventById(Long eventId) {

		List<Event> eventList = null;
		Event event = null;

		Criteria eventCriteria = getSession().createCriteria(Event.class);

		EventStatus eventStatus = EventStatus.Deleted;

		if (eventStatus != null) {
			eventCriteria.add(Restrictions.ne("eventStatus", eventStatus));
		}

		if (eventId != null) {
			eventCriteria.add(Restrictions.eq("id", eventId));
		}

		eventList = eventCriteria.list();

		if (eventList != null && eventList.size() > 0) {
			event = eventList.get(0);
		}

		return event;
	}

	public Event findByEventAndUserId(Long eventId, Long userId) {
		Event event = null;

		List<UserEvent> userEventList = null;
		Criteria eventCriteria = getSession().createCriteria(UserEvent.class);

		if (eventId != null) {
			eventCriteria.add(Restrictions.eq("event.id", eventId));
		}

		if (userId != null) {
			eventCriteria.add(Restrictions.eq("user.id", userId));
		}

		userEventList = eventCriteria.list();

		if (userEventList != null && userEventList.size() > 0) {
			event = userEventList.get(0).getEvent();
		}
		return event;
	}

	public List<String> getInviteesEmail(Long eventId) {
		Criteria eventCriteria = getSession().createCriteria(UserEvent.class);
		return null;
	}

	public List<Event> findEventsByRecipientUserId(Long userId) {
		Criteria eventCriteria = getSession().createCriteria(Event.class);

		if (userId != null) {
			eventCriteria.add(Restrictions.eq("recipientUser.id", userId));
		}

		return eventCriteria.list();
	}

	public List<Event> findEventsToSend() {
		Date date = new Date();
		Date today = DateUtils.truncate(date, Calendar.DAY_OF_MONTH);
		Date tommorrow = DateUtils.addDays(today, 1); 
		Criteria eventCriteria = getSession().createCriteria(Event.class);
		eventCriteria.add(Restrictions.ge("eventDate", today)); 
		eventCriteria.add(Restrictions.lt("eventDate", tommorrow));
		return (List<Event>) eventCriteria.list();		
	}
	
	public List<Event> findEventsByUserId(Long userId) {
		Criteria eventCriteria = getSession().createCriteria(Event.class);
		eventCriteria.createAlias("event.userEventList", "userEventList");
		if (userId != null) {
			eventCriteria.add(Restrictions.eq("userEventList.user.id", userId));
		}

		return eventCriteria.list();
	}

}
