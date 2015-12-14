package com.solweaver.greetings.dto;

import java.util.List;

public class DashboardNotificationResponse extends BaseResponse{

	private List<DashboardNotificationDTO> dashboardNotificationDTOList;

	public List<DashboardNotificationDTO> getDashboardNotificationDTOList() {
		return dashboardNotificationDTOList;
	}

	public void setDashboardNotificationDTOList(
			List<DashboardNotificationDTO> dashboardNotificationDTOList) {
		this.dashboardNotificationDTOList = dashboardNotificationDTOList;
	}
	
}
