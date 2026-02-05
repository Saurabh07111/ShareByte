package com.sharebyte.services;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ProblemDetail;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.sharebyte.dtos.ChangePasswordRequestDTO;
import com.sharebyte.dtos.ForgotPasswordRequestDTO;
import com.sharebyte.dtos.LoginRequestDTO;
import com.sharebyte.dtos.LoginResponseDTO;
import com.sharebyte.dtos.RegisterRequestDTO;
import com.sharebyte.dtos.RegisterResponseDTO;
import com.sharebyte.dtos.UpdateProfileDTO;
import com.sharebyte.dtos.UserProfileResponseDTO;
import com.sharebyte.entities.User;
import com.sharebyte.entities.VerificationToken;
import com.sharebyte.enums.Role;
import com.sharebyte.enums.UserStatus;

import com.sharebyte.exceptions.AccountNotActiveException;
import com.sharebyte.exceptions.EmailAlreadyExistsException;
import com.sharebyte.exceptions.InvalidTokenException;
import com.sharebyte.exceptions.UserNotFoundException;

import com.sharebyte.repositories.UserRepository;
import com.sharebyte.repositories.VerificationTokenRepository;
import com.sharebyte.security.JwtUtil;

import jakarta.validation.Valid;

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
	
	public void updateProfile(String email, UpdateProfileDTO dto, MultipartFile profileImage) {
		User user = userRepo.findByEmail(email);
		
		if(user==null) {
			throw new UserNotFoundException("User not found");
		} 
		
		if(dto!=null && dto.getName()!=null) {
			user.setName(dto.getName());
		}
		
		if(profileImage!=null && !profileImage.isEmpty()) {
			
			String fileName = profileImage.getOriginalFilename();
			String fname = user.getId() + "_profile" + fileName.substring(fileName.lastIndexOf('.'));
			
			saveFile(profileImage,  fname);
			
			user.setProfileImage(fname);
		}
		
		userRepo.save(user);
		
		
	}

	
	public UserProfileResponseDTO getMyProfile() {
		String email = SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();
		
		User user = userRepo.findByEmail(email);
		
		if(user == null) {
			throw new UserNotFoundException("User not found");
		}
		
		
		return mapToprofileDto(user);
	}

	
	public  UserProfileResponseDTO getUserProfile(Long userId) {
		
		User user = userRepo.findById(userId).orElseThrow(null);
		
		if(user==null) {
			throw new UserNotFoundException("User not found");
		}
		
		return mapToprofileDto(user);
	}
	
	public UserProfileResponseDTO mapToprofileDto(User user) {
		UserProfileResponseDTO dto = new UserProfileResponseDTO();
		dto.setEmail(user.getEmail());
		dto.setId(user.getId());
		dto.setName(user.getName());
		dto.setRole(user.getRole().name());
		dto.setImage(user.getProfileImage());
		
		return dto;
		
	}
	
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
		user.setStatus(UserStatus.ACTIVE);
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
		
		if(user.getStatus() != UserStatus.ACTIVE) {
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
		user.setRole(Role.valueOf(request.getRole()));
//		user.setPassword(request.getPassword());
		
		
		user.setStatus(UserStatus.ACTIVE);
		
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
	

	public void uploadImage(MultipartFile image) {
		String email = SecurityContextHolder.getContext()
				.getAuthentication()
				.getName();
		
		User user = userRepo.findByEmail(email);

		if(user == null) {
			throw new UserNotFoundException("User does not exists");
		}
		
		String fileName = image.getOriginalFilename();
		String fname = user.getId() + "_profile" + fileName.substring(fileName.lastIndexOf('.'));
		 saveFile(image,  fname);
		
		user.setProfileImage(fname);
		
		userRepo.save(user);
		
	}
	
	public void saveFile(MultipartFile file, String fileName)  {
		Path uploadPath = Paths.get("uploads/profile");
		Path filePath = uploadPath.resolve(fileName);
		try {
			Files.createDirectories(uploadPath);
			
			Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
			
			
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		
		
	}


	public boolean changePassword(ChangePasswordRequestDTO request) {
		// TODO Auto-generated method stub
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		User user = userRepo.findByEmail(email);
		
		if(passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
			user.setPassword(passwordEncoder.encode(request.getNewPassword()));
			userRepo.save(user);
			return true;
		} else {
			return false;
		}
		
	}


	public void forgotPassword(ForgotPasswordRequestDTO request) {
		// TODO Auto-generated method stub
		
		
		
	}


	
	
}
