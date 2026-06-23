package com.gmail.aydinov.sergey.campaign_analytics.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.TimeSeriesDto;
import com.gmail.aydinov.sergey.campaign_analytics.interfaces.CampaignMetricsService;
import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Analytics API")
@RestController
@RequestMapping("/api/campaign")
public class AnalyticsController {

    private final CampaignMetricsService service;

    public AnalyticsController(CampaignMetricsService service) {
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
    	
    	System.out.println(eventTypes);
        return ResponseEntity.ok(
                service.getTimeSeries(eventTypes, from, to)
        );
    }

    @Operation(summary = "Агрегация по mm_dma")
    @GetMapping("/aggregation/mm-dma")
    public ResponseEntity<List<AggregationDto>> getMmDmaAggregation(
            @RequestParam(required = false) List<EventType> eventTypes,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(service.getAggregationByMmDma(eventTypes, from, to));
    }

    @Operation(summary = "Агрегация по site_id")
    @GetMapping("/aggregation/site-id")
    public ResponseEntity<List<AggregationDto>> getSiteIdAggregation(
            @RequestParam(required = false) List<EventType> eventTypes,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {

        return ResponseEntity.ok(service.getAggregationBySiteId(eventTypes, from, to));
    }
}