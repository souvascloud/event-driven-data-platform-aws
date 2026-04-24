package com.souvanik.eventplatform.event_ingestion_service.dto;

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
public class ApiResponse<T> {

    private String status;
    private String message;
    private T data;
    private long timestamp;
}