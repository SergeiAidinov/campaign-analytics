package com.gmail.aydinov.sergey.campaign_analytics.service;

import com.opencsv.CSVReader;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
public class DataStore {

    private final CsvParserService csvParserService;

    // Основные хранилища
    private final Map<Object, Aggregate> daily = new HashMap<>();
    private final Map<Object, Aggregate> byMmDma = new HashMap<>();
    private final Map<Object, Aggregate> bySiteId = new HashMap<>();

    private final Map<String, Map<String, Long>> uidToEvents = new HashMap<>();

    public DataStore(CsvParserService csvParserService) {
        this.csvParserService = csvParserService;
    }

    // ===================== EVENTS (y.csv) =====================
    public CompletableFuture<String> collectDataFromEventFileAsync(MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (file.isEmpty()) throw new IllegalArgumentException("Файл пустой");

                Path tempFile = Files.createTempFile("events", ".csv");
                try {
                    Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

                    var parsed = csvParserService.parseEvents(tempFile.toString());

                    synchronized (uidToEvents) {
                        uidToEvents.clear();
                        uidToEvents.putAll(parsed);
                    }

                    String msg = "✅ События загружены: " + parsed.size() + " uid";
                    System.out.println(msg);
                    return msg;
                } finally {
                    Files.deleteIfExists(tempFile);
                }
            } catch (Exception e) {
                String err = "❌ Ошибка событий: " + e.getMessage();
                System.err.println(err);
                throw new RuntimeException(err, e);
            }
        });
    }

    // ===================== VIEWS / IMPRESSIONS (X.csv) =====================
    public CompletableFuture<String> collectDataFromViewsFileAsync(MultipartFile file) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (file.isEmpty()) throw new IllegalArgumentException("Файл пустой");

                Path tempFile = Files.createTempFile("views", ".csv");
                try {
                    Files.copy(file.getInputStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);

                    var stats = parseAndBuildAggregates(tempFile.toString());

                    String msg = String.format("✅ Показы загружены: %s | Дней: %d | mm_dma: %d | site_id: %d",
                            file.getOriginalFilename(), stats.dailyCount, stats.mmDmaCount, stats.siteIdCount);
                    System.out.println(msg);
                    return msg;
                } finally {
                    Files.deleteIfExists(tempFile);
                }
            } catch (Exception e) {
                String err = "❌ Ошибка показов: " + e.getMessage();
                System.err.println(err);
                throw new RuntimeException(err, e);
            }
        });
    }

    private Stats parseAndBuildAggregates(String filePath) throws Exception {
        Stats stats = new Stats();

        try (CSVReader reader = new CSVReader(new FileReader(filePath))) {
            reader.readNext(); // header

            String[] line;
            while ((line = reader.readNext()) != null) {
                if (line.length < 10) continue;

                int fcImpChk = Integer.parseInt(line[2]);
                if (fcImpChk < 0) continue;

                LocalDate date = LocalDate.parse(line[0].substring(0, 10));
                String uid = line[1].trim();
                String mmDma = line[5].trim();
                String siteId = line[9].trim();

                Map<String, Long> userEvents = uidToEvents.getOrDefault(uid, Map.of());

                updateAggregate(daily, date, userEvents);
                updateAggregate(byMmDma, mmDma, userEvents);
                updateAggregate(bySiteId, siteId, userEvents);

                stats.dailyCount = daily.size();
                stats.mmDmaCount = byMmDma.size();
                stats.siteIdCount = bySiteId.size();
            }
        }
        return stats;
    }

    private void updateAggregate(Map<Object, Aggregate> targetMap, Object key, Map<String, Long> userEvents) {
        Aggregate agg = targetMap.computeIfAbsent(key, k -> new Aggregate());
        agg.impressions++;

        userEvents.forEach((tag, count) ->
                agg.eventsByTag.merge(tag, count, Long::sum));
    }

    // ===================== Геттеры =====================
    public Map<String, Map<String, Long>> getUidToEvents() {
        return new HashMap<>(uidToEvents);
    }

    public Map<Object, Aggregate> getDaily() {
        return new HashMap<>(daily);
    }

    public Map<Object, Aggregate> getByMmDma() {
        return new HashMap<>(byMmDma);
    }

    public Map<Object, Aggregate> getBySiteId() {
        return new HashMap<>(bySiteId);
    }

    // ===================== Внутренние классы =====================
    public static class Aggregate {
        public long impressions = 0;
        public Map<String, Long> eventsByTag = new HashMap<>();
    }

    private static class Stats {
        int dailyCount = 0;
        int mmDmaCount = 0;
        int siteIdCount = 0;
    }
}