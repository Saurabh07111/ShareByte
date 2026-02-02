package com.sharebyte.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sharebyte.entities.User;

import com.sharebyte.enums.Role;
import com.sharebyte.enums.UserStatus;



@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	User findByEmail(String email);
	boolean existsByEmail(String email);
	
	Page<User>findByRole(Role role, Pageable pageable);
	
	Page<User> findByStatus(UserStatus status, Pageable pageable);
	
	Page<User> findByRoleAndStatus(Role role, UserStatus status, Pageable pageable);
}
