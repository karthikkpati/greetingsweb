package com.solweaver.greetings.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.solweaver.greetings.dto.BaseResponse;
import com.solweaver.greetings.dto.MakeVideoRequest;
import com.solweaver.greetings.dto.MakeVideoResponse;
import com.solweaver.greetings.dto.VideoUploadRequest;
import com.solweaver.greetings.dto.VideoUploadResponse;


public interface IVideoService {

	public VideoUploadResponse uploadVideo(VideoUploadRequest videoUploadRequest,
			InputStream inputStream, String fileName, String message) throws IOException, FileNotFoundException;

	public MakeVideoResponse makeGreeting(MakeVideoRequest makeVideoRequest) throws IOException;

	public void validateEventUser(Long eventId, Long userId, BaseResponse baseResponse);

}
