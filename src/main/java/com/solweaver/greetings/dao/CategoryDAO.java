package com.solweaver.greetings.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.Category;

@Repository
public class CategoryDAO extends BaseDAO<Category, Long > {

	public Category findByName(String categoryName){
		Category category = null;
		List<Category> categoryList = null;
		categoryList = findByCriteria(Restrictions.eq("categoryName",categoryName));
		
		if(categoryList != null && categoryList.size() > 0){
			category = categoryList.get(0);
		}
		return category;
	
	}
}
