package com.solweaver.greetings.dto;

public enum EventErrorEnum {

	FROM_USER_INVALID("1001","From User Details cannot be null"), INVALID_EVENT_DATE("1002", "Event Date is Invalid"), EVENT_STATUS_IS_REQUIRED("1003","Event Status is Reqd"),
	USER_EVENT_TYPE_IS_REQUIRED("1004", "User Event Type Cannot be null"), USER_EVENT_DETAILS_ARE_REQUIRED("1005", "Event and User Details are reqired"),
	INVALID_RECORDING_MODE("1006", "Recording Mode Cannot be null"), INVALID_EVENT_ID("1007", "Event Id Cannot be null"), INVALID_USER_EVENT_DETAILS("1008", "Invalid User Event Details"), 
	INVALID_EVENT_NAME("1009", "Invalid Event Name"), INVALID_EVENT_DESCRIPTION("1010", "Invalid Event Description"), INVALID_RECIPIENT_USER("1011", "Invalid Recipient User"), 
	INVALID_INVITEE("1012", "Invalid Invitee");
	
	public String code;
	public String message;
	
	private EventErrorEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
