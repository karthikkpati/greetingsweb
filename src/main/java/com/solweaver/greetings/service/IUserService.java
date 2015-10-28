package com.solweaver.greetings.service;

import com.solweaver.greetings.dto.BaseResponse;
import com.solweaver.greetings.dto.LoginRequest;
import com.solweaver.greetings.dto.LoginResponse;
import com.solweaver.greetings.dto.UserRegistrationRequest;
import com.solweaver.greetings.dto.UserRegistrationResponse;
import com.solweaver.greetings.model.User;

public interface IUserService {

	public UserRegistrationResponse createUserIfNotExists(UserRegistrationRequest userRegistrationRequest);

	public LoginResponse login(LoginRequest loginRequest);

	public User findUserById(Long userId);

	public BaseResponse logout(LoginRequest loginRequest);
	
}
