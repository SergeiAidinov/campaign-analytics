package com.gmail.aydinov.sergey.campaign_analytics.dto;

import java.time.LocalDate;

public record StatisticsByDateDto(
        LocalDate date,
        int impressionsCount,
        double ctr,
        double evpm
) {}
