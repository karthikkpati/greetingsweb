package com.solweaver.greetings.dto;

import javax.validation.constraints.NotNull;


public class ChangePasswordRequest extends BaseRequest{

	private Long userId;
	
	@NotNull(message="Password cannot be null")
	private String password;
	
	@NotNull(message="Confirm password cannot be null")
	private String confirmPassword;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
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
	
}
