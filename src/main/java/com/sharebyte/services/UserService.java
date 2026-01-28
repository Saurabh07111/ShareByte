package com.sharebyte.services;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.sharebyte.dtos.LoginRequestDTO;
import com.sharebyte.dtos.LoginResponseDTO;
import com.sharebyte.dtos.RegisterRequestDTO;
import com.sharebyte.dtos.RegisterResponseDTO;

import com.sharebyte.entities.User;
import com.sharebyte.entities.VerificationToken;
import com.sharebyte.enums.UserStatus;

import com.sharebyte.exceptions.AccountNotActiveException;
import com.sharebyte.exceptions.EmailAlreadyExistsException;
import com.sharebyte.exceptions.InvalidTokenException;
import com.sharebyte.exceptions.UserNotFoundException;

import com.sharebyte.repositories.UserRepository;
import com.sharebyte.repositories.VerificationTokenRepository;
import com.sharebyte.security.JwtUtil;

@Service
public class UserService {
	
	@Autowired 
	private JwtUtil jwtUtil;
	
	@Autowired
	private EmailService emailService;
	
	@Autowired
	private UserRepository userRepo;
	
	@Autowired
	private VerificationTokenRepository verificationRepo;
	
	@Autowired 
	private PasswordEncoder passwordEncoder;
	
	
	public String verifyUser(String token) {
		Optional<VerificationToken> op  = verificationRepo.findByToken(token);
		if(op.isEmpty() || !op.isPresent())  {
			throw new InvalidTokenException("Verification link is invalid");
		}
		VerificationToken verificationToken = op.get();
		
		if(verificationToken.getExpiryDate().isBefore(LocalDateTime.now())) {
			throw new InvalidTokenException("Verification link is expired");
		}
		
		User user = verificationToken.getUser();
		user.setStatus(UserStatus.ACITVE);
		userRepo.save(user);
		verificationRepo.delete(verificationToken);
		return "Account Varified";
		
	}
	
	public LoginResponseDTO login (LoginRequestDTO request) {
		User user = userRepo.findByEmail(request.getEmail());
		
		if(user==null) {
			throw new UserNotFoundException("User not found");
		}
		
		if(!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
			throw new UserNotFoundException("Invalid credentials");
		}
		
//		if(!user.getPassword().equals(request.getPassword())) {
//			throw new UserNotFoundException("Invalid credentials");
//		}
		
		if(user.getStatus() != UserStatus.ACITVE) {
			throw new AccountNotActiveException("Please varify your email before using");
		}
		
		String token = jwtUtil.generateToken(user.getEmail());
				
		LoginResponseDTO response = new LoginResponseDTO();
		
		response.setEmail(user.getEmail());
		response.setToken(token);
		response.setMessage("Login Successful");
		response.setRole(user.getRole());
		
		return response;
	}
	
	public RegisterResponseDTO register(RegisterRequestDTO request) {
		if(userRepo.existsByEmail(request.getEmail())) {
			throw new EmailAlreadyExistsException("Email already exists");
			
		}
		
		User user = new User();
		user.setName(request.getName());
		user.setEmail(request.getEmail());
		user.setPassword(passwordEncoder.encode(request.getPassword()));
		user.setRole(request.getRole());
//		user.setPassword(request.getPassword());
		
		
		user.setStatus(UserStatus.PANDING_VARIFICATION);
		
		user = userRepo.save(user);
		
		String token  = UUID.randomUUID().toString();
		VerificationToken verificationToken = new VerificationToken();
		verificationToken.setToken(token);
		verificationToken.setUser(user);
		verificationToken.setExpiryDate(LocalDateTime.now().plusHours(24));
		
		verificationRepo.save(verificationToken);
		
		String verificationLink = "http://localhost:8282/auth/verify?token=" + token;
		
		// email sending ..................................########################################
//		emailService.sendMail(user.getEmail(), "Verify your ShareByte account",  "Thank you for registering.\nPlease verify your email to activate your account. \n"  + verificationLink);
		
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
