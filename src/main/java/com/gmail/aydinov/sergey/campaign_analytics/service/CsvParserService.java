package com.gmail.aydinov.sergey.campaign_analytics.service;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.gmail.aydinov.sergey.campaign_analytics.model.Event;
import com.gmail.aydinov.sergey.campaign_analytics.model.Impression;
import com.opencsv.CSVReader;

@Service
public class CsvParserService {

    // формат даты
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    // минимальное число колонок в impressions
    private static final int IMPRESSION_MIN_COLUMNS = 10;

    // индексы колонок impressions
    private static final int IDX_REG_TIME = 0;
    private static final int IDX_UID = 1;
    private static final int IDX_FC_IMP_CHK = 2;
    private static final int IDX_FC_TIME_CHK = 3;
    private static final int IDX_UTMTR = 4;
    private static final int IDX_MM_DMA = 5;
    private static final int IDX_OS_NAME = 6;
    private static final int IDX_MODEL = 7;
    private static final int IDX_HARDWARE = 8;
    private static final int IDX_SITE_ID = 9;

    // минимальное число колонок events
    private static final int EVENTS_MIN_COLUMNS = 2;

    // индексы events
    private static final int IDX_EVENT_UID = 0;
    private static final int IDX_EVENT_TAG = 1;

    // logging messages (опционально)
    private static final String LOG_IMPRESSIONS_LOADED = "Impressions loaded: ";
    private static final String LOG_EVENTS_LOADED = "Events loaded: ";

    public TreeMap<LocalDate, List<Impression>> parseImpressions(MultipartFile file) throws Exception {

        int impressionsQuantity = 0;
        TreeMap<LocalDate, List<Impression>> impressions = new TreeMap<>();

        try (CSVReader reader = new CSVReader(
                new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8))) {

            reader.readNext(); // header

            String[] line;

            while ((line = reader.readNext()) != null) {

                if (line.length < IMPRESSION_MIN_COLUMNS) {
                    continue;
                }

                Impression impression = new Impression(
                        line[IDX_UID].trim(),
                        LocalDate.parse(line[IDX_REG_TIME].trim(), DATE_TIME_FORMATTER),
                        Integer.parseInt(line[IDX_FC_IMP_CHK]),
                        Integer.parseInt(line[IDX_FC_TIME_CHK]),
                        Integer.parseInt(line[IDX_UTMTR]),
                        line[IDX_MM_DMA].trim(),
                        line[IDX_OS_NAME].trim(),
                        line[IDX_MODEL].trim(),
                        line[IDX_HARDWARE].trim(),
                        line[IDX_SITE_ID].trim()
                );

                impressions
                        .computeIfAbsent(impression.regTime(), k -> new ArrayList<>())
                        .add(impression);

                impressionsQuantity++;
            }
        }

        System.out.println(LOG_IMPRESSIONS_LOADED + impressionsQuantity);
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

                if (line.length < EVENTS_MIN_COLUMNS) {
                    continue;
                }

                String uid = line[IDX_EVENT_UID].trim();

                Event event = new Event(
                        uid,
                        line[IDX_EVENT_TAG].trim()
                );

                eventsByUid
                        .computeIfAbsent(uid, ignored -> new ArrayList<>())
                        .add(event);

                eventsQuantity++;
            }
        }

        System.out.println(LOG_EVENTS_LOADED + eventsQuantity);
        return eventsByUid;
    }
}