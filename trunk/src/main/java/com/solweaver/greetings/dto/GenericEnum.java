package com.solweaver.greetings.dto;

public enum GenericEnum {

	Success("0000", "Success"), DB_EXCEPTION("0001", "Exception while updating database"), INVALID_USER("0002", "Invalid User"), DUPLICATE_USER("0003","Duplicate User")
	, INVALID_CREATED_BY_USER("0004","Invalid Created By User"), INVALID_CHANNEL("0005","INvalid Channel"), CONFIRM_PASSWORD("0006","Passwords doesnt match"), INVALID_GENDER("0007","Invalid Gender"), 
	INVALID_USERNAME_PASSWORD("0008", "Invalid Username or password"), USER_DOESNT_EXIST("0009","Username doesnt exist"), INVALID_EVENT_STATUS("0010","Invalid Event Status"), INVALID_EVENT("0011","Invalid Event"), EVENT_NO_LONGER_EXISTS("0012","Event is either closed or deleted"), 
	USER_EVENT_EXISTS("0013", "User is already associated to this event"), INVALID_USER_EVENT("0014"," User is not assoicated to event"), EVENT_IN_PROGRESS("0015","Final video is not ready"), 
	INVALID_INVITEE_STATUS("0016","Invalid Invitee Status"), INVALID_INVITEE_RECIPIENT("0017","Invitee list cannot have receiver email"), USER_EVENT_ORDER_IS_REQUIRED("0018","Order is required to make greeting"), OUTPUT_NOT_AVAILABLE("0019", "Output video for this event is not available");
	
	public String code;
	public String message;
	
	private GenericEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
