package com.sharebyte.services;

import org.apache.coyote.BadRequestException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.sharebyte.dtos.AdminUserResponseDTO;
import com.sharebyte.entities.User;
import com.sharebyte.enums.Role;
import com.sharebyte.enums.UserStatus;
import com.sharebyte.exceptions.UserNotFoundException;
import com.sharebyte.repositories.UserRepository;

@Service
public class AdminUserService {
	
	@Autowired
	private UserRepository userRepo;
	
	public void updateUserStatus(Long userId, String status) throws BadRequestException  {
		// TODO Auto-generated method stub
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User loggedInUser = userRepo.findByEmail(email);
		
		if(loggedInUser.getRole().name()!="ROLE_ADMIN") {
			throw new AccessDeniedException("You can not update user status");
		}
		if(loggedInUser.getId() == userId) {
			throw new BadRequestException("You can not block your self");
		}
		
		
		User user = userRepo.findById(userId).orElse(null);
		
		if(user==null) {
			throw new UserNotFoundException("User not found");
		}
		
		
		user.setStatus(UserStatus.valueOf(status));
		userRepo.save(user);
	}
		
		
	
	
	public Page<AdminUserResponseDTO> getUsers(
			int page,
			int size,
			String sortBy,
			String sortDir,
			String role,
			String status) {
		String email = SecurityContextHolder.getContext().getAuthentication().getName();
		
		User loggedInUser = userRepo.findByEmail(email);
		
		if(loggedInUser.getRole().name()!="ROLE_ADMIN") {
//			System.out.println("######");
			throw new AccessDeniedException("You can not update user status");
		}
		Sort sort = sortDir.equals("desc")?Sort.by(sortBy).descending():Sort.by(sortBy).ascending();
		
		Pageable pageable = PageRequest.of(page, size, sort);
		
		Page<User> users;
		
		if(role!=null && status!=null) {
				users = userRepo.findByRoleAndStatus(Role.valueOf(role), UserStatus.valueOf(status), pageable);
		} else if(role !=null)  {
			users = userRepo.findByRole(Role.valueOf(role), pageable);
		} else if(status!=null) {
			users = userRepo.findByStatus(UserStatus.valueOf(status), pageable);
		} else {
			users = userRepo.findAll(pageable);
		}
		
		return users.map(user-> 
				new AdminUserResponseDTO(
					user.getId(), 
					user.getName(), 
					user.getRole().name(),
					user.getStatus().name(),
					user.getEmail())
				);
	}


}
