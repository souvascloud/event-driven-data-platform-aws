package com.souvanik.eventplatform.processing_service.dto;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.Instant;
import java.util.Map;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventMessage {

    private String eventId;
    private String eventType;
    private String userId;
    private Instant timestamp;


    private Map<String, Object> payload;


    private String source;
    private String version;
    private String schemaVersion;
}