package com.solweaver.greetings.service;

import com.solweaver.greetings.dto.GetThemeRequest;
import com.solweaver.greetings.dto.GetThemeResponse;

public interface IThemeService {
	
	public GetThemeResponse getThemes(GetThemeRequest getThemeRequest);
	
}
