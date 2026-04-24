package com.souvanik.eventplatform.event_ingestion_service.dto;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
public class EventRequest {

    @NotBlank
    private String eventId;

    @NotBlank
    private String eventType;

    @NotBlank
    private String userId;

    @NotNull
    private Instant timestamp;

    private Map<String, Object> payload;

    private String source;

    private String version;

    private String schemaVersion;
}