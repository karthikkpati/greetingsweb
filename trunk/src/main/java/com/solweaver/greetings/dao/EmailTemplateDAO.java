package com.solweaver.greetings.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;

import com.solweaver.greetings.model.EmailTemplate;

@Repository
public class EmailTemplateDAO extends BaseDAO<EmailTemplate, Long > {

	public EmailTemplate getEmailTemplateByName(String templateName){
		EmailTemplate emailTemplate = null;
		List<EmailTemplate> emailTemplateList = null;
		emailTemplateList = findByCriteria(Restrictions.and(Restrictions.eq("templateName", templateName), Restrictions.eq("active", true)));
		if(emailTemplateList != null && emailTemplateList.size() > 0){
			emailTemplate = emailTemplateList.get(0);
		}
		return emailTemplate;
	}
}
