package com.solweaver.greetings.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserStatus;

@Repository
public class UserDAO extends BaseDAO<User, Long > {

	public List<User> findById(Long userId, List<UserStatus> userStatusList) {
		List<User> userList = null;

		if(userId != null){
			userList = findByCriteria(Restrictions.and(Restrictions.eq("id", userId), Restrictions.in("userStatus", userStatusList)));
		}
		
		return userList;
	}
	
	public List<User> findByEmail(String emailId, List<UserStatus> userStatusList) {
		List<User> userList = null;

		if(emailId != null){
			if(userStatusList != null){
				userList = findByCriteria(Restrictions.and(Restrictions.eq("email", emailId), Restrictions.in("userStatus", userStatusList)));
			}else{
				userList = findByCriteria(Restrictions.and(Restrictions.eq("email", emailId), Restrictions.in("userStatus", userStatusList)));
			}
			
		}
		
		return userList;
	}
	
	public List<User> findByEmailOrId(Long userId, String emailId, List<UserStatus> userStatusList) {
		List<User> userList = null;

		Criterion userCriterion = null;
		userCriterion = Restrictions.in("userStatus", userStatusList);
		
		if(userId != null || emailId != null){
			if(userId != null){
				userCriterion = Restrictions.and(Restrictions.eq("id", userId), userCriterion);
			}
			if(emailId != null){
				userCriterion = Restrictions.and(Restrictions.and(Restrictions.eq("email", emailId), userCriterion));
			}
			userList = findByCriteria(userCriterion);
		}
		return userList;
	}

	public User findExistingUserByEmail(String emailId) {
		User user = null;
		List<UserStatus> userStatusList = new ArrayList<UserStatus>();
		userStatusList.add(UserStatus.Active);
		userStatusList.add(UserStatus.InActive);
		List<User> userList = findByEmail(emailId, userStatusList);
		if(userList != null && !userList.isEmpty()){
			user = userList.get(0);
		}
		return user;
	}

	public User findActiveUserByEmail(String emailId){
		User user = null;
		List<UserStatus> userStatusList = new ArrayList<UserStatus>();
		userStatusList.add(UserStatus.Active);
		List<User> userList = findByEmail(emailId, userStatusList);
		if(userList != null && !userList.isEmpty()){
			user = userList.get(0);
		}
		return user;
	}

	public User findActiveUserById(Long userId) {
		User user = null;
		List<UserStatus> userStatusList = new ArrayList<UserStatus>();
		userStatusList.add(UserStatus.Active);
		List<User> userList = findById(userId, userStatusList);
		if(userList != null && !userList.isEmpty()){
			user = userList.get(0);
		}
		return user;
	}
}
