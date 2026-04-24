package com.souvanik.eventplatform.storage;

import com.souvanik.eventplatform.model.ProcessedEvent;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class S3KeyBuilder {
    private static final String BASE_PATH = "processed";

    public static String buildKey(ProcessedEvent event, String fileName) {

        String eventType = sanitize(event.getEventType());

        LocalDate date = extractDate(event.getEventTimeUtc());

        String year = String.valueOf(date.getYear());
        String month = String.format("%02d", date.getMonthValue());
        String day = String.format("%02d", date.getDayOfMonth());

        return String.format(
                "%s/event_type=%s/year=%s/month=%s/day=%s/%s",
                BASE_PATH,
                eventType,
                year,
                month,
                day,
                fileName
        );
    }

    private static String sanitize(String value) {
        return value == null ? "unknown" : value.toLowerCase().trim();
    }

    private static LocalDate extractDate(String eventTimeUtc) {
        try {
            return Instant.parse(eventTimeUtc)
                    .atZone(ZoneOffset.UTC)
                    .toLocalDate();
        } catch (Exception e) {
            return LocalDate.now(ZoneOffset.UTC);
        }
    }
}
