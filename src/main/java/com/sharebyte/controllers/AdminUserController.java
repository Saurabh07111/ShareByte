package com.sharebyte.controllers;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sharebyte.dtos.AdminUserResponseDTO;
import com.sharebyte.enums.Role;
import com.sharebyte.enums.UserStatus;
import com.sharebyte.services.AdminUserService;
import com.sharebyte.services.UserService;

@RestController
@RequestMapping("/admin/users")
public class AdminUserController {
	
	@Autowired
	AdminUserService adminUserService;
	
	@Autowired
	private UserService userService;
	
	@PutMapping("/{userId}/{status}")
	public ResponseEntity<String> updateUserStatus(@PathVariable Long userId, @PathVariable String status) throws BadRequestException {
		
		adminUserService.updateUserStatus(userId, status);
		return ResponseEntity.ok("User Status updated successfully");
	}
	
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
