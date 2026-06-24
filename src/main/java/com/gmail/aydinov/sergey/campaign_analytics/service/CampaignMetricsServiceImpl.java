package com.gmail.aydinov.sergey.campaign_analytics.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.MetricsByDateDto;
import com.gmail.aydinov.sergey.campaign_analytics.interfaces.CampaignMetricsService;
import com.gmail.aydinov.sergey.campaign_analytics.interfaces.DataStore;
import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;
import com.gmail.aydinov.sergey.campaign_analytics.model.Impression;

@Service
public class CampaignMetricsServiceImpl implements CampaignMetricsService {

	private final DataStore dataStore;

	public CampaignMetricsServiceImpl(DataStore dataStore) {
		this.dataStore = dataStore;
	}

	@Override
	public List<MetricsByDateDto> getTimeSeries(List<EventType> eventTypes, LocalDate from, LocalDate to) {
		
		SortedMap<LocalDate, List<Impression>> selectedImpressions = dataStore.getAllImpressions().subMap(from, to.plusDays(1));
		
		Map<LocalDate, List<Impression>> impressionsByDay = selectedImpressions.values().stream().flatMap(List::stream)
				.collect(Collectors.groupingBy(impression -> impression.regTime(), TreeMap::new, Collectors.toList()));
		List<MetricsByDateDto> result = new ArrayList<>();
		
		for (Entry<LocalDate, List<Impression>> entry : impressionsByDay.entrySet()) {
			LocalDate date = entry.getKey();
			List<Impression> impressions = entry.getValue();
			int impressionsCount = impressions.size();
			Set<String> uids = impressions.stream().map(Impression::uid).collect(Collectors.toSet());

			int clickCount = dataStore.getEventsOfUidsAndTypes(
			        uids,
			        List.of(EventType.fclick)
			).size();

			int eventCount = dataStore.getEventsOfUidsAndTypes(uids, eventTypes).size();
			double ctr = impressionsCount == 0 ? 0.0 : 100.0 * clickCount / impressionsCount;
			double evpm = impressionsCount == 0 ? 0.0 : 1000.0 * eventCount / impressionsCount;
			result.add(new MetricsByDateDto(date, impressionsCount, ctr, evpm));
		}

		return result;
	}

	@Override
	public List<AggregationDto> getAggregationByMmDma(List<EventType> eventTypes, LocalDate from, LocalDate to) {

	    SortedMap<LocalDate, List<Impression>> selectedImpressions =
	            dataStore.getAllImpressions()
	                    .subMap(from, to.plusDays(1));

	    Map<String, List<Impression>> impressionsByMmDma =
	            selectedImpressions.values().stream()
	                    .flatMap(List::stream)
	                    .collect(Collectors.groupingBy(Impression::mmDma));

	    List<AggregationDto> result = new ArrayList<>();

	    for (Entry<String, List<Impression>> entry : impressionsByMmDma.entrySet()) {

	        String mmDma = entry.getKey();
	        List<Impression> impressions = entry.getValue();

	        int impressionsCount = impressions.size();

	        Set<String> uids = impressions.stream()
	                .map(Impression::uid)
	                .collect(Collectors.toSet());

	        int clickCount = dataStore.getEventsOfUidsAndTypes(
	                uids,
	                List.of(EventType.fclick)
	        ).size();

	        int eventCount = dataStore.getEventsOfUidsAndTypes(
	                uids,
	                eventTypes
	        ).size();

	        double ctr = impressionsCount == 0
	                ? 0.0
	                : 100.0 * clickCount / impressionsCount;

	        double evpm = impressionsCount == 0
	                ? 0.0
	                : 1000.0 * eventCount / impressionsCount;

	        result.add(
	                new AggregationDto(
	                        mmDma,
	                        impressionsCount,
	                        ctr,
	                        evpm
	                )
	        );
	    }

	    return result;
	}

	@Override
	public List<AggregationDto> getAggregationBySiteId(
	        List<EventType> eventTypes,
	        LocalDate from,
	        LocalDate to) {

	    SortedMap<LocalDate, List<Impression>> selectedImpressions =
	            dataStore.getAllImpressions()
	                    .subMap(from, to.plusDays(1));

	    Map<String, List<Impression>> impressionsBySiteId =
	            selectedImpressions.values().stream()
	                    .flatMap(List::stream)
	                    .collect(Collectors.groupingBy(Impression::siteId));

	    List<AggregationDto> result = new ArrayList<>();

	    for (Entry<String, List<Impression>> entry : impressionsBySiteId.entrySet()) {

	        String siteId = entry.getKey();
	        List<Impression> impressions = entry.getValue();

	        int impressionsCount = impressions.size();

	        Set<String> uids = impressions.stream()
	                .map(Impression::uid)
	                .collect(Collectors.toSet());

	        int clickCount = dataStore.getEventsOfUidsAndTypes(
	                uids,
	                List.of(EventType.fclick)
	        ).size();

	        int eventCount = dataStore.getEventsOfUidsAndTypes(
	                uids,
	                eventTypes
	        ).size();

	        double ctr = impressionsCount == 0
	                ? 0.0
	                : 100.0 * clickCount / impressionsCount;

	        double evpm = impressionsCount == 0
	                ? 0.0
	                : 1000.0 * eventCount / impressionsCount;

	        result.add(
	                new AggregationDto(
	                        siteId,
	                        impressionsCount,
	                        ctr,
	                        evpm
	                )
	        );
	    }

	    return result;
	}

}
