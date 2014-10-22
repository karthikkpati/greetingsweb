package com.solweaver.greetings.dto;

import java.util.List;

public class GetThemeResponse extends BaseResponse{

	private List<ThemeDTO> themeDTOList;

	public List<ThemeDTO> getThemeDTOList() {
		return themeDTOList;
	}

	public void setThemeDTOList(List<ThemeDTO> themeDTOList) {
		this.themeDTOList = themeDTOList;
	}
	
}
