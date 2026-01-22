package com.sharebyte.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> handleValidationException (MethodArgumentNotValidException exception){
		Map<String, String> fieldErrors = new HashMap<>();
		
		exception.getBindingResult()
			.getFieldErrors()
			.forEach(
					error->fieldErrors.put(error.getField(), error.getDefaultMessage()
							));
		 Map<String, Object> response = new HashMap<>();
	        response.put("status", HttpStatus.BAD_REQUEST.value());
	        response.put("errors", fieldErrors);
	        
		return new ResponseEntity<Map<String,Object>>(response, HttpStatus.BAD_REQUEST);	
		
	}
	
	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<Map<String, Object>> handleEmailExists (EmailAlreadyExistsException ex) {
		 Map<String, Object> response = new HashMap<>();
		    response.put("status", HttpStatus.CONFLICT.value());
		    response.put("message", ex.getMessage());
		    
		    return new ResponseEntity<Map<String,Object>>(response, HttpStatus.CONFLICT);
	}
}
