package com.solweaver.greetings.utils;

import com.solweaver.greetings.dto.UserDTO;
import com.solweaver.greetings.model.User;

public class EntityDtoUtils {
	
	public static UserDTO getUserDTO(User user){
		UserDTO userDTO = new UserDTO();
		if(user.getDateOfBirth() != null){
			userDTO.setDateOfBirth(user.getDateOfBirth().toString());
		}
		userDTO.setEmailId(user.getEmail());
		userDTO.setFirstName(user.getFirstName());
		userDTO.setLastName(user.getLastName());
		userDTO.setGender(user.getGender().name());
		userDTO.setUserId(user.getId());
		userDTO.setUserStatus(user.getUserStatus().name());
		return userDTO;
	}

}
