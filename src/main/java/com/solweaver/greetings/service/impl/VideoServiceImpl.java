package com.solweaver.greetings.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.EventDAO;
import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.VideoUploadRequest;
import com.solweaver.greetings.dto.VideoUploadResponse;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.EventStatus;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.service.IVideoService;
import com.solweaver.greetings.utils.GenericUtils;

@Service
public class VideoServiceImpl implements IVideoService{

	public static final String EVENTS_FOLDER = "c:/karthik/junk/grite/";

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private EventDAO eventDAO;
	
	@Override
	@Transactional
	public VideoUploadResponse uploadVideo(
			VideoUploadRequest videoUploadRequest, InputStream inputStream,
			String fileName) throws IOException, FileNotFoundException{
		
		VideoUploadResponse videoUploadResponse = new VideoUploadResponse();
		
		User user = userDAO.findActiveUserById(videoUploadRequest.getUserId());
		if(user == null){
			GenericUtils.buildErrorDetail(videoUploadResponse, GenericEnum.INVALID_USER);
			return videoUploadResponse;
		}
		
		Event event = eventDAO.findByEventAndUserId(videoUploadRequest.getEventId(),videoUploadRequest.getUserId());
		
		if(event == null){
			GenericUtils.buildErrorDetail(videoUploadResponse, GenericEnum.INVALID_EVENT);
			return videoUploadResponse;
		}else if(!event.getEventStatus().equals(EventStatus.Active)){
			GenericUtils.buildErrorDetail(videoUploadResponse, GenericEnum.EVENT_NO_LONGER_EXISTS);
			return videoUploadResponse;
		}

		String outputFolderName = EVENTS_FOLDER+videoUploadRequest.getEventId()+"/upload/";
		File outputFolder = new File(outputFolderName);
		
		if(!outputFolder.isDirectory()){
			outputFolder.mkdirs();
		}
		String outputFileName = outputFolderName+fileName;
		
		FileOutputStream fos;
		fos = new FileOutputStream(outputFileName);
		IOUtils.copy(inputStream, fos);
		fos.close();
		
		GenericUtils.buildErrorDetail(videoUploadResponse, GenericEnum.Success);
		return videoUploadResponse;
	}

}
