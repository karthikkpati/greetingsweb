package com.solweaver.greetings.dto;

import javax.validation.constraints.NotNull;


public class GetThemeRequest extends BaseRequest{

	@NotNull(message="UserId cannot be null")
	private Long userId;

	private Long categoryId;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(Long categoryId) {
		this.categoryId = categoryId;
	}
	
}
