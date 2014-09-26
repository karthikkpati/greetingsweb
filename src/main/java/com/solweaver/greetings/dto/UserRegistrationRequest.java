package com.solweaver.greetings.dto;

import java.util.Date;

public class UserRegistrationRequest extends BaseRequest{

	private String email;
	
	private String password;
	
	private String confirmPassword;
	
	private Date dateOfBirth;
	
	private String deviceId;
	
	private String gender;
	
	private String firstName;
	
	private String lastName;
	
	private String registeredChannelId;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getRegisteredChannelId() {
		return registeredChannelId;
	}

	public void setRegisteredChannelId(String registeredChannelId) {
		this.registeredChannelId = registeredChannelId;
	}

}