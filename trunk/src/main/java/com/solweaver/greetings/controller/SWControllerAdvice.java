package com.solweaver.greetings.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.solweaver.greetings.dto.BaseResponse;
import com.solweaver.greetings.dto.ErrorDetail;

@ControllerAdvice
public class SWControllerAdvice {

	@Autowired
	private MessageSource messageSource;
	 
	@ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public BaseResponse processValidationError(MethodArgumentNotValidException ex) {
		BaseResponse baseResponse = new BaseResponse();
	 	BindingResult result = ex.getBindingResult();
	 	List<FieldError> fieldErrors = result.getFieldErrors();
	 	baseResponse.setErrorDetailList(processFieldErrors(fieldErrors));
        return baseResponse;
    }
 
    private List<ErrorDetail> processFieldErrors(List<FieldError> fieldErrors) {
    	List<ErrorDetail> errorDetailList = new ArrayList<ErrorDetail>();
    	
        for (FieldError fieldError: fieldErrors) {
        	ErrorDetail errorDetail = new ErrorDetail();
        	String localizedErrorMessage = resolveLocalizedErrorMessage(fieldError);
            errorDetail.setMessage(localizedErrorMessage);
            errorDetailList.add(errorDetail);
        }
 
        return errorDetailList;
    }
 
    private String resolveLocalizedErrorMessage(FieldError fieldError) {
        Locale currentLocale =  LocaleContextHolder.getLocale();
        String localizedErrorMessage = messageSource.getMessage(fieldError, currentLocale);
 
        return localizedErrorMessage;
    }
}
