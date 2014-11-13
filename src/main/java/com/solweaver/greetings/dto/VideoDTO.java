package com.solweaver.greetings.dto;

public class VideoDTO {

	private String fileName;
	private Double angleOfRotation;
	private String formattedFile;
	private String flvFile;
	private Long userEventId;
	
	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Double getAngleOfRotation() {
		return angleOfRotation;
	}

	public void setAngleOfRotation(Double angleOfRotation) {
		this.angleOfRotation = angleOfRotation;
	}

	public String getFormattedFile() {
		return formattedFile;
	}

	public void setFormattedFile(String formattedFile) {
		this.formattedFile = formattedFile;
	}

	public String getFlvFile() {
		return flvFile;
	}

	public void setFlvFile(String flvFile) {
		this.flvFile = flvFile;
	}

	public Long getUserEventId() {
		return userEventId;
	}

	public void setUserEventId(Long userEventId) {
		this.userEventId = userEventId;
	}

}
