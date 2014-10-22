package com.solweaver.greetings.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.Theme;

@Repository
public class ThemeDAO extends BaseDAO<Theme, Long > {

	public List<Theme> findAllActiveThemes() {
		List<Theme> themeList = null;
		themeList = findByCriteria(Restrictions.eq("active", true));
		return themeList;
	}

}
