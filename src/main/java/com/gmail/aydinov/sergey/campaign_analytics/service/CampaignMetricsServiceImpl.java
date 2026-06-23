package com.gmail.aydinov.sergey.campaign_analytics.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.TimeSeriesDto;
import com.gmail.aydinov.sergey.campaign_analytics.interfaces.CampaignMetricsService;
import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;
import com.gmail.aydinov.sergey.campaign_analytics.model.Impression;

@Service
public class CampaignMetricsServiceImpl implements CampaignMetricsService {
	
	 private final DataStore dataStore;

	    public CampaignMetricsServiceImpl(DataStore dataStore) {
	        this.dataStore = dataStore;
	    }

		@Override
		public List<TimeSeriesDto> getTimeSeries(List<EventType> eventTypes, LocalDate from, LocalDate to) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<AggregationDto> getAggregationByMmDma(List<EventType> eventTypes, LocalDate from, LocalDate to) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public List<AggregationDto> getAggregationBySiteId(List<EventType> eventTypes, LocalDate from, LocalDate to) {
			// TODO Auto-generated method stub
			return null;
		}

	   

}
