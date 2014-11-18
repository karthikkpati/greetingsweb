package com.solweaver.greetings.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.solweaver.greetings.dto.DownloadVideoRequest;
import com.solweaver.greetings.dto.DownloadVideoResponse;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.MakeVideoRequest;
import com.solweaver.greetings.dto.MakeVideoResponse;
import com.solweaver.greetings.dto.VideoDTO;
import com.solweaver.greetings.dto.VideoUploadRequest;
import com.solweaver.greetings.dto.VideoUploadResponse;
import com.solweaver.greetings.model.Event;
import com.solweaver.greetings.model.Theme;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.model.UserEvent;
import com.solweaver.greetings.service.IEventService;
import com.solweaver.greetings.service.IThemeService;
import com.solweaver.greetings.service.IUserService;
import com.solweaver.greetings.service.IVideoService;
import com.solweaver.greetings.utils.GenericUtils;
import com.solweaver.xuggler.utils.XugglerMediaUtils;

@Controller
public class VideoController {

	@Autowired
	private IVideoService videoService;

	@Autowired
	private IEventService eventService;
	
	@Autowired
	private IThemeService themeService;

	@Autowired
	private IUserService userService;
	
	public static final String EVENTS_FOLDER = "c:/karthik/junk/grite/";
	@RequestMapping(value="/hello")
	public @ResponseBody String hello(){
		return "Hello world";
	}
	
	@RequestMapping(value="/makeGreetingWithFiles", method=RequestMethod.POST)
	public @ResponseBody String makeGreeting(@RequestBody MakeVideoRequest makeVideoRequest) throws IOException{
		Theme theme = themeService.getTheme(makeVideoRequest.getThemeId());
		XugglerMediaUtils.mergeVideos(makeVideoRequest, theme);
		return makeVideoRequest.getOutputFileName();
	}
	
	@RequestMapping(value="/makeGreeting", method=RequestMethod.POST)
	public @ResponseBody MakeVideoResponse makeGreetingWithEvent(@RequestBody MakeVideoRequest makeVideoRequest,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		
		MakeVideoResponse makeVideoResponse = videoService.makeGreeting(makeVideoRequest);
		/*Long eventId = Long.valueOf(makeVideoRequest.getEventId());
		
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
		
		String requestUrl = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/download?eventId="+makeVideoRequest.getEventId();
	
		String requestUrl = request.getScheme()+"://"+request.getServerName()+request.getContextPath()+"/download?eventId="+makeVideoRequest.getEventId();
		makeVideoResponse.setOutputVideo(requestUrl);*/	
		return makeVideoResponse;
	}
	
	@RequestMapping(value="/makeGreeting1", method=RequestMethod.POST)
	public @ResponseBody MakeVideoRequest makeGreeting1() throws IOException{
		MakeVideoRequest makeVideoRequest = new MakeVideoRequest();
		makeVideoRequest.setOverlayImage("c:/karthik/junk/grite/Birthday.jpg");
		makeVideoRequest.setOutputFileName("myvideo1.flv");
		makeVideoRequest.setEventId(1L);
		VideoDTO[] videoDTOs = new VideoDTO[2];
		VideoDTO videoDTO = new VideoDTO();
		VideoDTO videoDTO1 = new VideoDTO();
		videoDTOs[0] = videoDTO;
		videoDTOs[1] = videoDTO1;
		videoDTO.setFileName("c:/karthik/junk/grite/happynewyear.mp4");
		videoDTO.setAngleOfRotation(90.0);
		videoDTO1.setFileName("c:/karthik/junk/grite/allthebest.mp4");
		videoDTO1.setAngleOfRotation(90.0);
		makeVideoRequest.setVideoDTOList(videoDTOs);
		makeVideoRequest.setEmbeddedImageMaxHeight(350);
		makeVideoRequest.setEmbeddedImageMinHeight(160);
		makeVideoRequest.setEmbeddedImageMaxWidth(482);
		makeVideoRequest.setEmbeddedImageMinWidth(50);
		return makeVideoRequest;
	}
	
