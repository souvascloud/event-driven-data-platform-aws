package com.souvanik.eventplatform.model;

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
public class ProcessedEvent {

    private String eventId;
    private String eventType;
    private String userId;

    private String eventTimeUtc;
    private String eventTimeIst;
    private String eventDate;
    private Integer eventHour;

    private Double amount;
    private String paymentMethod;

    private String amountCategory;
    private Boolean highValue;

    private String deviceType;
    private String city;

    private String errorCode;

    private String source;
    private String version;

    private String processedAt;
}
