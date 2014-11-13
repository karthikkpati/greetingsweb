package com.solweaver.greetings.dto;

import java.util.List;

public class GetCategoryResponse extends BaseResponse{

	private List<CategoryDTO> cagtegoryDTOList;

	public List<CategoryDTO> getCagtegoryDTOList() {
		return cagtegoryDTOList;
	}

	public void setCagtegoryDTOList(List<CategoryDTO> cagtegoryDTOList) {
		this.cagtegoryDTOList = cagtegoryDTOList;
	}

}
