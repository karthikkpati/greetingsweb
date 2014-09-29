package com.solweaver.greetings.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dto.GenericEnum;
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
		user.setRegisteredChannelId(Channel.valueOf(userRegistrationRequest.getChannel()));
		userDAO.makePersistent(user);
		
		userRegistrationResponse.setUserDTO(EntityDtoUtils.getUserDTO(user));
		
		GenericUtils.buildErrorDetail(userRegistrationResponse, GenericEnum.Success);
		return userRegistrationResponse;
	}
	
	/*@Override
	@Transactional
	public UserRe createUserIfNotExists(UserDTO userDTO, UserMetaDataDTO userMetaDataDTO) {
		UserResponse userResponse = new UserResponse();
		User user = null;
		if(userDTO != null){
			if(userDTO.getUserId() != null){
				user = userDAO.findById(userDTO.getUserId(), false);
				if(user == null){
					GenericUtils.buildErrorDetail(userResponse, GenericEnum.INVALID_USER);
					return userResponse;
				}
			}else if(userDTO.getEmailId() != null){
				List<UserStatus> userStatusList = new ArrayList<UserStatus>();
				userStatusList.add(UserStatus.Active);
				userStatusList.add(UserStatus.InActive);
				List<User> userList = userDAO.findByEmail(userDTO.getEmailId(), userStatusList);
				if(userList != null && userList.size() > 0){
					if(userList.size() == 1){
						user = (User) userList.get(0);
						userDTO.setUserId(user.getId());
						if(user.getUserStatus().equals(UserStatus.InActive)){
							user.setUserStatus(UserStatus.Active);
							userDAO.merge(user);
							UserMetaData userMetaData = GenericUtils.convertToUserMetaDataEntity(userMetaDataDTO);
							userMetaData.setUser(user);
							userMetaDataDAO.makePersistent(userMetaData);
							user.setUserMetaData(userMetaData);
						}
					}else{
						GenericUtils.buildErrorDetail(userResponse, GenericEnum.DUPLICATE_USER);
						return userResponse;
					}
				}
			}
			if(user == null){
				try{
					user = GenericUtils.convertToUserEntity(userDTO);
					user.setUserStatus(UserStatus.Active);
					userDAO.makePersistent(user);
					userDTO.setUserId(user.getId());
					UserMetaData userMetaData = GenericUtils.convertToUserMetaDataEntity(userMetaDataDTO);
					userMetaData.setUser(user);
					userMetaDataDAO.makePersistent(userMetaData);
				}catch(Exception exception){
					exception.printStackTrace();
					GenericUtils.buildErrorDetail(userResponse, GenericEnum.DB_EXCEPTION);
					return userResponse;
				}
			}else{
				UserMetaData userMetaData = user.getUserMetaData();
				userMetaData.setLastLoggedInDeviceId(userMetaDataDTO.getLastLoggedInDeviceId());
				userMetaData.setLastLoggedInTime(userMetaData.getLastLoggedInTime());
				userMetaDataDAO.merge(userMetaData);
			}
			GenericUtils.buildErrorDetail(userResponse, GenericEnum.Success);
		}
		return userResponse;
	}*/
	
	
}
