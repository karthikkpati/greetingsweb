package com.solweaver.greetings.dto;

public class UpdateUserResponse extends BaseResponse{

	private UserDTO userDTO;

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}
	
}
