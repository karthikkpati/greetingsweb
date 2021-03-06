package com.solweaver.greetings.service;

import com.solweaver.greetings.dto.GetCategoryRequest;
import com.solweaver.greetings.dto.GetCategoryResponse;
import com.solweaver.greetings.dto.GetThemeRequest;
import com.solweaver.greetings.dto.GetThemeResponse;
import com.solweaver.greetings.model.Theme;

public interface IThemeService {
	
	public GetThemeResponse getThemes(GetThemeRequest getThemeRequest);

	public Theme getTheme(Long themeId);

	public GetCategoryResponse getCategories(GetCategoryRequest getCategoryRequest);

}
