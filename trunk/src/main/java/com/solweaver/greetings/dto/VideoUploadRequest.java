package com.solweaver.greetings.dto;

import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

public class VideoUploadRequest extends BaseRequest{

	@NotNull(message="User Id cannot be null")
	private Long userId;
	
	@NotNull(message="Event Id cannot be null")
	private Long eventId;

	private MultipartFile multiPartFile;
	
	public Long getUserId() {
		return userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public Long getEventId() {
		return eventId;
	}

	public void setEventId(Long eventId) {
		this.eventId = eventId;
	}

	public MultipartFile getMultiPartFile() {
		return multiPartFile;
	}

	public void setMultiPartFile(MultipartFile multiPartFile) {
		this.multiPartFile = multiPartFile;
	}
	
}
