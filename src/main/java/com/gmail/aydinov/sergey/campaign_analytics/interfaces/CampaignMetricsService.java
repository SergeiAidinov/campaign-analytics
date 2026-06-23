package com.gmail.aydinov.sergey.campaign_analytics.interfaces;

import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.TimeSeriesDto;
import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;

import java.time.LocalDate;
import java.util.List;

public interface CampaignMetricsService {
	
	List<TimeSeriesDto> getTimeSeries(List<EventType> eventTypes, LocalDate from, LocalDate to);

	List<AggregationDto> getAggregationByMmDma(List<EventType> eventTypes, LocalDate from, LocalDate to);

	List<AggregationDto> getAggregationBySiteId(List<EventType> eventTypes, LocalDate from, LocalDate to);
}
