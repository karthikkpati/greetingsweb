package com.solweaver.greetings.utils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.solweaver.greetings.dto.BaseResponse;
import com.solweaver.greetings.dto.ErrorDetail;
import com.solweaver.greetings.dto.EventErrorEnum;
import com.solweaver.greetings.dto.GenericEnum;
import com.solweaver.greetings.dto.UserDTO;
import com.solweaver.greetings.model.User;

public class GenericUtils {

	public static void buildErrorDetail(BaseResponse baseResponse, EventErrorEnum inviteErrorEnum){
		List<ErrorDetail> errorDetailList = baseResponse.getErrorDetailList();
		if(errorDetailList == null){
			errorDetailList = new ArrayList<ErrorDetail>();
		}
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setCode(inviteErrorEnum.code);
		errorDetail.setMessage(inviteErrorEnum.message);
		errorDetailList.add(errorDetail);
		baseResponse.setErrorDetailList(errorDetailList);
	}
	
	public static void buildErrorDetail(BaseResponse baseResponse, GenericEnum genericEnum){
		buildErrorDetail(baseResponse, genericEnum, null);
	}
	
	public static void buildErrorDetail(BaseResponse baseResponse, GenericEnum genericEnum, String message){
		
		List<ErrorDetail> errorDetailList = baseResponse.getErrorDetailList();
		if(errorDetailList == null){
			errorDetailList = new ArrayList<ErrorDetail>();
		}
		ErrorDetail errorDetail = new ErrorDetail();
		errorDetail.setCode(genericEnum.code);
		if(message != null){
			errorDetail.setMessage(message);
		}else{
			errorDetail.setMessage(genericEnum.message);
		}
		errorDetailList.add(errorDetail);
		baseResponse.setErrorDetailList(errorDetailList);
	}
	
	public static boolean isSuccess(List<ErrorDetail> errorDetailList){
		boolean result = false;
		if(errorDetailList == null || (errorDetailList.size() == 1 && errorDetailList.get(0).getCode().equals(GenericEnum.Success.code))){
			result = true;
		}
		return result;
	}
	
	public static boolean isSuccess(BaseResponse baseResponse){
		return isSuccess(baseResponse.getErrorDetailList());
	}

	public static Date converToDate(String date) throws Exception{
		
		
		/*Date eventDate = null;
		if(date != null){
			SimpleDateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy");
			try{
				eventDate = dateFormat.parse(date);
			}catch(Exception exception){
				exception.printStackTrace();
				throw exception;
			}
		}
		return eventDate;*/
		
		String epochString = date;
		long epoch = Long.parseLong( epochString );
		Date expiry = new Date( epoch );
		//String targetDate = expiry.toString();
		
		Date eventDate = null;
		if(date != null){
			//SimpleDateFormat dateFormat = new SimpleDateFormat("mm-dd-yyyy");
			try{
				eventDate = expiry;//dateFormat.parse(targetDate); //expiry;
				}catch(Exception exception){
				exception.printStackTrace();
				throw exception;
			}
		}
		return eventDate;
	}

	public static UserDTO convertToUserDTO(User user) {
		UserDTO userDTO = null;
		if(user != null){
			userDTO = new UserDTO();
			userDTO.setEmailId(user.getEmail());
			userDTO.setFirstName(user.getFirstName());
			userDTO.setLastName(user.getLastName());
			userDTO.setUserId(user.getId());
			userDTO.setUserStatus(user.getUserStatus().name());
		}
		return userDTO;
	}
	
}
