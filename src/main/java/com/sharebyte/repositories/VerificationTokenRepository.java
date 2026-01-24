package com.sharebyte.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sharebyte.entities.VerificationToken;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long>{
	Optional<VerificationToken> findByToken(String token); 
}
