package com.souvanik.eventplatform.event_ingestion_service.exception;

import lombok.Builder;
import lombok.Data;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Data
@Builder
public class ErrorResponse {

    private String status;
    private String errorCode;
    private String message;
    private long timestamp;
}