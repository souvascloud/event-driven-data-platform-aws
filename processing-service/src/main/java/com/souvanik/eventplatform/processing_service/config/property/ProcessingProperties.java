package com.souvanik.eventplatform.processing_service.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "processing.batch")
public class ProcessingProperties {
    private int size;
    private long intervalMs;
}
