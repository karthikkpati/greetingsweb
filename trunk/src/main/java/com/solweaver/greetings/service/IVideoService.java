package com.solweaver.greetings.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.solweaver.greetings.dto.VideoUploadRequest;
import com.solweaver.greetings.dto.VideoUploadResponse;


public interface IVideoService {

	public VideoUploadResponse uploadVideo(VideoUploadRequest videoUploadRequest,
			InputStream inputStream, String fileName) throws IOException, FileNotFoundException;

}
