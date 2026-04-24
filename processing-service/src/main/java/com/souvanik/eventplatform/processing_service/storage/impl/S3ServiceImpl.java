package com.souvanik.eventplatform.processing_service.storage.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.souvanik.eventplatform.processing_service.config.property.S3Properties;
import com.souvanik.eventplatform.processing_service.dto.EventMessage;
import com.souvanik.eventplatform.processing_service.storage.S3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.time.Instant;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@Service
@RequiredArgsConstructor
public class S3ServiceImpl implements S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3ServiceImpl.class);

    private final S3Client s3Client;
    private final ObjectMapper objectMapper;
    private final S3Properties s3Properties;


    @Override
    public void uploadBatch(List<EventMessage> events) {

        if (events == null || events.isEmpty()) {
            logger.warn("event=s3_upload status=skipped reason=empty_batch");
            return;
        }

        String bucketName = s3Properties.getBucketName();

        if (bucketName == null || bucketName.isBlank()) {
            throw new IllegalStateException("S3 bucket name is not configured");
        }

        try {
            // 1. Serialize batch
            String json = objectMapper.writeValueAsString(events);

            // 2. Generate key
            String key = generateS3Key();

            // 3. Upload
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType("application/json")
                    .build();

            s3Client.putObject(request, RequestBody.fromString(json));

            logger.info("event=s3_upload status=success key={} batch_size={}", key, events.size());

        } catch (Exception e) {
            logger.error("event=s3_upload status=failed batch_size={}", events.size(), e);
            throw new RuntimeException("Failed to upload batch to S3", e);
        }
    }

    /**
     * Generate partitioned S3 key
     */
    private String generateS3Key() {

        Instant now = Instant.now();

        String year = DateTimeFormatter.ofPattern("yyyy")
                .withZone(ZoneOffset.UTC)
                .format(now);

        String month = DateTimeFormatter.ofPattern("MM")
                .withZone(ZoneOffset.UTC)
                .format(now);

        String day = DateTimeFormatter.ofPattern("dd")
                .withZone(ZoneOffset.UTC)
                .format(now);

        long timestamp = now.toEpochMilli();

        return String.format(
                "raw/user-events/year=%s/month=%s/day=%s/events_%d_%s.json",
                year,
                month,
                day,
                timestamp,
                UUID.randomUUID()
        );
    }
}