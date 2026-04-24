package com.souvanik.eventplatform.event_ingestion_service.service;

import com.souvanik.eventplatform.event_ingestion_service.dto.BatchEventRequest;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface EventService {

    void processBatch(BatchEventRequest request);
}
