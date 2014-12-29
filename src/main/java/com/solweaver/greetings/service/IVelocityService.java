package com.solweaver.greetings.service;

import java.util.List;
import java.util.Map;


public interface IVelocityService {
	
	public void sendEmail(Map<String,Object> model,String emailTemplateName, List<String> emailList) throws Exception;

	public void sendEmail(Map<String, Object> model, String emailTemplateName,
			String email) throws Exception;
	
}
