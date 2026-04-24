package com.souvanik.eventplatform.processing_service.storage;

import com.souvanik.eventplatform.processing_service.dto.EventMessage;

import java.util.List;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface S3Service {

    void uploadBatch(List<EventMessage> events);
}
