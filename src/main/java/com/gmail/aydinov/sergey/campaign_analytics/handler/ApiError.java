package com.gmail.aydinov.sergey.campaign_analytics.handler;

public record ApiError(
        String message,
        int status
) {}
