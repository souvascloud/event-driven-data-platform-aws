package com.souvanik.eventplatform.transformer;

import com.souvanik.eventplatform.model.ProcessedEvent;
import com.souvanik.eventplatform.util.TimeUtil;

import java.util.Map;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public abstract class AbstractBaseTransformer implements EventTransformer {
    protected ProcessedEvent.ProcessedEventBuilder baseBuilder(Map<String, Object> e) {

        String eventTypeRaw = getString(e, "eventType");
        String timestamp = getString(e, "timestamp");

        String normalizedEventType = normalize(eventTypeRaw);

        return ProcessedEvent.builder()
                .eventId(getString(e, "eventId"))
                .eventType(normalizedEventType)
                .userId(getString(e, "userId"))

                .eventTimeUtc(TimeUtil.utc(timestamp))
                .eventTimeIst(TimeUtil.ist(timestamp))
                .eventDate(TimeUtil.date(timestamp))
                .eventHour(TimeUtil.hour(timestamp))

                .source(getString(e, "source"))
                .version(getString(e, "version"))
                .processedAt(TimeUtil.nowIst());
    }


    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString() : null;
    }

    private String normalize(String value) {
        if (value == null) return null;

        return value
                .toLowerCase()
                .replace("_", "-")
                .trim();
    }
}