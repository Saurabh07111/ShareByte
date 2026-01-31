package com.sharebyte.dtos;

import com.sharebyte.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public class RegisterRequestDTO {
	
	@NotEmpty(message="Name is required")
	private String name;
	
	@Email(message="Invalid email format")
	@NotEmpty(message="Email is required")
	private String email;
	
	@Size(min = 6, message = "Password must be atleat 6 characters")
	private String password;
	
	@NotEmpty(message="Role is required")
	private String role;
	
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	
}
