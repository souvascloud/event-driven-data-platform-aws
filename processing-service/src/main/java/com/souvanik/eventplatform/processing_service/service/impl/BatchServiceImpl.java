package com.souvanik.eventplatform.processing_service.service.impl;

import com.souvanik.eventplatform.processing_service.config.property.ProcessingProperties;
import com.souvanik.eventplatform.processing_service.dto.EventMessage;
import com.souvanik.eventplatform.processing_service.service.BatchService;
import com.souvanik.eventplatform.processing_service.storage.S3Service;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */


@Service
@RequiredArgsConstructor
public class BatchServiceImpl implements BatchService {

    private static final Logger logger = LoggerFactory.getLogger(BatchServiceImpl.class);

    private final List<EventMessage> buffer = new ArrayList<>();

    private final ProcessingProperties processingProperties;
    private final S3Service s3Service;



    /**
     * Add event to buffer
     */
    @Override
    public void addEvent(EventMessage event) {

        if (event == null) {
            logger.warn("event=batch_add status=skipped reason=null_event");
            return;
        }

        List<EventMessage> batchToFlush = null;

        synchronized (this) {
            buffer.add(event);

            int currentSize = buffer.size();
            logger.info("event=batch_add status=added current_size={}", currentSize);

            if (currentSize >= processingProperties.getSize()) {
                batchToFlush = drainBuffer();
            }
        }

        if (batchToFlush != null) {
            flush(batchToFlush);
        }
    }

    /**
     * Time-based flush
     */
    @Scheduled(fixedDelayString = "${processing.batch.interval-ms:30000}")
    public void scheduledFlush() {

        List<EventMessage> batchToFlush;

        synchronized (this) {
            if (buffer.isEmpty()) {
                return;
            }

            logger.info("event=batch_flush_trigger status=scheduled size={}", buffer.size());
            batchToFlush = drainBuffer();
        }

        // flush outside lock
        flush(batchToFlush);
    }

    /**
     * Drain buffer safely
     */
    private List<EventMessage> drainBuffer() {
        List<EventMessage> batch = new ArrayList<>(buffer);
        buffer.clear();
        return batch;
    }

    /**
     * Actual flush logic
     */
    private void flush(List<EventMessage> batch) {

        if (batch == null || batch.isEmpty()) {
            return;
        }

        logger.info("event=batch_flush status=start batch_size={}", batch.size());

        try {
            s3Service.uploadBatch(batch);

            logger.info("event=batch_flush status=success batch_size={}", batch.size());

        } catch (Exception e) {
            logger.error("event=batch_flush status=failed batch_size={}", batch.size(), e);

        }
    }
}