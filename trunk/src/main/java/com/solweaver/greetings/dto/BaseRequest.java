package com.solweaver.greetings.dto;

import javax.validation.constraints.NotNull;

public class BaseRequest {

	@NotNull(message="Channel cannot be null")
	private String channel;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}
	
}
