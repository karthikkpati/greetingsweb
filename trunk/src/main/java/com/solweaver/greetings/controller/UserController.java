package com.solweaver.greetings.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.LoginRequest;
import com.solweaver.greetings.dto.LoginResponse;
import com.solweaver.greetings.dto.UserRegistrationRequest;
import com.solweaver.greetings.dto.UserRegistrationResponse;
import com.solweaver.greetings.model.GenericConstants;
import com.solweaver.greetings.service.IUserService;
import com.solweaver.greetings.service.IVelocityService;
import com.solweaver.greetings.utils.GenericUtils;

@Controller
public class UserController {

	@Autowired
	private IUserService userService;
	
	@Autowired
	private IVelocityService velocityService;
	
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public @ResponseBody UserRegistrationResponse registerUser(@Valid @RequestBody UserRegistrationRequest userRegistrationRequest) throws IOException{
		UserRegistrationResponse userRegistrationResponse = userService.createUserIfNotExists(userRegistrationRequest);
		
		if(GenericUtils.isSuccess(userRegistrationResponse)){
			Map<String,Object> emailMap = new HashMap<String, Object>();
			emailMap.put(GenericConstants.FIRST_NAME, userRegistrationRequest.getFirstName());
			emailMap.put(GenericConstants.LAST_NAME, userRegistrationRequest.getLastName());
			emailMap.put(GenericConstants.USER_NAME, userRegistrationRequest.getEmail());
			try {
				List<String> emailList = new ArrayList<String>();
				velocityService.sendEmail(emailMap, "Registration", emailList);
			} catch (Exception e) {
				e.printStackTrace();
				GenericUtils.buildErrorDetail(userRegistrationResponse, GenericEnum.Success, "Unable to send Confirmation email");
			}
		}
		return userRegistrationResponse;
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
