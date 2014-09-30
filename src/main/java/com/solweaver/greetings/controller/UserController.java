package com.solweaver.greetings.controller;

import java.io.IOException;
import java.util.Date;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.solweaver.greetings.dto.LoginRequest;
import com.solweaver.greetings.dto.LoginResponse;
import com.solweaver.greetings.dto.UserRegistrationRequest;
import com.solweaver.greetings.dto.UserRegistrationResponse;
import com.solweaver.greetings.service.IUserService;

@Controller
public class UserController {

	@Autowired
	private IUserService userService;
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public @ResponseBody UserRegistrationResponse registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) throws IOException{
		return userService.createUserIfNotExists(userRegistrationRequest);
	}
	
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public @ResponseBody LoginResponse login(@Valid @RequestBody LoginRequest loginRequest) throws IOException{
		return userService.login(loginRequest);
	}

	@RequestMapping(value="/register1", method=RequestMethod.GET)
	public @ResponseBody UserRegistrationRequest registerUser1() throws IOException{
		UserRegistrationRequest userRegistrationRequest = new UserRegistrationRequest();
		userRegistrationRequest.setConfirmPassword("123");
		userRegistrationRequest.setDateOfBirth(new Date());
		userRegistrationRequest.setDeviceId("132");
		userRegistrationRequest.setEmail("abcd@abcd.com");
		userRegistrationRequest.setFirstName("firstName");
		userRegistrationRequest.setLastName("lastName");
		userRegistrationRequest.setGender("Male");
		userRegistrationRequest.setLastName("lastName");
		userRegistrationRequest.setPassword("test");
		return userRegistrationRequest;
	}
}
