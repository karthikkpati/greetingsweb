package com.solweaver.greetings.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.LoginActivity;
import com.solweaver.greetings.model.User;

@Repository
public class LoginActivityDAO extends BaseDAO<LoginActivity, Long > {
	
	public List<LoginActivity> findByUserDevice(User user, String deviceID){
		List<LoginActivity> loginActivityList = null;

		if(user != null){
			loginActivityList = findByCriteria(Restrictions.and(Restrictions.eq("loggedInUser", user), Restrictions.eq("deviceId", deviceID), Restrictions.eq("active", true)));
		}
		
		return loginActivityList;
	}

	public void logoutUserDevice(User user, String deviceId) {
		List<LoginActivity> loginActivityList = findByUserDevice(user, deviceId);
		if(loginActivityList != null && loginActivityList.size() > 0){
			for(LoginActivity loginActivity : loginActivityList){
				loginActivity.setActive(false);
				merge(loginActivity);
			}
		}
	}
	
	public List<LoginActivity> findAllLoggedInDevices(User user){
		List<LoginActivity> loginActivityList = null;
		if(user != null){
			loginActivityList = findByCriteria(Restrictions.and(Restrictions.eq("loggedInUser", user), Restrictions.eq("active", true)));
		}
		return loginActivityList;

	}
}
