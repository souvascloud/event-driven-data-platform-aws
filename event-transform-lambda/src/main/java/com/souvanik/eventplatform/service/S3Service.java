package com.souvanik.eventplatform.service;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface S3Service {

    public String read(String bucket, String key);
    void writeBytes(String bucket, String key, byte[] data);
}
