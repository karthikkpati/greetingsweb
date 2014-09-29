package com.solweaver.greetings.dto;

public enum GenericEnum {

	Success("0000", "Success"), DB_EXCEPTION("0001", "Exception while updating database"), INVALID_USER("0002", "Invalid User"), DUPLICATE_USER("0003","Duplicate User")
	, INVALID_CREATED_BY_USER("0004","Invalid Created By User"), INVALID_CHANNEL("0005","INvalid Channel"), CONFIRM_PASSWORD("0006","Passwords doesnt match"), INVALID_GENDER("0007","Invalid Gender");
	
	public String code;
	public String message;
	
	private GenericEnum(String code, String message) {
		this.code = code;
		this.message = message;
	}
}
