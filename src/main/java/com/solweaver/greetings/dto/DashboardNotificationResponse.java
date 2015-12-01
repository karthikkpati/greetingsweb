package com.solweaver.greetings.dto;

import java.util.List;

public class DashboardNotificationResponse extends BaseResponse{

	private List<DashboardNotificationDTO> inviterDashboardNotificationDTOList;
	
	private List<DashboardNotificationDTO> inviteeDashboardNotificationDTOList;

	private List<DashboardNotificationDTO> recipientDashboardNotificationDTOList;

	public List<DashboardNotificationDTO> getInviterDashboardNotificationDTOList() {
		return inviterDashboardNotificationDTOList;
	}

	public void setInviterDashboardNotificationDTOList(
			List<DashboardNotificationDTO> inviterDashboardNotificationDTOList) {
		this.inviterDashboardNotificationDTOList = inviterDashboardNotificationDTOList;
	}

	public List<DashboardNotificationDTO> getInviteeDashboardNotificationDTOList() {
		return inviteeDashboardNotificationDTOList;
	}

	public void setInviteeDashboardNotificationDTOList(
			List<DashboardNotificationDTO> inviteeDashboardNotificationDTOList) {
		this.inviteeDashboardNotificationDTOList = inviteeDashboardNotificationDTOList;
	}

	public List<DashboardNotificationDTO> getRecipientDashboardNotificationDTOList() {
		return recipientDashboardNotificationDTOList;
	}

	public void setRecipientDashboardNotificationDTOList(
			List<DashboardNotificationDTO> recipientDashboardNotificationDTOList) {
		this.recipientDashboardNotificationDTOList = recipientDashboardNotificationDTOList;
	}
	
}
