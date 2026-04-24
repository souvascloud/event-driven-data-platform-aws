package com.souvanik.eventplatform.transformer;

import com.souvanik.eventplatform.model.ProcessedEvent;

import java.util.Map;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface EventTransformer {
    String getEventType();

    ProcessedEvent transform(Map<String, Object> event);
}
