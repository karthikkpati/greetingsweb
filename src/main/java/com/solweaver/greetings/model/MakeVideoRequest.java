package com.solweaver.greetings.model;

import java.util.List;


public class MakeVideoRequest {

	/*private String[] inputFiles;*/
	private VideoDTO[] videoDTOList;
	private String outputFileName;
	private String overlayImage;
	private int embeddedImageMinWidth;
	private int embeddedImageMaxWidth;
	private int embeddedImageMinHeight;
	private int embeddedImageMaxHeight;
	private String eventId;
	/*public String[] getInputFiles() {
		return inputFiles;
	}
	public void setInputFiles(String[] inputFiles) {
		this.inputFiles = inputFiles;
	}*/
	public String getOutputFileName() {
		return outputFileName;
	}
	public void setOutputFileName(String outputFileName) {
		this.outputFileName = outputFileName;
	}
	public String getOverlayImage() {
		return overlayImage;
	}
	public void setOverlayImage(String overlayImage) {
		this.overlayImage = overlayImage;
	}
	public int getEmbeddedImageMinWidth() {
		return embeddedImageMinWidth;
	}
	public void setEmbeddedImageMinWidth(int embeddedImageMinWidth) {
		this.embeddedImageMinWidth = embeddedImageMinWidth;
	}
	public int getEmbeddedImageMaxWidth() {
		return embeddedImageMaxWidth;
	}
	public void setEmbeddedImageMaxWidth(int embeddedImageMaxWidth) {
		this.embeddedImageMaxWidth = embeddedImageMaxWidth;
	}
	public int getEmbeddedImageMinHeight() {
		return embeddedImageMinHeight;
	}
	public void setEmbeddedImageMinHeight(int embeddedImageMinHeight) {
		this.embeddedImageMinHeight = embeddedImageMinHeight;
	}
	public int getEmbeddedImageMaxHeight() {
		return embeddedImageMaxHeight;
	}
	public void setEmbeddedImageMaxHeight(int embeddedImageMaxHeight) {
		this.embeddedImageMaxHeight = embeddedImageMaxHeight;
	}
	public String getEventId() {
		return eventId;
	}
	public void setEventId(String eventId) {
		this.eventId = eventId;
	}
	public VideoDTO[] getVideoDTOList() {
		return videoDTOList;
	}
	public void setVideoDTOList(VideoDTO[] videoDTOList) {
		this.videoDTOList = videoDTOList;
	}
	
}
