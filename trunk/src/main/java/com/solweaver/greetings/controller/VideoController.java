package com.solweaver.greetings.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

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

	public static final String UPLOAD_OUTPUT_FOLDER = "c:/karthik/junk/grite/";
	@RequestMapping(value="/hello")
	public @ResponseBody String hello(){
		return "Hello world";
	}
	
	@RequestMapping(value="/makeGreeting", method=RequestMethod.POST)
	public @ResponseBody String makeGreeting(@RequestBody MakeVideoRequest makeVideoRequest) throws IOException{
		
		XugglerMediaUtils.mergeVideos(makeVideoRequest);
		return makeVideoRequest.getOutputFileName();
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
		String outputFolderName = UPLOAD_OUTPUT_FOLDER+eventId+"/upload/";
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
}
