package com.solweaver.greetings.dto;

import javax.validation.constraints.NotNull;


public class GetCategoryRequest extends BaseRequest{

	@NotNull(message="UserId cannot be null")
	private Long userId;

	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

}
