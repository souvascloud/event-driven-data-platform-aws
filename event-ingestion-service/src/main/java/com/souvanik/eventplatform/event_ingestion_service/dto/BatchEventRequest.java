package com.souvanik.eventplatform.event_ingestion_service.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.List;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Data
public class BatchEventRequest {
    @NotEmpty
    @Valid
    private List<EventRequest> events;
}
