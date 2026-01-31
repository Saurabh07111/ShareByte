package com.sharebyte.config;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.sharebyte.security.JwtAuthFilter;

@Configuration
public class SecurityConfig {
	
	@Autowired
	public  JwtAuthFilter authFilter;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
    	http
    		.csrf(csrf-> csrf.disable())
    		.authorizeHttpRequests(
    				auth->auth.requestMatchers(
    						"/auth/login",
    						"/auth/register",
    						"/auth/verify",
    						"/file/image/*").permitAll()
    				.anyRequest().authenticated()
    		).addFilterBefore(authFilter, UsernamePasswordAuthenticationFilter.class);
    
    	return http.build();
    }
    
    
}