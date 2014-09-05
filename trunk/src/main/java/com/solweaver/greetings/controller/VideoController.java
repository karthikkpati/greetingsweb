package com.solweaver.greetings.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.solweaver.greetings.model.MakeVideoRequest;
import com.solweaver.greetings.model.VideoDTO;
import com.solweaver.xuggler.utils.XugglerMediaUtils;

@Controller
public class VideoController {

	public static final String EVENTS_FOLDER = "c:/karthik/junk/grite/";
	@RequestMapping(value="/hello")
	public @ResponseBody String hello(){
		return "Hello world";
	}
	
	@RequestMapping(value="/makeGreetingWithFiles", method=RequestMethod.POST)
	public @ResponseBody String makeGreeting(@RequestBody MakeVideoRequest makeVideoRequest) throws IOException{
		
		XugglerMediaUtils.mergeVideos(makeVideoRequest);
		return makeVideoRequest.getOutputFileName();
	}
	
	@RequestMapping(value="/makeGreeting", method=RequestMethod.POST)
	public @ResponseBody String makeGreetingWithEvent(@RequestBody MakeVideoRequest makeVideoRequest,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		
		String eventIdStr = makeVideoRequest.getEventId();
		Long eventId = Long.valueOf(eventIdStr);
		
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
		
		return requestUrl;
	}
	
	@RequestMapping(value="/makeGreeting1", method=RequestMethod.POST)
	public @ResponseBody MakeVideoRequest makeGreeting1() throws IOException{
		MakeVideoRequest makeVideoRequest = new MakeVideoRequest();
		makeVideoRequest.setOverlayImage("c:/karthik/junk/grite/Birthday.jpg");
		makeVideoRequest.setOutputFileName("myvideo1.flv");
		makeVideoRequest.setEventId("1");
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
	
	@RequestMapping(value="/upload", method=RequestMethod.POST)
	public @ResponseBody String uploadVideo(@RequestParam("file") MultipartFile file,
			Model model,
			@RequestParam("eventId") String eventId,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		
		
		InputStream inputStream = file.getInputStream();
		String fileName = file.getOriginalFilename();
		System.out.println("File name is "+fileName);
		String outputFolderName = EVENTS_FOLDER+eventId+"/upload/";
		File outputFolder = new File(outputFolderName);
		
		if(!outputFolder.isDirectory()){
			outputFolder.mkdirs();
		}
		String outputFileName = outputFolderName+fileName;
		
		FileOutputStream fos = new FileOutputStream(outputFileName);
		IOUtils.copy(inputStream, fos);
		fos.close();
		return outputFileName;
	}
	
	@RequestMapping(value="/download", method=RequestMethod.GET)
	public void downloadVideo(
			Model model,
			@RequestParam("eventId") String eventId,
			HttpServletRequest request, 
			HttpServletResponse response) throws IOException{
		
		String fileName = null;
		
		String outputFolderName = EVENTS_FOLDER+eventId+"/output/";
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
			}
			fileIn.close();
			out.flush();
			out.close();
			
		}

	}
}
