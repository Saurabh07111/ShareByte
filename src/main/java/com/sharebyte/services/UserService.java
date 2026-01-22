package com.sharebyte.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharebyte.dtos.RegisterRequestDTO;
import com.sharebyte.dtos.RegisterResponseDTO;
import com.sharebyte.entities.User;
import com.sharebyte.enums.UserStatus;
import com.sharebyte.exceptions.EmailAlreadyExistsException;
import com.sharebyte.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	private UserRepository userRepo;
	
	public RegisterResponseDTO register(RegisterRequestDTO request) {
		if(userRepo.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("Email already exists");
			
		}
		
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(request.getPassword());
		user.setRole(request.getRole());
		
		user.setStatus(UserStatus.PANDING_VARIFICATION);
		
		userRepo.save(user);
		
		RegisterResponseDTO response = new RegisterResponseDTO();
		response.setEmail(user.getEmail());
		response.setStatus(user.getStatus().name());
		response.setMessage("Registration successful. Please verify your email to activate your account.");
		
		return response;
		
	}
	
	public User getUserByEmail(String email) {
		return userRepo.findByEmail(email);
	}
	
}
