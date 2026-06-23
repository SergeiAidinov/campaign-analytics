package com.gmail.aydinov.sergey.campaign_analytics.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.gmail.aydinov.sergey.campaign_analytics.service.DataStore;

@Tag(name = "File API")
@RestController
@RequestMapping("/files")
public class FileController {
	
	private final DataStore dataStore;
	
	public FileController(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	@Operation(summary = "Upload events file")
	@PostMapping(value = "/events/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadEvents(
	        @RequestParam("file") MultipartFile file) {
	    dataStore.loadEventsAsync(file);
	    return ResponseEntity.accepted()
	            .body("Events file accepted. Parsing started.");
	}

	@Operation(summary = "Upload impressions file")
	@PostMapping(value = "/impressions/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadImpressions(
	        @RequestParam("file") MultipartFile file) {
	    dataStore.loadImpressionsAsync(file);
	    return ResponseEntity.accepted()
	            .body("Impressions file accepted. Parsing started.");
	}
}
