package com.gmail.aydinov.sergey.campaign_analytics.handler;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiError> handleTypeMismatch(org.springframework.web.method.annotation.MethodArgumentTypeMismatchException ex) {

    	  Object value = ex.getValue();

    	    List<String> incoming;

    	    if (value instanceof String[] arr) {
    	        incoming = Arrays.asList(arr);
    	    } else {
    	        incoming = List.of(String.valueOf(value));
    	    }

    	    Set<String> allowed = Arrays.stream(EventType.values())
    	            .map(Enum::name)
    	            .collect(Collectors.toSet());

    	    List<String> invalid = incoming.stream()
    	            .filter(v -> !allowed.contains(v))
    	            .toList();

    	    if (invalid.isEmpty()) {
    	        return ResponseEntity.badRequest()
    	                .body(new ApiError("Invalid request", 400));
    	    }

    	    String message = "Unknown eventTypes: " + invalid +
    	            ". Allowed values: " + allowed;

    	    return ResponseEntity.badRequest()
    	            .body(new ApiError(message, 400));
    	}
}
