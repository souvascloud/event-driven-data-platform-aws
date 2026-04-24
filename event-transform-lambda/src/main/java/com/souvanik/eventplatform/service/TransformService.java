package com.souvanik.eventplatform.service;

import com.amazonaws.services.lambda.runtime.events.S3Event;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public interface TransformService {
    public void process(S3Event event);
}
