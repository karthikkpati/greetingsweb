package com.solweaver.greetings.dto;

import java.util.List;

public class GetRecipientEventResponse extends BaseResponse{

	private List<RecipientEventDTO> recipientEventDTOList;

	public List<RecipientEventDTO> getRecipientEventDTOList() {
		return recipientEventDTOList;
	}

	public void setRecipientEventDTOList(
			List<RecipientEventDTO> recipientEventDTOList) {
		this.recipientEventDTOList = recipientEventDTOList;
	}

}
