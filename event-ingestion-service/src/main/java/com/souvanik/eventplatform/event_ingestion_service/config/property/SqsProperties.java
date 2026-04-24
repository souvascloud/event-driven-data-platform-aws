package com.souvanik.eventplatform.event_ingestion_service.config.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Configuration
@ConfigurationProperties(prefix = "aws.sqs")
@Getter
@Setter
public class SqsProperties {

    private String queueUrl;
}