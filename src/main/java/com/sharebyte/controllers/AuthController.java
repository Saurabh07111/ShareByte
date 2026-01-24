package com.sharebyte.controllers;

import com.sharebyte.services.UserService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserService userService;


    AuthController(UserService userService) {
        this.userService = userService;
    }

	
	@GetMapping("/verify")
	public ResponseEntity<String> verifyAccount(@RequestParam String token) {
			String res = userService.verifyUser(token);
			return new ResponseEntity<String>(res, HttpStatus.OK);
	}
}
