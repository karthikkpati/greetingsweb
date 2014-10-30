package com.solweaver.greetings.service;

import java.util.Map;


public interface IVelocityService {
	
	public void sendEmail(Map<String,Object> model,String emailTemplateName, String emailList) throws Exception;
	
}
