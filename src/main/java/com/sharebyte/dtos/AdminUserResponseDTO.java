package com.sharebyte.dtos;

public class AdminUserResponseDTO {
	private Long id;
	private String name;
	private String email;
	private String role;
	private String status;
	
	
	
	public AdminUserResponseDTO(Long id, String name, String role, String status, String email) {
		super();
		this.id = id;
		this.name = name;
		this.role = role;
		this.status = status;
		this.email= email;
	}
	
	
	


	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	
}

