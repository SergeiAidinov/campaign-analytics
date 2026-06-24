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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<AggregationDto> getAggregationBySiteId(List<EventType> eventTypes, LocalDate from, LocalDate to) {
		// TODO Auto-generated method stub
		return null;
	}

	private String normalize(EventType type) {
		if (type == null)
			return null;

		String name = type.name();

		// vregistration → registration
		if (name.startsWith("v")) {
			return name.substring(1);
		}

		return name;
	}

//		private List<Impression> getFilteredImpressions(LocalDate from, LocalDate to) {
//		    return dataStore.getAllImpressions().values().stream()
//		            .filter(i -> {
//		                LocalDate d = i.regTime().toLocalDate();
//		                return (from == null || !d.isBefore(from))
//		                        && (to == null || !d.isAfter(to));
//		            })
//		            .toList();
//		}

	private List<String> getEventTypesByUid(String uid) {
		return dataStore.getAllEvents().getOrDefault(uid, List.of()).stream()
				.map(e -> normalize(EventType.valueOf(e.tag()))).toList();
	}

	private boolean matchesEventFilter(String uid, List<EventType> eventTypes) {
		if (eventTypes == null || eventTypes.isEmpty())
			return true;

		return dataStore.getEventsByUid(uid).stream().map(Event::tag).anyMatch(tag -> {
			try {
				EventType type = EventType.valueOf(tag);
				return eventTypes.contains(type);
			} catch (Exception e) {
				return false;
			}
		});
	}

}
