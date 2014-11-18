package com.solweaver.greetings.service.impl;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dto.EventCreationRequest;
import com.solweaver.greetings.dto.EventCreationResponse;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.LoginRequest;
import com.solweaver.greetings.dto.LoginResponse;
import com.solweaver.greetings.dto.UserDTO;
import com.solweaver.greetings.dto.UserRegistrationRequest;
import com.solweaver.greetings.dto.UserRegistrationResponse;
import com.solweaver.greetings.model.Channel;
import com.solweaver.greetings.model.Gender;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserStatus;
import com.solweaver.greetings.service.IUserService;
import com.solweaver.greetings.utils.EntityDtoUtils;
import com.solweaver.greetings.utils.GenericUtils;

@Service
public class UserServiceImpl implements IUserService{

	@Autowired
	private UserDAO userDAO;

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
		
		user = userDAO.findExistingUserByEmail(userRegistrationRequest.getEmail());
		if(user != null){
			if(user.getUserStatus().equals(UserStatus.Active)){
				GenericUtils.buildErrorDetail(userRegistrationResponse, GenericEnum.DUPLICATE_USER);
				return userRegistrationResponse;
			}
		}else{
			user = new User();
		}
		user.setDateOfBirth(userRegistrationRequest.getDateOfBirth());
		user.setEmail(userRegistrationRequest.getEmail());
		user.setFirstName(userRegistrationRequest.getFirstName());
		user.setLastName(userRegistrationRequest.getLastName());
		user.setGender(gender);
		user.setRegisteredDeviceId(userRegistrationRequest.getDeviceId());
		user.setUserStatus(UserStatus.Active);
		user.setRegisteredChannel(Channel.valueOf(userRegistrationRequest.getChannel()));
		user.setPassword(userRegistrationRequest.getPassword());
		userDAO.makePersistent(user);
		
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

		user.setLastLoggedInChannel(Channel.valueOf(loginRequest.getChannel()));
		user.setLastLoggedInDeviceId(loginRequest.getDeviceId());
		user.setLastLoggedInTime(new Date());
		
		userDAO.merge(user);
		
		GenericUtils.buildErrorDetail(loginResponse, GenericEnum.Success);
		return loginResponse;
	}

	@Override
	@Transactional
	public User findUserById(Long userId) {
		return userDAO.findActiveUserById(userId);
	}

}