	@RequestMapping(value="/upload1", method=RequestMethod.POST)
	public @ResponseBody VideoUploadResponse uploadVideo(@RequestBody VideoUploadRequest videoUploadRequest, @RequestParam(value="file", required=false) MultipartFile file,
			Model model,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		InputStream inputStream = file.getInputStream();
		String fileName = file.getOriginalFilename();
		return videoService.uploadVideo(videoUploadRequest, inputStream, fileName);
	}
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public @ResponseBody VideoUploadResponse uploadVideo(@RequestParam("file") MultipartFile file,
			Model model,
			@RequestParam("eventId") Long eventId,
			@RequestParam("userId") Long userId,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		VideoUploadRequest videoUploadRequest = new VideoUploadRequest();
		videoUploadRequest.setEventId(eventId);
		videoUploadRequest.setUserId(userId);
		InputStream inputStream = file.getInputStream();
		String fileName = file.getOriginalFilename();
		return videoService.uploadVideo(videoUploadRequest, inputStream, fileName);
	}
	
	@RequestMapping(value="/downloadRequest", method=RequestMethod.POST)
	public void downloadVideoFromRequest(
			Model model,
			@RequestBody DownloadVideoRequest downloadVideoRequest,
			/*@RequestParam("eventId") String eventId,*/
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		String fileName = null;
		DownloadVideoResponse downloadVideoResponse = new DownloadVideoResponse();
		videoService.validateEventUser(downloadVideoRequest.getEventId(), downloadVideoRequest.getUserId(), downloadVideoResponse);
		if(!GenericUtils.isSuccess(downloadVideoResponse.getErrorDetailList())){
			throw new RuntimeException(downloadVideoResponse.getErrorDetailList().get(0).getMessage());
		}
		String outputFolderName = EVENTS_FOLDER+downloadVideoRequest.getEventId()+"/output/";
		File outputFolder = new File(outputFolderName);
		if(outputFolder.isDirectory()){
			String[] fileList = outputFolder.list();
			response.setContentType("application/octet-stream");
			fileName = fileList[0];
			response.setHeader("Content-Disposition","attachment;filename="+fileName);

			File file = new File(outputFolderName+fileName);
			FileInputStream fileIn = new FileInputStream(file);
			ServletOutputStream out = response.getOutputStream();
			byte[] outputByte = new byte[4096];
			while(fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);
				outputByte = new byte[4096];
			}
			fileIn.close();
			out.flush();
			out.close();
		}
	}
	
	@RequestMapping(value="/downloadFinal", method=RequestMethod.GET)
	public void downloadFinalVideo(
			Model model,
			@RequestParam("eventId") Long eventId,
			@RequestParam("userId") Long userId,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		
		String fileName = null;
		
		DownloadVideoResponse downloadVideoResponse = new DownloadVideoResponse();
		
		UserEvent userEvent = eventService.getUserEvent(userId, eventId);
		if(userEvent == null){
			GenericUtils.buildErrorDetail(downloadVideoResponse, GenericEnum.INVALID_USER_EVENT);
			throw new RuntimeException(downloadVideoResponse.getErrorDetailList().get(0).getMessage());
		}

		String outputFolderName = EVENTS_FOLDER+eventId+"/output/";
		File outputFolder = new File(outputFolderName);
		if(outputFolder.isDirectory()){
			String[] fileList = outputFolder.list();
			if(fileList.length <= 0){
				GenericUtils.buildErrorDetail(downloadVideoResponse, GenericEnum.EVENT_IN_PROGRESS);
				throw new RuntimeException(downloadVideoResponse.getErrorDetailList().get(0).getMessage());
			}
			response.setContentType("application/octet-stream");
			for(String fileStr : fileList){
				if(fileStr.endsWith(".mp4")){
					fileName = fileStr;
				}
			}
			response.setHeader("Content-Disposition","attachment;filename="+fileName);

			File file = new File(outputFolderName+fileName);
			FileInputStream fileIn = new FileInputStream(file);
			ServletOutputStream out = response.getOutputStream();
			byte[] outputByte = new byte[4096];
			while(fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);
				outputByte = new byte[4096];
			}
			fileIn.close();
			out.flush();
			out.close();
		}
	}
	
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public void downloadVideo(
			Model model,
			@RequestParam("eventId") Long eventId,
			@RequestParam("userId") Long userId,
			@RequestParam(value="fileName",required=false) String fileName,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		
		DownloadVideoResponse downloadVideoResponse = new DownloadVideoResponse();
		
		UserEvent userEvent = eventService.getUserEvent(userId, eventId);
		if(userEvent == null){
			GenericUtils.buildErrorDetail(downloadVideoResponse, GenericEnum.INVALID_USER_EVENT);
			throw new RuntimeException(downloadVideoResponse.getErrorDetailList().get(0).getMessage());
		}

		String outputFolderName = EVENTS_FOLDER+eventId+"/upload/";
		File outputFolder = new File(outputFolderName);
		if(outputFolder.isDirectory()){
			if(fileName == null || fileName.isEmpty()){
				fileName = userEvent.getRecordedLink();
			}
			File outputFile = new File(outputFolderName+fileName);
			response.setContentType("application/octet-stream");
			response.setHeader("Content-Disposition","attachment;filename="+fileName);

			FileInputStream fileIn = new FileInputStream(outputFile);
			ServletOutputStream out = response.getOutputStream();
			byte[] outputByte = new byte[4096];
			while(fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte, 0, 4096);
				outputByte = new byte[4096];
			}
			fileIn.close();
			out.flush();
			out.close();
		}
	}
	
	@RequestMapping(value="/streamMp4", method=RequestMethod.GET)
	public void streamMp4Video(
			Model model,
			@RequestParam("eventId") Long eventId,
			@RequestParam("userId") Long userId,
			@RequestParam(value="fileName",required=false) String fileName,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		
		DownloadVideoResponse downloadVideoResponse = new DownloadVideoResponse();
		
		UserEvent userEvent = eventService.getUserEvent(userId, eventId);
		if(userEvent == null){
			GenericUtils.buildErrorDetail(downloadVideoResponse, GenericEnum.INVALID_USER_EVENT);
			throw new RuntimeException(downloadVideoResponse.getErrorDetailList().get(0).getMessage());
		}

		String outputFolderName = EVENTS_FOLDER+eventId+"/upload/";
		File outputFolder = new File(outputFolderName);
		if(outputFolder.isDirectory()){
			if(fileName == null || fileName.isEmpty()){
				fileName = userEvent.getRecordedLink();
			}
			File outputFile = new File(outputFolderName+fileName);
			response.setContentType("video/mp4");
			response.setHeader("Content-Disposition","inline; filename="+fileName);
			response.setHeader("Accept-Ranges", "bytes");
			Long outputFileLength = outputFile.length();
			response.setContentLength(outputFileLength.intValue());
            
			FileInputStream fileIn = new FileInputStream(outputFile);
			ServletOutputStream out = response.getOutputStream();
			byte[] outputByte = new byte[4096];
			while(fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte);
				outputByte = new byte[4096];
			}
			fileIn.close();
			out.flush();
			out.close();
		}
	}
	
	@RequestMapping(value="/streamMp4Final", method=RequestMethod.GET)
	public void streamMp4FinalVideo(
			Model model,
			@RequestParam("eventId") Long eventId,
			@RequestParam("userId") Long userId,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		String fileName = null;
		DownloadVideoResponse downloadVideoResponse = new DownloadVideoResponse();

		User user = userService.findUserById(userId);
		if(user == null){
			GenericUtils.buildErrorDetail(downloadVideoResponse, GenericEnum.INVALID_USER);
			throw new RuntimeException(downloadVideoResponse.getErrorDetailList().get(0).getMessage());
		}
		
		Event event = eventService.getEvent(eventId);
		if(event == null){
			GenericUtils.buildErrorDetail(downloadVideoResponse, GenericEnum.INVALID_EVENT);
			throw new RuntimeException(downloadVideoResponse.getErrorDetailList().get(0).getMessage());
		}
		
		String outputFolderName = EVENTS_FOLDER+eventId+"/output/";
		File outputFolder = new File(outputFolderName);
		if(outputFolder.isDirectory()){
			String[] outputFiles = outputFolder.list();
			for(String outputFile : outputFiles){
				if(outputFile.endsWith(".mp4")){
					fileName = outputFile;
					break;
				}
			}
			File outputFile = new File(outputFolderName+fileName);
			response.setContentType("video/mp4");
			response.setHeader("Content-Disposition","inline; filename="+fileName);
			response.setHeader("Accept-Ranges", "bytes");
			Long outputFileLength = outputFile.length();
			response.setContentLength(outputFileLength.intValue());
            
			FileInputStream fileIn = new FileInputStream(outputFile);
			ServletOutputStream out = response.getOutputStream();
			byte[] outputByte = new byte[4096];
			while(fileIn.read(outputByte, 0, 4096) != -1) {
				out.write(outputByte);
				outputByte = new byte[4096];
			}
			fileIn.close();
			out.flush();
			out.close();
		}
	}

}
