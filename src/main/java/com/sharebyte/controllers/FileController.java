package com.sharebyte.controllers;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("file")
public class FileController {
	
	@GetMapping("/image/{filePath}")
	public ResponseEntity<Resource> getProfileImage(@PathVariable String filePath) {
		
		try {
			Path path =Paths.get("uploads/profile/" +filePath);			
			Resource resource = new UrlResource( path.toUri());
			
			if(!resource.exists()) {
				throw new IOException();
			}
			
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(resource);
		} catch(IOException e) {
			return ResponseEntity.notFound().build();
		}
	}
	

}
