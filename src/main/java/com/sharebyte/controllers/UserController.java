package com.sharebyte.controllers;

import com.sharebyte.dtos.UpdateProfileDTO;
import com.sharebyte.dtos.UserProfileResponseDTO;
import com.sharebyte.repositories.UserRepository;
import com.sharebyte.services.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/user")
public class UserController {


	@Autowired
   private UserService userService;
	
	@PutMapping("/profile")
	public ResponseEntity<?> updateProfile(
			@RequestPart(required = false) UpdateProfileDTO dto,
			@RequestPart(required = false) MultipartFile profileImage
			, Authentication authentication) {
		
		userService.updateProfile(authentication.getName(), dto, profileImage);
		return ResponseEntity.ok("Profile updated successfully");
		
	}
	
	@GetMapping("/me")
	public ResponseEntity<UserProfileResponseDTO> getMyProfile() {
		return ResponseEntity.ok(userService.getMyProfile());
	}
	
	@GetMapping("/{userId}/profile")
	public ResponseEntity<UserProfileResponseDTO > getUserProfile(@PathVariable Long userId) {
		
		return ResponseEntity.ok(userService.getUserProfile(userId));
	}
	
	
	@PostMapping("/upload-image")
	public ResponseEntity<String> uploadImage(@RequestParam("image") MultipartFile image) {
		if(image == null) {
			return new ResponseEntity("Image not found" , HttpStatus.NOT_ACCEPTABLE);
		}
		
		userService.uploadImage(image) ;
		
		return new ResponseEntity<String>("Image uploaded successfully", HttpStatus.OK);
	}		

}
