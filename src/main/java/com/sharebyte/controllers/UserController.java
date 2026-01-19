package com.sharebyte.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sharebyte.entities.User;
import com.sharebyte.services.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    public ResponseEntity<String> registerUser(@RequestBody User user) {
        // registration code
        if(user!=null) {
           boolean flag =  userService.save(user);
            if(flag) {
                return new ResponseEntity("User registered successfully", HttpStatus.OK);
            } else {
                return new ResponseEntity<>("User registration failed", HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }
        return  return new ResponseEntity<>("Invalid user data", HttpStatus.BAD_REQUEST);
    }
}
