package com.souvanik.eventplatform.transformer.factory;

import com.souvanik.eventplatform.transformer.EventTransformer;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class TransformerFactory {
    private final Map<String, EventTransformer> map = new HashMap<>();

    public TransformerFactory(List<EventTransformer> transformers) {
        for (EventTransformer t : transformers) {
            map.put(normalize(t.getEventType()), t);
        }
    }

    public EventTransformer get(String eventType) {
        return map.get(normalize(eventType));
    }

    private String normalize(String value) {
        if (value == null) return null;

        return value
                .toLowerCase()
                .replace("_", "-")
                .trim();
    }
}
