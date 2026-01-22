package com.sharebyte.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sharebyte.dtos.RegisterRequestDTO;
import com.sharebyte.dtos.RegisterResponseDTO;

import com.sharebyte.services.UserService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
    private UserService userService;
	
	@PostMapping("/register")
	public ResponseEntity<RegisterResponseDTO> registerUser(@Valid @RequestBody RegisterRequestDTO request) {
		RegisterResponseDTO response = userService.register(request);
		
		if(response == null	) {
			RegisterResponseDTO error = new RegisterResponseDTO();
			error.setEmail(request.getEmail());
			error.setMessage("Email already exists");
			error.setStatus("FAILED");
			
			return new ResponseEntity<>(error, HttpStatus.CONFLICT);
		}
		return new ResponseEntity<>(response, HttpStatus.CREATED);
		
		
	}
	
	
  
}
