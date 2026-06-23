package com.gmail.aydinov.sergey.campaign_analytics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "File API")
@RestController
@RequestMapping("/files")
public class FileController {

	@Operation(summary = "Upload actions file")
	@PostMapping(value = "/actions/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadActions(
	        @RequestParam("file") MultipartFile file
	) {
	    return ResponseEntity.ok("Actions file received: " + file.getOriginalFilename());
	}
	
	@Operation(summary = "Upload views file")
	@PostMapping(value = "/views/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadViews(
	        @RequestParam("file") MultipartFile file
	) {
	    return ResponseEntity.ok("Views file received: " + file.getOriginalFilename());
	}
}
