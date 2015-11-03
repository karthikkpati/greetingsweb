package com.solweaver.greetings.dto;



public class UserEventDTO {

    private Long userEventID;
	
	private EventDTO eventDTO;
	
	private UserDTO userDTO;
	
	private String userEventType;
	
/*	private String inviteeLink;
*/
	private String recordedLink;
	
	private String inviteStatus;
	
	private String message;

	public Long getUserEventID() {
		return userEventID;
	}

	public void setUserEventID(Long userEventID) {
		this.userEventID = userEventID;
	}

	public EventDTO getEventDTO() {
		return eventDTO;
	}

	public void setEventDTO(EventDTO eventDTO) {
		this.eventDTO = eventDTO;
	}

	public UserDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(UserDTO userDTO) {
		this.userDTO = userDTO;
	}

	/*public String getInviteeLink() {
		return inviteeLink;
	}

	public void setInviteeLink(String inviteeLink) {
		this.inviteeLink = inviteeLink;
	}*/

	public String getRecordedLink() {
		return recordedLink;
	}

	public void setRecordedLink(String recordedLink) {
		this.recordedLink = recordedLink;
	}

	public String getUserEventType() {
		return userEventType;
	}

	public void setUserEventType(String userEventType) {
		this.userEventType = userEventType;
	}

	public String getInviteStatus() {
		return inviteStatus;
	}

	public void setInviteStatus(String inviteStatus) {
		this.inviteStatus = inviteStatus;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

}
