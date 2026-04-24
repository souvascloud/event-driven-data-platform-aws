package com.souvanik.eventplatform.service.impl;

import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.souvanik.eventplatform.model.ProcessedEvent;
import com.souvanik.eventplatform.service.S3Service;
import com.souvanik.eventplatform.service.TransformService;
import com.souvanik.eventplatform.storage.ParquetWriterUtil;
import com.souvanik.eventplatform.storage.S3KeyBuilder;
import com.souvanik.eventplatform.transformer.EventTransformer;
import com.souvanik.eventplatform.transformer.factory.TransformerFactory;
import com.souvanik.eventplatform.util.TimeUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class TransformServiceImpl implements TransformService {

    private static final Logger logger = LoggerFactory.getLogger(TransformServiceImpl.class);

    private final S3Service s3Service;
    private final TransformerFactory factory;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public TransformServiceImpl(S3Service s3Service, TransformerFactory factory) {
        this.s3Service = s3Service;
        this.factory = factory;
    }

    @Override
    public void process(S3Event event) {

        event.getRecords().forEach(record -> {

            String bucket = record.getS3().getBucket().getName();
            String rawKey = record.getS3().getObject().getKey();
            String key = decode(rawKey);

            logger.info("event=transform_start raw_key={} decoded_key={}", rawKey, key);

            try {
                String content = s3Service.read(bucket, key);

                List<Map<String, Object>> events = objectMapper.readValue(content, List.class);


                Map<String, List<ProcessedEvent>> groupedEvents = new HashMap<>();

                for (Map<String, Object> e : events) {

                    String eventType = (String) e.get("eventType");

                    EventTransformer transformer = factory.get(eventType);

                    if (transformer == null) {
                        logger.warn("event=unknown_type eventType={} skipping", eventType);
                        continue;
                    }

                    ProcessedEvent transformed = transformer.transform(e);

                    String groupKey = transformed.getEventType() + "_" + transformed.getEventDate();

                    groupedEvents
                            .computeIfAbsent(groupKey, k -> new ArrayList<>())
                            .add(transformed);
                }


                for (Map.Entry<String, List<ProcessedEvent>> entry : groupedEvents.entrySet()) {

                    List<ProcessedEvent> eventList = entry.getValue();

                    String fileName = generateFileName();

                    String newKey = S3KeyBuilder.buildKey(eventList.get(0), fileName);
                    byte[] parquetBytes =
                            ParquetWriterUtil.writeToParquet(eventList, "v1");

                    String parquetKey = newKey.replace(".json", ".parquet");

                    s3Service.writeBytes(bucket, parquetKey, parquetBytes);

                    logger.info("event=partition_write_success eventType={} count={} key={}",
                            eventList.get(0).getEventType(),
                            eventList.size(),
                            newKey);
                }

                logger.info("event=transform_success input={}", key);

            } catch (Exception ex) {
                logger.error("event=transform_failed key={}", key, ex);
                throw new RuntimeException(ex);
            }
        });
    }

    private String decode(String rawKey) {
        try {
            return URLDecoder.decode(rawKey, "UTF-8");
        } catch (Exception e) {
            throw new RuntimeException("Failed to decode S3 key", e);
        }
    }

    private String generateFileName() {
        return "events_" + System.currentTimeMillis() + "_" + UUID.randomUUID() + ".json";
    }
}
