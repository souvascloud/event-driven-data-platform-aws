package com.souvanik.eventplatform.processing_service.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.souvanik.eventplatform.processing_service.config.property.SqsProperties;
import com.souvanik.eventplatform.processing_service.dto.EventMessage;
import com.souvanik.eventplatform.processing_service.service.BatchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.DeleteMessageRequest;
import software.amazon.awssdk.services.sqs.model.Message;
import software.amazon.awssdk.services.sqs.model.ReceiveMessageRequest;

import java.util.List;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Component
@RequiredArgsConstructor
public class SqsConsumer {
    private static final Logger logger = LoggerFactory.getLogger(SqsConsumer.class);

    private final SqsClient sqsClient;
    private final BatchService batchService;
    private final ObjectMapper objectMapper;
    private final SqsProperties sqsProperties;

    @Scheduled(fixedDelay = 5000)
    public void pollMessages() {

        String queueUrl = sqsProperties.getQueueUrl();

        logger.info("event=sqs_poll status=triggered queue={}", queueUrl);

        try {
            ReceiveMessageRequest request = ReceiveMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(20)
                    .build();

            List<Message> messages = sqsClient.receiveMessage(request).messages();

            if (messages == null || messages.isEmpty()) {
                logger.info("event=sqs_poll status=empty");
                return;
            }

            logger.info("event=sqs_poll status=received count={}", messages.size());

            for (Message message : messages) {
                processMessage(message);
            }

        } catch (Exception e) {
            logger.error("event=sqs_poll status=failed", e);
        }
    }

    private void processMessage(Message message) {

        String messageId = message.messageId();

        try {
            EventMessage event = objectMapper.readValue(message.body(), EventMessage.class);

            logger.info("event=sqs_process status=parsed message_id={}", messageId);

            batchService.addEvent(event);

            deleteMessage(message);

        } catch (Exception e) {
            logger.error("event=sqs_process status=failed message_id={}", messageId, e);
        }
    }

    private void deleteMessage(Message message) {

        String queueUrl = sqsProperties.getQueueUrl();

        try {
            DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .receiptHandle(message.receiptHandle())
                    .build();

            sqsClient.deleteMessage(deleteRequest);

            logger.info("event=sqs_delete status=success message_id={}", message.messageId());

        } catch (Exception e) {
            logger.error("event=sqs_delete status=failed message_id={}", message.messageId(), e);
        }
    }
}