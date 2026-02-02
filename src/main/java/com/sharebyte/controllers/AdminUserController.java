package com.sharebyte.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharebyte.dtos.AdminUserResponseDTO;
import com.sharebyte.enums.Role;
import com.sharebyte.enums.UserStatus;
import com.sharebyte.services.AdminUserService;

@RestController
@RequestMapping("/admin/users")
@PreAuthorize("hasRole('ROLE_ADMIN')")
public class AdminUserController {
	
	@Autowired
	AdminUserService adminUserService;
	
	public Response
	
	@GetMapping
	public ResponseEntity<Page<AdminUserResponseDTO>> getAllUsers(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "id") String  sortBy,
			@RequestParam(defaultValue = "desc") String sortDir,
			@RequestParam(required = false) String role,
			@RequestParam(required = false) String status
			){
		
		return ResponseEntity.ok(
					adminUserService.getUsers(page, size, sortBy, sortDir, role, status)
				);
	}
	
}
