package com.gmail.aydinov.sergey.campaign_analytics.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.TimeSeriesDto;
import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;

@Service
public class CampaignService {
	
	public List<AggregationDto> getAggregationByMmDma(List<String> eventTypes, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<AggregationDto> getAggregationBySiteId(List<String> eventTypes, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TimeSeriesDto> getTimeSeries(Set<EventType> eventTypes, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

	public List<TimeSeriesDto> getTimeSeries(Object eventTypes, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

}
