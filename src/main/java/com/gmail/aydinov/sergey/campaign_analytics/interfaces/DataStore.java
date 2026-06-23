package com.gmail.aydinov.sergey.campaign_analytics.interfaces;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.springframework.web.multipart.MultipartFile;

import com.gmail.aydinov.sergey.campaign_analytics.model.Event;
import com.gmail.aydinov.sergey.campaign_analytics.model.Impression;

public interface DataStore {

	CompletableFuture<Void> loadImpressionsAsync(MultipartFile file);

	CompletableFuture<Void> loadEventsAsync(MultipartFile file);

	Map<String, Impression> getAllImpressions();

	Map<String, List<Event>> getAllEvents();

	Optional<Impression> getImpressionByUid(String uid);

	List<Event> getEventsByUid(String uid);

	boolean isReady();

}
