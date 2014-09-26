package com.solweaver.greetings.service;

import com.solweaver.greetings.dto.UserRegistrationRequest;
import com.solweaver.greetings.dto.UserRegistrationResponse;

public interface IUserService {

	UserRegistrationResponse createUserIfNotExists(UserRegistrationRequest userRegistrationRequest);
}
