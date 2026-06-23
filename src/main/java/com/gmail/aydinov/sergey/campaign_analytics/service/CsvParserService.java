package com.gmail.aydinov.sergey.campaign_analytics.service;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.util.HashMap;
import java.util.Map;

@Service
public class CsvParserService {

    public Map<String, Map<String, Long>> parseEvents(String filePath) throws Exception {
        Map<String, Map<String, Long>> uidToEvents = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // header
            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 2) continue;
                String uid = line[0].trim();
                String tag = line[1].trim();
                uidToEvents.computeIfAbsent(uid, k -> new HashMap<>())
                        .merge(tag, 1L, Long::sum);
            }
        }
        return uidToEvents;
    }

    // Можно добавить метод для парсинга X.csv, если нужно
}