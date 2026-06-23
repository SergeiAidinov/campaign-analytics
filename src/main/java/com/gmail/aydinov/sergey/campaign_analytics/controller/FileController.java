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

	@Operation(summary = "Upload actions file")
	@PostMapping(value = "/actions/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadActions(@RequestParam("file") MultipartFile file) {
	    dataStore.collectDataFromEventFileAsync(file)
	            .exceptionally(ex -> {
	                System.err.println("Ошибка: " + ex.getMessage());
	                return null;
	            });

	    return ResponseEntity.ok("Файл принят. Парсинг запущен в фоне.");
	}
	
	@Operation(summary = "Upload views file")
	@PostMapping(value = "/views/upload", consumes = "multipart/form-data")
	public ResponseEntity<String> uploadViews(
	        @RequestParam("file") MultipartFile file
	) {
		 dataStore.collectDataFromViewsFileAsync(file)
         .exceptionally(ex -> {
             System.err.println("Ошибка: " + ex.getMessage());
             return null;
         });
	    return ResponseEntity.ok("Views file received: " + file.getOriginalFilename());
	}
}
