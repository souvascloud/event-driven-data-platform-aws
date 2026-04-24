package com.souvanik.eventplatform.service.impl;

import com.souvanik.eventplatform.service.S3Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
import software.amazon.awssdk.services.s3.model.NoSuchKeyException;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class S3ServiceImpl implements S3Service {

    private static final Logger logger = LoggerFactory.getLogger(S3Service.class);
    private static final S3Client s3Client = S3Client.builder().build();

    @Override
    public String read(String bucket, String key) {

        logger.info("event=s3_read_start bucket={} key={}", bucket, key);

        GetObjectRequest request = GetObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .build();

        try (ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(request);
             ByteArrayOutputStream buffer = new ByteArrayOutputStream()) {

            byte[] data = new byte[4096];
            int nRead;

            while ((nRead = s3Object.read(data)) != -1) {
                buffer.write(data, 0, nRead);
            }

            String content = buffer.toString("UTF-8");

            logger.info("event=s3_read_success bucket={} key={} size={}",
                    bucket, key, content.length());

            return content;

        } catch (NoSuchKeyException e) {
            throw new RuntimeException("S3 object not found", e);
        } catch (IOException e) {
            throw new RuntimeException("IO error while reading S3 object", e);
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error while reading S3", e);
        }
    }

    @Override
    public void writeBytes(String bucket, String key, byte[] data)  {

        logger.info("event=s3_write_bytes_start bucket={} key={} size={}",
                bucket, key, data.length);

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucket)
                .key(key)
                .contentType("application/octet-stream")
                .build();

        try {
            s3Client.putObject(request, RequestBody.fromBytes(data));

            logger.info("event=s3_write_bytes_success bucket={} key={}", bucket, key);

        } catch (Exception e) {
            logger.error("event=s3_write_bytes_failed bucket={} key={}", bucket, key, e);
            throw new RuntimeException("Failed to write bytes to S3", e);
        }
    }
}
