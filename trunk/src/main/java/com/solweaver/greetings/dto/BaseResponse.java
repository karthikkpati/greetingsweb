package com.solweaver.greetings.dto;

import java.util.List;

public class BaseResponse {

	private List<ErrorDetail> errorDetailList;

	public List<ErrorDetail> getErrorDetailList() {
		return errorDetailList;
	}

	public void setErrorDetailList(List<ErrorDetail> errorDetailList) {
		this.errorDetailList = errorDetailList;
	}
}
