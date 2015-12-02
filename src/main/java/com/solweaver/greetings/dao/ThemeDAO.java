package com.solweaver.greetings.dao;

import java.util.List;

import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.Theme;

@Repository
public class ThemeDAO extends BaseDAO<Theme, Long > {

	public List<Theme> findAllActiveThemes(Long categoryId) {
		List<Theme> themeList = null;
		Criterion criteria = Restrictions.eq("active", true);
		if(categoryId != null){
			criteria = Restrictions.and(criteria, Restrictions.eq("category.id", categoryId));
		}
		themeList = findByCriteria("category.id", null, criteria);
		return themeList;
	}

}
