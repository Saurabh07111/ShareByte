package com.sharebyte.dtos;

import jakarta.validation.constraints.Size;


public class ChangePasswordRequestDTO {
	@Size(min = 6, message = "Password must be atleat 6 characters")
	private String oldPassword;
	@Size(min = 6, message = "Password must be atleat 6 characters")
	private String newPassword;
	
	
	public String getOldPassword() {
		return oldPassword;
	}
	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	
}
