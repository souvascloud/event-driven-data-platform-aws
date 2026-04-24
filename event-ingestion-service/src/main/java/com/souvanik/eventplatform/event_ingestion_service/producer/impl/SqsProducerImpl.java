package com.souvanik.eventplatform.event_ingestion_service.producer.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.souvanik.eventplatform.event_ingestion_service.config.property.SqsProperties;
import com.souvanik.eventplatform.event_ingestion_service.dto.EventRequest;
import com.souvanik.eventplatform.event_ingestion_service.producer.SqsProducer;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Component
@RequiredArgsConstructor
public class SqsProducerImpl implements SqsProducer {

    private static final Logger logger = LoggerFactory.getLogger(SqsProducerImpl.class);

    private final SqsClient sqsClient;
    private final SqsProperties sqsProperties;
    private final ObjectMapper objectMapper;

    @Override
    public void sendEvent(EventRequest event) {

        try {
            String messageBody = objectMapper.writeValueAsString(event);

            SendMessageRequest request = SendMessageRequest.builder()
                    .queueUrl(sqsProperties.getQueueUrl())
                    .messageBody(messageBody)
                    .build();

            sqsClient.sendMessage(request);

            logger.info("event=sqs_publish status=success event_id={}", event.getEventId());

        } catch (JsonProcessingException e) {
            logger.error("event=sqs_publish status=serialization_failed event_id={}", event.getEventId(), e);
            throw new RuntimeException("Failed to serialize event", e);

        } catch (Exception e) {
            logger.error("event=sqs_publish status=failed event_id={}", event.getEventId(), e);
            throw new RuntimeException("Failed to send message to SQS", e);
        }
    }
}