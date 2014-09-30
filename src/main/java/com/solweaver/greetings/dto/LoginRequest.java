package com.solweaver.greetings.dto;

import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Email;


public class LoginRequest extends BaseRequest{

	@NotNull(message="EMail cannot be null")
	@Email(message="Invalid Email Format")
	private String email;
	
	@NotNull(message="Password cannot be null")
	private String password;

	private String deviceId;

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

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
