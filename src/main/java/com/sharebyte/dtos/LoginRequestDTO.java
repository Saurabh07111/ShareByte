package com.sharebyte.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class LoginRequestDTO {
	@Email(message = "Invalid email format")
	@NotEmpty(message="Email is required")
	private String email;
	
	@Size(min = 6, message="Password must be atleat 6 characters")
	private String password;

	
	
	public LoginRequestDTO(
			 String email,
			String password) {
		super();
		this.email = email;
		this.password = password;
	}

	public LoginRequestDTO() {
		super();
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
