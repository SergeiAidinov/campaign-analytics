package com.gmail.aydinov.sergey.campaign_analytics.dto;

import java.time.LocalDate;

public record TimeSeriesDto(
        LocalDate date,
        double ctr,
        double evpm
) {}
