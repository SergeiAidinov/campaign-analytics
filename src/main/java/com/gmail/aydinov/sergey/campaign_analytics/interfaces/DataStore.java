package com.gmail.aydinov.sergey.campaign_analytics.interfaces;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

import com.gmail.aydinov.sergey.campaign_analytics.model.Event;
import com.gmail.aydinov.sergey.campaign_analytics.model.EventType;
import com.gmail.aydinov.sergey.campaign_analytics.model.Impression;

public interface DataStore {

	CompletableFuture<Void> loadImpressionsAsync(MultipartFile file);

	CompletableFuture<Void> loadEventsAsync(MultipartFile file);

	SortedMap<LocalDate, List<Impression>> getAllImpressions();

	Map<String, List<Event>> getAllEvents();

	List<Event> getEventsByUid(String uid);
	
	List<Event> getEventsOfUidsAndTypes(Set<String> set, List<EventType> types);

	boolean isReady();

}
