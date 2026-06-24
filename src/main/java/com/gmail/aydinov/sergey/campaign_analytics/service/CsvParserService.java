package com.gmail.aydinov.sergey.campaign_analytics.service;

import com.gmail.aydinov.sergey.campaign_analytics.model.Event;
import com.gmail.aydinov.sergey.campaign_analytics.model.Impression;
import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class CsvParserService {

    private final DateTimeFormatter formatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public TreeMap<LocalDate, List<Impression>> parseImpressions(MultipartFile file) throws Exception {
    	int impressionsQuantity = 0;
        TreeMap<LocalDate, List<Impression>> impressions = new TreeMap<>();

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            reader.readNext(); // header

            String[] line;

            while ((line = reader.readNext()) != null) {

                if (line.length < 10) {
                    continue;
                }

                Impression impression = new Impression(
                        line[1].trim(),
                        LocalDate.parse(line[0].trim(), formatter),
                        Integer.parseInt(line[2]),
                        Integer.parseInt(line[3]),
                        Integer.parseInt(line[4]),
                        line[5].trim(),
                        line[6].trim(),
                        line[7].trim(),
                        line[8].trim(),
                        line[9].trim()
                );

                impressions
                        .computeIfAbsent(impression.regTime(), k -> new ArrayList<>())
                        .add(impression);
                impressionsQuantity++;
            }
        }
        System.out.println("Impressions loaded: " + impressionsQuantity);
        return impressions;
    }

    public Map<String, List<Event>> parseEvents(MultipartFile file) throws Exception {
        Map<String, List<Event>> eventsByUid = new HashMap<>();
        int eventsQuantity = 0;
        try (CSVReader reader = new CSVReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {
            reader.readNext(); // header
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 2) {
                    continue;
                }
                String uid = line[0].trim();
                Event event = new Event(
                        uid,
                        line[1].trim()
                );
                eventsByUid
                        .computeIfAbsent(uid, ignored -> new ArrayList<>())
                        .add(event);
                eventsQuantity++;
            }
        }
        System.out.println("Events loaded: " + eventsQuantity);
        return eventsByUid;
    }
}