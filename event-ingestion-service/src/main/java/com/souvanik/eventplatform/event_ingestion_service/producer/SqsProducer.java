package com.souvanik.eventplatform.event_ingestion_service.producer;

import com.souvanik.eventplatform.event_ingestion_service.dto.EventRequest;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface SqsProducer {
    void sendEvent(EventRequest event);
}
