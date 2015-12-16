package com.solweaver.greetings.service.impl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.EventDAO;
import com.solweaver.greetings.dao.ThemeDAO;
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
import com.solweaver.greetings.model.InviteStatus;
import com.solweaver.greetings.model.Theme;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.model.VideoStatus;
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
	
	@Autowired
	private ThemeDAO themeDAO;
	
	@Override
	@Transactional
	public VideoUploadResponse uploadVideo(
			VideoUploadRequest videoUploadRequest, InputStream inputStream,
			String fileName, String message) throws IOException, FileNotFoundException{
		
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
		
		fileName = userEvent.getId() + "_" + fileName;
		String outputFileName = outputFolderName+ fileName;
		
		FileOutputStream fos;
		fos = new FileOutputStream(outputFileName);
		IOUtils.copy(inputStream, fos);
		fos.close();

		userEvent.setInviteStatus(InviteStatus.Uploaded);
		userEvent.setRecordedLink(fileName);
		userEvent.setMessage(message);
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

		List<UserEvent> userEventList = event.getUserEventList();
		Map<Long, UserEvent> userEventMap = new HashMap<Long, UserEvent>();
		for(UserEvent userEvent : userEventList){
			userEventMap.put(userEvent.getId(), userEvent);
		}
		
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
			VideoDTO[] videoDTOList = makeVideoRequest.getVideoDTOList();
			if(videoDTOList != null && videoDTOList.length > 0){
				for(int i=0;i<videoDTOList.length;i++){
					VideoDTO videoDTO = videoDTOList[i];
					if(videoDTO.getUserEventId() != null){
						videoDTO.setFileName(userEventMap.get(videoDTO.getUserEventId()).getRecordedLink());
						if(makeVideoRequest.getRotationAngle() != null && !makeVideoRequest.getRotationAngle().isEmpty()){
							double rotationAngle = 0;
							try{
								rotationAngle = Double.valueOf(makeVideoRequest.getRotationAngle());
							}catch(Exception e){
								e.printStackTrace();
							}
							videoDTO.setAngleOfRotation(rotationAngle);
						}
					}
				}
			}else{
				GenericUtils.buildErrorDetail(makeVideoResponse, GenericEnum.USER_EVENT_ORDER_IS_REQUIRED);
				return makeVideoResponse;
			}
			
/*			if(videoDTOList == null){
				videoDTOList = new VideoDTO[eventFiles.length];
			}
			for(int i=0;i<eventFiles.length;i++){
				VideoDTO videoDTO = new VideoDTO();
				videoDTO.setFileName(eventUploadFolderName+eventFiles[i]);
				videoDTOList[i] = videoDTO;
			}
*/			makeVideoRequest.setVideoDTOList(videoDTOList);
		}
		
		Theme theme = themeDAO.findById(makeVideoRequest.getThemeId(), false);
		
		XugglerMediaUtils.mergeVideos(makeVideoRequest, theme);
		
		event.setVideoStatus(VideoStatus.Completed);
		event.setEventStatus(EventStatus.Completed);
		
		eventDAO.merge(event);
		
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
