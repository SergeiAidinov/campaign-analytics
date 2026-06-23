package com.gmail.aydinov.sergey.campaign_analytics.controller;

import com.fasterxml.jackson.databind.util.EnumValues;
import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.TimeSeriesDto;
import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;
import com.gmail.aydinov.sergey.campaign_analytics.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Tag(name = "Analytics API")
@RestController
@RequestMapping("/api/campaign")
public class AnalyticsController {

    private final CampaignService service;
    
//     private final Set<String> allowed = Arrays.stream(EventType.values())
//            .map(Enum::name)
//            .collect(Collectors.toSet());

    public AnalyticsController(CampaignService service) {
        this.service = service;
    }

    @GetMapping("/timeseries")
    @Operation(summary = "График метрик по времени")
    public ResponseEntity<List<TimeSeriesDto>> getTimeSeries(
    		
            @Parameter(
                    description = "Типы событий. Возможные значения: content, fclick, misc, registration, signup, vcontent, vlead, vregistration, vmisc, vsignup",
                    array = @ArraySchema(
                            schema = @io.swagger.v3.oas.annotations.media.Schema(
                                    implementation = String.class
                            )
                    )
            )
            @RequestParam(required = false)
            List<EventType> eventTypes,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate to
    ) {
    	
//    	List<String> invalid = eventTypes.stream()
//    	        .filter(e -> !allowed.contains(e))
//    	        .map(Enum::name)
//    	        .toList();
//
//    	if (!invalid.isEmpty()) {
//    	    throw new IllegalArgumentException(
//    	            "Invalid eventTypes: " + invalid +
//    	            ". Allowed values: " + allowed
//    	    );
//    	}
    	System.out.println(eventTypes);
        return ResponseEntity.ok(
                service.getTimeSeries(eventTypes, from, to)
        );
    }

    @Operation(summary = "Агрегация по mm_dma")
    @GetMapping("/aggregation/mm-dma")
    public ResponseEntity<List<AggregationDto>> getMmDmaAggregation(
            @RequestParam(required = false) List<String> eventTypes,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(service.getAggregationByMmDma(eventTypes, from, to));
    }

    @Operation(summary = "Агрегация по site_id")
    @GetMapping("/aggregation/site-id")
    public ResponseEntity<List<AggregationDto>> getSiteIdAggregation(
            @RequestParam(required = false) List<String> eventTypes,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(service.getAggregationBySiteId(eventTypes, from, to));
    }
}