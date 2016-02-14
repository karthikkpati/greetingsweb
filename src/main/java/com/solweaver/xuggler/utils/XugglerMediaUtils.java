package com.solweaver.xuggler.utils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.solweaver.greetings.dto.MakeVideoRequest;
import com.solweaver.greetings.dto.VideoDTO;
import com.solweaver.greetings.model.Theme;
import com.xuggle.mediatool.IMediaReader;
import com.xuggle.mediatool.IMediaTool;
import com.xuggle.mediatool.IMediaWriter;
import com.xuggle.mediatool.ToolFactory;
import com.xuggle.xuggler.ICodec;

public class XugglerMediaUtils {
	
	public static String VIDEOFILESLOCN = "c:/karthik/junk/grite/";
	
	public static void mergeVideos(/*String eventId,String[] inputFiles, String outputFileName, String overlayImage,
			int embeddedImageMinWidth, int embeddedImageMaxWidth,
			int embeddedImageMinHeight, int embeddedImageMaxHeight*/MakeVideoRequest makeVideoRequest, Theme theme) throws IOException{
		Date startTime = new Date();
		System.out.println("Start Date is "+startTime);
		String eventFolder =  VIDEOFILESLOCN + makeVideoRequest.getEventId();
		// create a media reader
		File formattedFilesLocation = new File(eventFolder+"/formattedFiles/");
		File flvFilesLocation = new File(eventFolder+"/flvFiles/");
		if(!flvFilesLocation.isDirectory()){
			flvFilesLocation.mkdirs();
		}
		if(!formattedFilesLocation.isDirectory()){
			formattedFilesLocation.mkdirs();
		}
		String mergedFile = eventFolder + "/mergedfile.flv"; 
		/*String flvFiles[] = new String[makeVideoRequest.getVideoDTOList().length];
		String formattedFiles[] = new String[makeVideoRequest.getVideoDTOList().length];*/
		
		String overlayImagePath = makeVideoRequest.getOverlayImage();
		BufferedImage overlayImage = null;
		if(theme != null){
			overlayImagePath = theme.getFileSystemPath();
			try {
				overlayImage = ImageIO.read(new File(overlayImagePath));
			}catch (IOException e) {
				e.printStackTrace();
				throw new RuntimeException("Could not open file");
			}

		}

		for(int i=0;i<makeVideoRequest.getVideoDTOList().length; i++){
			VideoDTO videoDTO = makeVideoRequest.getVideoDTOList()[i];
			videoDTO.setFileName(eventFolder +"/upload/"+ videoDTO.getFileName());
			String inputFile = videoDTO.getFileName();
			File file = new File(inputFile);
			String inputFileName = file.getName();
		//	String formattedFile = null;
			if(!(inputFile.endsWith(".flv") || inputFile.endsWith(".jpg") || inputFile.endsWith(".png"))){
				int fileSeperator = inputFileName.indexOf(".");
				if(fileSeperator == -1){
					throw new RuntimeException("Incorrect file name "+inputFile);
				}else{
					//String formattedFile = inputFile.substring(0, fileSeperator) + ".flv";
					String formattedFile = flvFilesLocation + "\\" + inputFileName.substring(0, fileSeperator) + ".flv";
					//formattedFile = formattedFilesLocation+"\\formattedFile"+i+".flv";
					System.out.println("Input file name is "+inputFile);
					System.out.println("Formatted file name is "+formattedFile);
					videoDTO.setFlvFile(formattedFile);
					
					videoDTO.setFormattedFile(formattedFilesLocation+"\\formattedFile"+i+".flv");
					
					ConvertVideo convertVideo = new ConvertVideo(new File(videoDTO.getFileName()), new File(videoDTO.getFormattedFile()), overlayImage.getHeight(), overlayImage.getWidth());
					convertVideo.run();
					
					//flvFiles[i] = formattedFile;
					convertVideoFormat(videoDTO.getFormattedFile(), formattedFile);
				}
			}else{
				//formattedFile = inputFile;
				videoDTO.setFlvFile(inputFile);
				videoDTO.setFormattedFile(inputFile);
				//flvFiles[i] = inputFile;
			}
			
			//formattedFiles[i] = formattedFile;
			//formattedFiles[i] = formattedFilesLocation+"\\formattedFile"+i+".flv";
			/*videoDTO.setFormattedFile(formattedFilesLocation+"\\formattedFile"+i+".flv");
			
			ConvertVideo convertVideo = new ConvertVideo(new File(videoDTO.getFlvFile()), new File(videoDTO.getFormattedFile()));
			convertVideo.run();*/
		}
		
		concatenateFiles(makeVideoRequest.getVideoDTOList(), mergedFile, overlayImage.getHeight(), overlayImage.getWidth());		
		
		IMediaReader mediaReader = ToolFactory.makeReader(mergedFile);

		// configure it to generate BufferImages

		mediaReader.setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);

