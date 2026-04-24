package com.souvanik.eventplatform.event_ingestion_service.controller;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */

import com.souvanik.eventplatform.event_ingestion_service.dto.ApiResponse;
import com.souvanik.eventplatform.event_ingestion_service.dto.BatchEventRequest;
import com.souvanik.eventplatform.event_ingestion_service.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private static final Logger logger = LoggerFactory.getLogger(EventController.class);

    private final EventService eventService;

    @PostMapping("/batch")
    public ResponseEntity<ApiResponse<Void>> ingestBatchEvents(@Valid @RequestBody BatchEventRequest request) {

        int size = request.getEvents().size();

        logger.info("event=ingestion_request status=received batch_size={}", size);

        eventService.processBatch(request);

        logger.info("event=ingestion_request status=queued batch_size={}", size);

        ApiResponse<Void> response = ApiResponse.<Void>builder()
                .status("SUCCESS")
                .message("Events accepted for processing")
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.accepted().body(response);
    }
}
