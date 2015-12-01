package com.solweaver.greetings.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.solweaver.greetings.dao.CategoryDAO;
import com.solweaver.greetings.dao.ThemeDAO;
import com.solweaver.greetings.dao.UserDAO;
import com.solweaver.greetings.dto.CategoryDTO;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.GetCategoryRequest;
import com.solweaver.greetings.dto.GetCategoryResponse;
import com.solweaver.greetings.dto.GetThemeRequest;
import com.solweaver.greetings.dto.GetThemeResponse;
import com.solweaver.greetings.model.Category;
import com.solweaver.greetings.model.Theme;
import com.solweaver.greetings.model.User;
import com.solweaver.greetings.service.IThemeService;
import com.solweaver.greetings.utils.EntityDtoUtils;
import com.solweaver.greetings.utils.GenericUtils;

@Service
public class ThemeServiceImpl implements IThemeService {

	@Autowired
	private ThemeDAO themeDAO;

	@Autowired
	private UserDAO userDAO;

	@Autowired
	private CategoryDAO categoryDAO;
	
	@Override
	@Transactional
	public GetThemeResponse getThemes(GetThemeRequest getThemeRequest) {
		GetThemeResponse getThemeResponse = new GetThemeResponse();
		User user = userDAO.findActiveUserById(getThemeRequest.getUserId());
		if (user == null) {
			GenericUtils.buildErrorDetail(getThemeResponse, GenericEnum.INVALID_USER);
			return getThemeResponse;
		}
		
		List<Theme> themeList = themeDAO.findAllActiveThemes(getThemeRequest.getCategoryId());
		
		getThemeResponse.setThemeDTOList(EntityDtoUtils.getThemeDTOList(themeList));
		
		GenericUtils.buildErrorDetail(getThemeResponse, GenericEnum.Success);

		return getThemeResponse;
	}

	@Override
	@Transactional
	public Theme getTheme(Long themeId) {
		return themeDAO.findById(themeId, false);
	}

	@Override
	@Transactional
	public GetCategoryResponse getCategories(GetCategoryRequest getCategoryRequest) {
		GetCategoryResponse getCategoryResponse = new GetCategoryResponse();
		User user = userDAO.findActiveUserById(getCategoryRequest.getUserId());
		if (user == null) {
			GenericUtils.buildErrorDetail(getCategoryResponse, GenericEnum.INVALID_USER);
			return getCategoryResponse;
		}
		
		List<Category> categoryList = categoryDAO.findAll();
		List<CategoryDTO> categoryDTOList = EntityDtoUtils.getCategoryDTOList(categoryList);
		getCategoryResponse.setCagtegoryDTOList(categoryDTOList);
		
		GenericUtils.buildErrorDetail(getCategoryResponse, GenericEnum.Success);
		return getCategoryResponse;
	}
	
}
