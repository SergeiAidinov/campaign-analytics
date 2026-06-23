package com.gmail.aydinov.sergey.campaign_analytics.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gmail.aydinov.sergey.campaign_analytics.dto.AggregationDto;
import com.gmail.aydinov.sergey.campaign_analytics.dto.TimeSeriesDto;
import com.gmail.aydinov.sergey.campaign_analytics.interfaces.CampaignMetricsService;
import com.gmail.aydinov.sergey.campaign_analytics.interfaces.DataStore;
import com.gmail.aydinov.sergey.campaign_analytics.model.Event;
import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;
import com.gmail.aydinov.sergey.campaign_analytics.model.Impression;

@Service
public class CampaignMetricsServiceImpl implements CampaignMetricsService {
	
	 private final DataStore dataStore;

	    public CampaignMetricsServiceImpl(DataStore dataStore) {
	        this.dataStore = dataStore;
	    }

		@Override
		public List<TimeSeriesDto> getTimeSeries(List<EventType> eventTypes,
		                                         LocalDate from,
		                                         LocalDate to) {

		    List<Impression> impressions = getFilteredImpressions(from, to);

		    Map<LocalDate, List<Impression>> grouped = impressions.stream()
		            .filter(i -> matchesEventFilter(i.uid(), eventTypes))
		            .collect(Collectors.groupingBy(i -> i.regTime().toLocalDate()));

		    List<TimeSeriesDto> result = new ArrayList<>();

		    for (Entry<LocalDate, List<Impression>> entry : grouped.entrySet()) {

		        LocalDate date = entry.getKey();
		        List<Impression> ims = entry.getValue();

		        int impressionsCount = ims.size();

		        long eventsCount = ims.stream()
		                .map(Impression::uid)
		                .distinct()
		                .flatMap(uid -> dataStore.getEventsByUid(uid).stream())
		                .count();

		        double ctr = impressionsCount == 0 ? 0.0 :
		                (double) eventsCount / impressionsCount;

		        double evpm = impressionsCount == 0 ? 0.0 :
		                ((double) eventsCount / impressionsCount) * 1000;

		        result.add(new TimeSeriesDto(
		                date,
		                ctr,
		                evpm
		        ));
		    }

		    return result.stream()
		            .sorted(Comparator.comparing(TimeSeriesDto::date))
		            .toList();
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

		private String normalize(EventType type) {
		    if (type == null) return null;

		    String name = type.name();

		    // vregistration → registration
		    if (name.startsWith("v")) {
		        return name.substring(1);
		    }

		    return name;
		}
		
		private List<Impression> getFilteredImpressions(LocalDate from, LocalDate to) {
		    return dataStore.getAllImpressions().values().stream()
		            .filter(i -> {
		                LocalDate d = i.regTime().toLocalDate();
		                return (from == null || !d.isBefore(from))
		                        && (to == null || !d.isAfter(to));
		            })
		            .toList();
		}
		
		private List<String> getEventTypesByUid(String uid) {
		    return dataStore.getAllEvents()
		            .getOrDefault(uid, List.of())
		            .stream()
		            .map(e -> normalize(EventType.valueOf(e.tag())))
		            .toList();
		}
		
		private boolean matchesEventFilter(String uid, List<EventType> eventTypes) {
		    if (eventTypes == null || eventTypes.isEmpty()) return true;

		    return dataStore.getEventsByUid(uid).stream()
		            .map(Event::tag)
		            .anyMatch(tag -> {
		                try {
		                    EventType type = EventType.valueOf(tag);
		                    return eventTypes.contains(type);
		                } catch (Exception e) {
		                    return false;
		                }
		            });
		}
		
		
		
		
	   

}
