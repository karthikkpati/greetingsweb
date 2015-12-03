package com.solweaver.greetings.dto;

import java.util.List;
import java.util.Map;

public class GetThemeResponse extends BaseResponse{

	Map<CategoryDTO, List<ThemeDTO>> themeCategoryMap;

	public Map<CategoryDTO, List<ThemeDTO>> getThemeCategoryMap() {
		return themeCategoryMap;
	}

	public void setThemeCategoryMap(
			Map<CategoryDTO, List<ThemeDTO>> themeCategoryMap) {
		this.themeCategoryMap = themeCategoryMap;
	}

}
