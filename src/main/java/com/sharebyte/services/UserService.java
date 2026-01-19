package com.sharebyte.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.sharebyte.entities.User;
import com.sharebyte.repositories.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public boolean save(User user) {
		boolean flag =  false;
		// save user code 
		return flag;
	}
}
