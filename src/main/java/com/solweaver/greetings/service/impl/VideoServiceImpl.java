package com.solweaver.greetings.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.EventDAO;
import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dao.UserEventDAO;
import com.solweaver.greetings.dto.BaseResponse;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.MakeVideoRequest;
import com.solweaver.greetings.dto.MakeVideoResponse;
import com.solweaver.greetings.dto.VideoDTO;
import com.solweaver.greetings.dto.VideoUploadRequest;
import com.solweaver.greetings.dto.VideoUploadResponse;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.EventStatus;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.service.IVideoService;
import com.solweaver.greetings.utils.GenericUtils;
import com.solweaver.xuggler.utils.XugglerMediaUtils;

@Service
public class VideoServiceImpl implements IVideoService{

	public static final String EVENTS_FOLDER = "c:/karthik/junk/grite/";

	@Autowired
	private UserDAO userDAO;
	
	@Autowired
	private EventDAO eventDAO;

	@Autowired
	private UserEventDAO userEventDAO;
	
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
		
		Event event = null;
		
		/*Event event = eventDAO.findByEventAndUserId(videoUploadRequest.getEventId(),videoUploadRequest.getUserId());*/
		
/*		if(event == null){
			GenericUtils.buildErrorDetail(videoUploadResponse, GenericEnum.INVALID_EVENT);
			return videoUploadResponse;
		}else if(!event.getEventStatus().equals(EventStatus.Active)){
			GenericUtils.buildErrorDetail(videoUploadResponse, GenericEnum.EVENT_NO_LONGER_EXISTS);
			return videoUploadResponse;
		}
*/
		UserEvent userEvent = userEventDAO.findByUserAndEvent(videoUploadRequest.getUserId(), videoUploadRequest.getEventId());
		
		if(userEvent == null){
			GenericUtils.buildErrorDetail(videoUploadResponse, GenericEnum.INVALID_USER_EVENT);
			return videoUploadResponse;
		}else{
			event = userEvent.getEvent();
			if(!event.getEventStatus().equals(EventStatus.Active)){
				GenericUtils.buildErrorDetail(videoUploadResponse, GenericEnum.EVENT_NO_LONGER_EXISTS);
				return videoUploadResponse;
			}
		}
		String outputFolderName = EVENTS_FOLDER+videoUploadRequest.getEventId()+"/upload/";
		File outputFolder = new File(outputFolderName);
		
		if(!outputFolder.isDirectory()){
			outputFolder.mkdirs();
		}
		
		fileName = Calendar.getInstance().getTimeInMillis() + "_" + fileName;
		String outputFileName = outputFolderName+ fileName;
		
		FileOutputStream fos;
		fos = new FileOutputStream(outputFileName);
		IOUtils.copy(inputStream, fos);
		fos.close();

		userEvent.setRecordedLink(fileName);
		
		userEventDAO.merge(userEvent);
		
		GenericUtils.buildErrorDetail(videoUploadResponse, GenericEnum.Success);
		return videoUploadResponse;
	}

	@Override
	@Transactional
	public MakeVideoResponse makeGreeting(MakeVideoRequest makeVideoRequest) throws IOException {
		MakeVideoResponse makeVideoResponse = new MakeVideoResponse();

		Long eventId = makeVideoRequest.getEventId();
		
		User user = userDAO.findActiveUserById(makeVideoRequest.getUserId());
		if(user == null){
			GenericUtils.buildErrorDetail(makeVideoResponse, GenericEnum.INVALID_USER);
			return makeVideoResponse;
		}
		
		Event event = eventDAO.findByEventAndUserId(makeVideoRequest.getEventId(),makeVideoRequest.getUserId());
		
		if(event == null){
			GenericUtils.buildErrorDetail(makeVideoResponse, GenericEnum.INVALID_EVENT);
			return makeVideoResponse;
		}else if(!event.getEventStatus().equals(EventStatus.Active)){
			GenericUtils.buildErrorDetail(makeVideoResponse, GenericEnum.EVENT_NO_LONGER_EXISTS);
			return makeVideoResponse;
		}

		String eventUploadFolderName = EVENTS_FOLDER+eventId+"/upload/";
		File eventOutputFolder = new File(eventUploadFolderName);
		
		if(eventOutputFolder.isDirectory()){
			String[] eventFiles = eventOutputFolder.list();
			VideoDTO[] videoDTOList = makeVideoRequest.getVideoDTOList();
			if(videoDTOList == null){
				videoDTOList = new VideoDTO[eventFiles.length];
			}
			for(int i=0;i<eventFiles.length;i++){
				VideoDTO videoDTO = new VideoDTO();
				videoDTO.setFileName(eventUploadFolderName+eventFiles[i]);
				videoDTOList[i] = videoDTO;
			}
			makeVideoRequest.setVideoDTOList(videoDTOList);
		}
		
		XugglerMediaUtils.mergeVideos(makeVideoRequest);
		
		GenericUtils.buildErrorDetail(makeVideoResponse, GenericEnum.Success);
		
		return makeVideoResponse;
	}

	@Override
	@Transactional
	public void validateEventUser(Long eventId, Long userId, BaseResponse baseResponse) {
		User user = userDAO.findActiveUserById(userId);
		if(user == null){
			GenericUtils.buildErrorDetail(baseResponse, GenericEnum.INVALID_USER);
			return;
		}
		Event event = eventDAO.findByEventAndUserId(eventId,userId);
		if(event == null){
			GenericUtils.buildErrorDetail(baseResponse, GenericEnum.INVALID_EVENT);
			return;
		}else if(!event.getEventStatus().equals(EventStatus.Active)){
			GenericUtils.buildErrorDetail(baseResponse, GenericEnum.EVENT_NO_LONGER_EXISTS);
			return;
		}
	}

}
