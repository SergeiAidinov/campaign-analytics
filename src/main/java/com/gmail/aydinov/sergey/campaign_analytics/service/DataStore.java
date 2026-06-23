package com.gmail.aydinov.sergey.campaign_analytics.service;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.gmail.aydinov.sergey.campaign_analytics.exception.DataNotReadyException;
import com.gmail.aydinov.sergey.campaign_analytics.model.Event;
import com.gmail.aydinov.sergey.campaign_analytics.model.Impression;

@Component
public class DataStore {

	private volatile Map<String, Impression> impressionsByUid = Map.of();
	private volatile Map<String, List<Event>> eventsByUid = Map.of();
	
	private final CsvParserService csvParserService;
	private final AtomicBoolean impressionsLoading = new AtomicBoolean(false);
	private final AtomicBoolean eventsLoading = new AtomicBoolean(false);

	public DataStore(CsvParserService csvParserService) {
		this.csvParserService = csvParserService;
	}

	public CompletableFuture<Void> loadImpressionsAsync(MultipartFile file) {

	    if (!impressionsLoading.compareAndSet(false, true)) {
	        throw new DataNotReadyException("Impressions are already loading");
	    }

	    return CompletableFuture.runAsync(() -> {
	        try {

	            if (file.isEmpty()) {
	                throw new IllegalArgumentException("Impressions file is empty");
	            }
	            impressionsByUid =csvParserService.parseImpressions(file);;
	            System.out.println("Loaded impressions: " + impressionsByUid.size());
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        } finally {
	            impressionsLoading.set(false); 
	        }
	    });
	}

	public CompletableFuture<Void> loadEventsAsync(MultipartFile file) {

	    if (!eventsLoading.compareAndSet(false, true)) {
	        throw new DataNotReadyException("Events are already loading");
	    }

	    return CompletableFuture.runAsync(() -> {
	        try {

	            if (file.isEmpty()) {
	                throw new IllegalArgumentException("Events file is empty");
	            }
	            eventsByUid =csvParserService.parseEvents(file);;
	            System.out.println("Loaded events: " + eventsByUid.size());
	        } catch (Exception e) {
	            throw new RuntimeException(e);
	        } finally {
	            eventsLoading.set(false);
	        }
	    });
	}

	public Map<String, Impression> getImpressionsByUid() {
	    return Collections.unmodifiableMap(impressionsByUid);
	}

	public Map<String, List<Event>> getEventsByUid() {
	    return Collections.unmodifiableMap(eventsByUid);
	}

	public Optional<Impression> getImpression(String uid) {
	    return Optional.ofNullable(impressionsByUid.get(uid));
	}

	public List<Event> getEvents(String uid) {
	    return eventsByUid.getOrDefault(uid, List.of());
	}

	public boolean isReady() {
	    return !impressionsLoading.get()
	            && !eventsLoading.get()
	            && !impressionsByUid.isEmpty()
	            && !eventsByUid.isEmpty();
	}
}