package com.solweaver.greetings.dto;

import java.util.ArrayList;
import java.util.List;

public class GetThemeResponse extends BaseResponse{

	private List<CategoryDTO> categoryDTOList = new ArrayList<CategoryDTO>();

	public List<CategoryDTO> getCategoryDTOList() {
		return categoryDTOList;
	}

	public void setCategoryDTOList(List<CategoryDTO> categoryDTOList) {
		this.categoryDTOList = categoryDTOList;
	}
	
}
