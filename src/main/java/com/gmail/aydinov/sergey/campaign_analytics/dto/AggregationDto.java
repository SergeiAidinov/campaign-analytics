package com.gmail.aydinov.sergey.campaign_analytics.dto;

public record AggregationDto(
        String groupKey,
        int impressionsCount,
        double ctr,
        double evpm
) {}
