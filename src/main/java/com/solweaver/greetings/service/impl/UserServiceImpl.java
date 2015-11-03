package com.solweaver.greetings.service.impl;

import java.util.Date;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.LoginActivityDAO;
import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dto.BaseResponse;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.LoginRequest;
import com.solweaver.greetings.dto.LoginResponse;
import com.solweaver.greetings.dto.LogoutRequest;
import com.solweaver.greetings.dto.UserDTO;
import com.solweaver.greetings.dto.UserRegistrationRequest;
import com.solweaver.greetings.dto.UserRegistrationResponse;
import com.solweaver.greetings.model.Channel;
import com.solweaver.greetings.model.Gender;
import com.solweaver.greetings.model.LoginActivity;
import com.solweaver.greetings.model.SocialAuthProvider;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserStatus;
import com.solweaver.greetings.service.IUserService;
import com.solweaver.greetings.utils.EntityDtoUtils;
import com.solweaver.greetings.utils.GenericUtils;

@Service
public class UserServiceImpl implements IUserService{

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private LoginActivityDAO loginActivityDAO;
	
	@Override
	@Transactional
	public UserRegistrationResponse createUserIfNotExists(
			UserRegistrationRequest userRegistrationRequest) {
		UserRegistrationResponse userRegistrationResponse = new UserRegistrationResponse();
		User user = null;
		if(!userRegistrationRequest.getPassword().equals(userRegistrationRequest.getConfirmPassword())){
			GenericUtils.buildErrorDetail(userRegistrationResponse, GenericEnum.CONFIRM_PASSWORD);
			return userRegistrationResponse;
		}
		Gender gender = null;
		try{
			gender = Gender.valueOf(userRegistrationRequest.getGender());
		}catch(Exception exception){
			GenericUtils.buildErrorDetail(userRegistrationResponse, GenericEnum.INVALID_GENDER);
			return userRegistrationResponse;
		}
		
		SocialAuthProvider socialAuthProvider = null;
		try{
			if(!StringUtils.isEmpty(userRegistrationRequest.getSocialAuthProvider()))
			socialAuthProvider = SocialAuthProvider.valueOf(userRegistrationRequest.getSocialAuthProvider());
		}catch(Exception exception){
			GenericUtils.buildErrorDetail(userRegistrationResponse, GenericEnum.INVALID_SOCIAL_AUTH_PROVIDER);
			return userRegistrationResponse;
		}
		user = userDAO.findExistingUserByEmail(userRegistrationRequest.getEmail());
		if(user != null){
			if((socialAuthProvider == null && user.getUserStatus().equals(UserStatus.Active))){
				GenericUtils.buildErrorDetail(userRegistrationResponse, GenericEnum.DUPLICATE_USER);
				return userRegistrationResponse;
			}
			
		}else{
			user = new User();
		}
		Date dateOfBirth = userRegistrationRequest.getDateOfBirth();
		String email = userRegistrationRequest.getEmail();
		String firstName = userRegistrationRequest.getFirstName();
		String lastName = userRegistrationRequest.getLastName();
		String deviceId = userRegistrationRequest.getDeviceId();
		Channel channel = Channel.valueOf(userRegistrationRequest.getChannel());
		String password = userRegistrationRequest.getPassword();
		String countryCode = userRegistrationRequest.getCountryCode();
		String phoneNumber = userRegistrationRequest.getPhoneNumber();
		
		if(dateOfBirth != null){
			user.setDateOfBirth(dateOfBirth);
		}
		if(email != null){
			user.setEmail(email);
		}
		if(firstName != null){
			user.setFirstName(firstName);
		}
		if(lastName != null){
			user.setLastName(lastName);
		}
		if(gender != null){
			user.setGender(gender);
		}
		if(deviceId != null && user.getRegisteredDeviceId() == null){
			user.setRegisteredDeviceId(deviceId);
		}
		if(channel != null && user.getRegisteredChannel() == null){
			user.setRegisteredChannel(channel);
		}
		if(password != null){
			user.setPassword(password);
		}
		if(socialAuthProvider != null && user.getSocialAuthProvider() == null){
			user.setSocialAuthProvider(socialAuthProvider);
		}
		if(countryCode != null){
			user.setCountryCode(countryCode);
		}
		if(phoneNumber != null){
			user.setPhoneNumber(phoneNumber);
		}
		
		user.setUserStatus(UserStatus.Active);
		userDAO.makePersistent(user);
		
		LoginActivity loginActivity = new LoginActivity();
		loginActivity.setLoggedInUser(user);
		loginActivity.setLoginTime(new Date());
		loginActivity.setDeviceId(userRegistrationRequest.getDeviceId());
		loginActivity.setActive(true);
		
		loginActivityDAO.makePersistent(loginActivity);
		
		userRegistrationResponse.setUserDTO(EntityDtoUtils.getUserDTO(user));
		
		GenericUtils.buildErrorDetail(userRegistrationResponse, GenericEnum.Success);
		return userRegistrationResponse;
	}
	
	@Override
	@Transactional
	public LoginResponse login(LoginRequest loginRequest){
		LoginResponse loginResponse = new LoginResponse();
		User user = userDAO.findActiveUserByEmail(loginRequest.getEmail());
		if(user == null){
			GenericUtils.buildErrorDetail(loginResponse, GenericEnum.USER_DOESNT_EXIST);
			return loginResponse;
		}
		
		if(!user.getPassword().equals(loginRequest.getPassword())){
			GenericUtils.buildErrorDetail(loginResponse, GenericEnum.INVALID_USERNAME_PASSWORD);
			return loginResponse;
		}
		UserDTO userDTO = EntityDtoUtils.getUserDTO(user);
		loginResponse.setUserDTO(userDTO);

		LoginActivity loginActivity = new LoginActivity();
		loginActivity.setLoggedInUser(user);
		loginActivity.setLoginTime(new Date());
		loginActivity.setDeviceId(loginRequest.getDeviceId());
		loginActivity.setActive(true);
		
		loginActivityDAO.makePersistent(loginActivity);
		userDAO.merge(user);
		
		GenericUtils.buildErrorDetail(loginResponse, GenericEnum.Success);
		return loginResponse;
	}

	@Override
	@Transactional
	public User findUserById(Long userId) {
		return userDAO.findActiveUserById(userId);
	}

	@Override
	@Transactional
	public BaseResponse logout(LogoutRequest logoutRequest){
		BaseResponse logoutResponse = new BaseResponse();
		User user = userDAO.findActiveUserByEmail(logoutRequest.getEmail());
		if(user == null){
			GenericUtils.buildErrorDetail(logoutResponse, GenericEnum.USER_DOESNT_EXIST);
			return logoutResponse;
		}
		
		loginActivityDAO.logoutUserDevice(user, logoutRequest.getDeviceId());
			
		GenericUtils.buildErrorDetail(logoutResponse, GenericEnum.Success);
		return logoutResponse;
	}

}
