package com.sharebyte.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
	
	@Value("${jwt.secret}") 
	private String secret;

	@Value("${jwt.expiration}") 
	private Long expiration;
	
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder()
			.setSigningKey(getSigningKey())
			.build()
			.parseClaimsJws(token);
			
			return true;
		} catch (Exception e) {
			System.out.println(e);
			return false;
		}
	}
	
	private SecretKeySpec getSigningKey() {
		return new SecretKeySpec(
				secret.getBytes(StandardCharsets.UTF_8),
				SignatureAlgorithm.HS256.getJcaName()
				);
	}
	
	
	
	public String extractEmail(String token) {
		return Jwts.parserBuilder()
				.setSigningKey(getSigningKey())
				.build()
				.parseClaimsJws(token)
				.getBody()
				.getSubject();
	}
	
	public String generateToken(String email) {


		return Jwts.builder()
				.setSubject(email)
				.setIssuedAt(new Date())
				.setExpiration(new Date(System.currentTimeMillis() + expiration))
				.signWith(getSigningKey(), SignatureAlgorithm.HS256)
				.compact();
	}

}
