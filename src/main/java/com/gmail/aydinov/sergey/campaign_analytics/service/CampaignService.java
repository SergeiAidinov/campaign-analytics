package com.gmail.aydinov.sergey.campaign_analytics.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;

import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.TimeSeriesDto;

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

	public List<TimeSeriesDto> getTimeSeries(List<String> eventTypes, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

}
