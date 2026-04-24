package com.souvanik.eventplatform.processing_service.service;

import com.souvanik.eventplatform.processing_service.dto.EventMessage;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface BatchService {
    void addEvent(EventMessage event);
}
