package com.solweaver.greetings.service.impl;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.velocity.VelocityEngineUtils;

import com.solweaver.greetings.dao.EmailTemplateDAO;
import com.solweaver.greetings.model.EmailTemplate;
import com.solweaver.greetings.service.IVelocityService;

@Service
public class VelocityServiceImpl implements IVelocityService {

	@Autowired
	private VelocityEngine velocityEngine;
	
	@Autowired
	private EmailTemplateDAO emailTemplateDAO;
	
	public String getEmailContent(String name, Map<String, Object> model) {
		String mergedTemplate = null;
		try {
			Template emailTemplate = velocityEngine.getTemplate(name);
			StringWriter sw = new StringWriter();
			VelocityContext velocityContext = new VelocityContext();
			emailTemplate.merge(velocityContext, sw);

			mergedTemplate = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, emailTemplate.getName(), "ISO-8859-1", model);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mergedTemplate;
	}
	
	@Override
	@Transactional
	public void sendEmail(Map<String, Object> model, String emailTemplateName,String email) throws Exception {
		List<String> emailList = new ArrayList<String>();
		emailList.add(email);
		sendEmail(model, emailTemplateName, emailList);
	}
	
	@Override
	@Transactional
	public void sendEmail(Map<String, Object> model, String emailTemplateName,List<String> emailList) throws Exception {
			
		EmailTemplate emailTemplate = emailTemplateDAO.getEmailTemplateByName(emailTemplateName);
		if(emailTemplate != null ){
			if(emailTemplate.isActive()){
				
				String emailContent = getEmailContent(emailTemplateName, model);
				Properties props = new Properties();
				props.put("mail.smtp.host", "smtp.mail.yahoo.com");
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.port", "587");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.auth", "true");
				Session session = Session.getDefaultInstance(props, new javax.mail.Authenticator() {
					protected PasswordAuthentication getPasswordAuthentication() {
						//return new PasswordAuthentication("solgrite@gmail.com", "Greetings@123");
						return new PasswordAuthentication("greetings.invitations@yahoo.com", "Greetings@123");
					}
				  });
				MimeMessage message = new MimeMessage(session);
				try {
					message.setFrom(new InternetAddress(emailTemplate.getFromEmail()));
					for(String email : emailList){
						message.setRecipients(Message.RecipientType.TO,
								InternetAddress.parse(email));
					}
					message.setSubject(emailTemplate.getSubject());
					message.setContent(emailContent, "text/html");
					Transport.send(message);
				} catch (AddressException e) {
					e.printStackTrace();
					throw new Exception(e);
				} catch (MessagingException e) {
					e.printStackTrace();
					throw new Exception(e);
				}
			}
		}else{
			throw new Exception("Invalid Email Template: "+ emailTemplateName);
		}
	}
}