		String outputFileName = makeVideoRequest.getOutputFileName();
		String outputFolderName = eventFolder + "/output/" ; 
		
		File outputFolder = new File(outputFolderName);
		if(!outputFolder.isDirectory()){
			outputFolder.mkdirs();
		}
		
		outputFileName = outputFolderName + outputFileName;
		
		System.out.println("Output file name is "+outputFileName);
		
		
		IMediaWriter mediaWriter =	ToolFactory.makeWriter(outputFileName, mediaReader);
		
		String thumbNailImagePath = outputFolderName + "/thumbNail.jpg";

		IMediaTool imageMediaTool = new StaticImageMediaTool(overlayImagePath, thumbNailImagePath, makeVideoRequest.getEmbeddedImageMinWidth(), makeVideoRequest.getEmbeddedImageMaxWidth(),
				makeVideoRequest.getEmbeddedImageMinHeight(), makeVideoRequest.getEmbeddedImageMaxHeight());

		IMediaTool audioVolumeMediaTool = new VolumeAdjustMediaTool(0.1);

		// create a tool chain:

		// reader -> addStaticImage -> reduceVolume -> writer

		mediaReader.addListener(imageMediaTool);

		imageMediaTool.addListener(audioVolumeMediaTool);

		audioVolumeMediaTool.addListener(mediaWriter);

		while (mediaReader.readPacket() == null);

		/*mediaReader.setCloseOnEofOnly(false); 
		mediaWriter.setForceInterleave(false);*/ 

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//mediaWriter.close();
		String mp4OutputFile = outputFileName.substring(0, outputFileName.indexOf(".")) + ".mp4";
		System.out.println(mp4OutputFile);
	
