package com.gmail.aydinov.sergey.campaign_analytics.interfaces;

import java.time.LocalDate;
import java.util.List;

import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.StatisticsByDateDto;
import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;

public interface CampaignMetricsService {
	
	List<StatisticsByDateDto> getTimeSeries(List<EventType> eventTypes,  LocalDate from,  LocalDate to);

	List<AggregationDto> getAggregationByMmDma(List<EventType> eventTypes, LocalDate from, LocalDate to);

	List<AggregationDto> getAggregationBySiteId(List<EventType> eventTypes, LocalDate from, LocalDate to);
}
