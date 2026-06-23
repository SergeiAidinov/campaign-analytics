package com.gmail.aydinov.sergey.campaign_analytics.controller;

import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.TimeSeriesDto;
import com.gmail.aydinov.sergey.campaign_analytics.service.CampaignService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/campaign")
public class AnalyticsController {

    private final CampaignService service;

    public AnalyticsController(CampaignService service) {
        this.service = service;
    }

    @GetMapping("/timeseries")
    @Operation(summary = "График метрик по времени")
    public ResponseEntity<List<TimeSeriesDto>> getTimeSeries(

            @RequestParam(required = false)
            @Parameter(description = "Типы событий (например: view, click)")
            List<String> eventTypes,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Начальная дата (YYYY-MM-DD)", example = "2021-07-21")
            LocalDate from,

            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            @Parameter(description = "Конечная дата (YYYY-MM-DD)", example = "2021-08-06")
            LocalDate to
    ) {
        return ResponseEntity.ok(
                service.getTimeSeries(eventTypes, from, to)
        );
    }
    // Аналогично для mm-dma и site-id
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