		convertVideoFormat(outputFileName, mp4OutputFile);
		Date endDate = new Date();
		System.out.println("End Date is "+endDate);
	}
	
	 public static void concatenateFiles(String[] sourceUrls, String destinationUrl) throws IOException
	  {
	   System.out.println("transcoding starts");
	   
	   //video parameters
	   final int videoStreamIndex = 0;
	   final int videoStreamId = 0;

	   final int width = 400;
	   final int height = 240;
	   
	   //audio parameters
	      
	   final int audioStreamIndex = 1;
	   final int audioStreamId = 0;
	   final int channelCount = 2;
	   final int sampleRate = 44100; //Hz
	   
	   IMediaReader[] mediaReaderArray = new IMediaReader[sourceUrls.length];
	   
	   MediaConcatenator concatenator = new MediaConcatenator(audioStreamIndex,videoStreamIndex, null);
	   for(int i=0; i<sourceUrls.length ; i++){
		   mediaReaderArray[i] = ToolFactory.makeReader(sourceUrls[i]);
		   mediaReaderArray[i].addListener(concatenator);
	   }

	   IMediaWriter writer = ToolFactory.makeWriter(destinationUrl);
	   concatenator.addListener(writer);
	   System.out.println("Rational value is "+writer.getDefaultTimebase());
	   writer.addVideoStream(videoStreamIndex, videoStreamId, ICodec.ID.CODEC_ID_H264,  width,height);
	   writer.addAudioStream(audioStreamIndex, audioStreamId, channelCount, sampleRate);

	   for(int i=0; i<sourceUrls.length ; i++){
		   while(mediaReaderArray[i].readPacket() == null){
		   }
	   }
	  /* BufferedImage logoImage = ImageIO.read(new File(imageFilename));
		BufferedImage newYearLogo = convertImage(logoImage, BufferedImage.TYPE_3BYTE_BGR);
		//writer.addVideoStream(videoStreamIndex, videoStreamId, width, height);
	   

	   for(int i =15000;i<16000;i++){
		   writer.encodeVideo(videoStreamIndex, newYearLogo, i, TimeUnit.MILLISECONDS);
	   }
	   */
	   writer.close(); 
	   System.out.println("finished merging");
	  }

	 public static void concatenateFiles(VideoDTO[] videoDTOList, String destinationUrl, int height, int width) throws IOException
	  {
	   System.out.println("transcoding starts");
	   
	   //video parameters
	   final int videoStreamIndex = 0;
	   final int videoStreamId = 0;

	   //audio parameters
	      
	   final int audioStreamIndex = 1;
	   final int audioStreamId = 0;
	   final int channelCount = 2;
	   final int sampleRate = 44100; //Hz
	   
	   IMediaReader[] mediaReaderArray = new IMediaReader[videoDTOList.length];
	   IMediaWriter writer = ToolFactory.makeWriter(destinationUrl);
	   Map<Double, MediaConcatenator> mediaConcatenatorMap = new HashMap<Double, MediaConcatenator>();
	   MediaConcatenator.mNextAudio=0;
	   MediaConcatenator.mNextVideo=0;
	   MediaConcatenator.mOffset=0;
	   //concatenator.addListener(writer);
	   for(int i=0; i<videoDTOList.length ; i++){
		   mediaReaderArray[i] = ToolFactory.makeReader(videoDTOList[i].getFormattedFile());
		   //mediaReaderArray[i].addListener(concatenator);
		   System.out.println("Angle of rotation is "+videoDTOList[i].getAngleOfRotation());
		   if(videoDTOList[i].getAngleOfRotation() == null ) {
			   MediaConcatenator concatenator = mediaConcatenatorMap.get(0.0); 
			   if(concatenator == null){
				   concatenator = new MediaConcatenator(audioStreamIndex,videoStreamIndex, null);
				   mediaConcatenatorMap.put(0.0, concatenator);
			   }
			   mediaReaderArray[i].addListener(concatenator);
		   }else{
			   MediaConcatenator mediaConcatenator = mediaConcatenatorMap.get(videoDTOList[i].getAngleOfRotation());
			   if(mediaConcatenator == null){
				   mediaConcatenator = new MediaConcatenator(audioStreamIndex,videoStreamIndex, videoDTOList[i].getAngleOfRotation());
					  mediaConcatenatorMap.put(videoDTOList[i].getAngleOfRotation(), mediaConcatenator);
			   }
			   mediaReaderArray[i].setBufferedImageTypeToGenerate(BufferedImage.TYPE_3BYTE_BGR);
			   mediaReaderArray[i].addListener(mediaConcatenator);
		   }
	   }

	   Collection<MediaConcatenator> mediaConcatenatorList = mediaConcatenatorMap.values();
	   for(MediaConcatenator mediaConcatenator : mediaConcatenatorList){
		   mediaConcatenator.addListener(writer);
	   }
	   System.out.println("Rational value is "+writer.getDefaultTimebase());
	   writer.addVideoStream(videoStreamIndex, videoStreamId, ICodec.ID.CODEC_ID_H264, width,height);
	   writer.addAudioStream(audioStreamIndex, audioStreamId, channelCount, sampleRate);

	   for(int i=0; i<videoDTOList.length ; i++){
		   while(mediaReaderArray[i].readPacket() == null){
		   }
		   mediaReaderArray[i].close();
	   }
	  /* BufferedImage logoImage = ImageIO.read(new File(imageFilename));
		BufferedImage newYearLogo = convertImage(logoImage, BufferedImage.TYPE_3BYTE_BGR);
		//writer.addVideoStream(videoStreamIndex, videoStreamId, width, height);
	   

	   for(int i =15000;i<16000;i++){
		   writer.encodeVideo(videoStreamIndex, newYearLogo, i, TimeUnit.MILLISECONDS);
	   }
	   */
	   writer.flush();
	   writer.close(); 
	   System.out.println("finished merging");
	  }
	 public static boolean convertVideoFormat(String inputFileName, String outputFileName){
		try{
			IMediaReader mediaReader = ToolFactory.makeReader(inputFileName);
			IMediaWriter mediaWriter = ToolFactory.makeWriter(outputFileName, mediaReader);
			mediaReader.addListener(mediaWriter);
			while (mediaReader.readPacket() == null);
		}
		catch(Exception exception){
			exception.printStackTrace();
		}
		return true;
	 }
	 
	 public static BufferedImage convertImage(BufferedImage sourceImage, int targetType){
		 BufferedImage image;
		 if(sourceImage.getType() == targetType){
			 image = sourceImage;
		 }else{
			 image = new BufferedImage(sourceImage.getWidth(), sourceImage.getHeight(), targetType);
			 image.getGraphics().drawImage(sourceImage, 0, 0, null);
		 }
		 return image;
	 }
}
