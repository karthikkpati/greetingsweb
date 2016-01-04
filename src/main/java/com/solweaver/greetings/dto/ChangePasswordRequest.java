package com.solweaver.greetings.dto;

import javax.validation.constraints.NotNull;


public class ChangePasswordRequest extends BaseRequest{

	private Long userId;
	
	@NotNull(message="Current Password cannot be null")
	private String currentPassword;
	
	@NotNull(message="New Password cannot be null")
	private String newPassword;
	
	@NotNull(message="Confirm password cannot be null")
	private String confirmPassword;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getCurrentPassword() {
		return currentPassword;
	}

	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}

	public String getNewPassword() {
		return newPassword;
	}

	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	
}
