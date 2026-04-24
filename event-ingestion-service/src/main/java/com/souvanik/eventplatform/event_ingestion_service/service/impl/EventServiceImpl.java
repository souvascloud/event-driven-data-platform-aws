package com.souvanik.eventplatform.event_ingestion_service.service.impl;

import com.souvanik.eventplatform.event_ingestion_service.dto.BatchEventRequest;
import com.souvanik.eventplatform.event_ingestion_service.dto.EventRequest;
import com.souvanik.eventplatform.event_ingestion_service.producer.SqsProducer;
import com.souvanik.eventplatform.event_ingestion_service.service.EventService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final SqsProducer sqsProducer;

    @Override
    public void processBatch(BatchEventRequest request) {

        int totalEvents = request.getEvents().size();

        logger.info("event=service_process_batch status=start total_events={}", totalEvents);

        for (EventRequest event : request.getEvents()) {


            validateEvent(event);

            sqsProducer.sendEvent(event);
        }

        logger.info("event=service_process_batch status=completed total_events={}", totalEvents);
    }

    private void validateEvent(EventRequest event) {
        if (event.getEventType() == null) {
            throw new IllegalArgumentException("eventType cannot be null");
        }
    }
}