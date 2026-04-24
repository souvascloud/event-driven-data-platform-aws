package com.souvanik.eventplatform.transformer.impl;

import com.souvanik.eventplatform.model.ProcessedEvent;
import com.souvanik.eventplatform.transformer.AbstractBaseTransformer;
import com.souvanik.eventplatform.transformer.EventTransformer;
import com.souvanik.eventplatform.util.TimeUtil;

import java.util.Map;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class AppCrashTransformer extends AbstractBaseTransformer {

    @Override
    public String getEventType() {
        return "app-crash";
    }

    @Override
    public ProcessedEvent transform(Map<String, Object> e) {

        Map<String, Object> payload = (Map<String, Object>) e.get("payload");

        String errorCode = null;

        if (payload != null) {

            Map<String, Object> error = (Map<String, Object>) payload.get("error");
            if (error != null) {
                errorCode = (String) error.get("code");
            }
        }

        return baseBuilder(e)
                .errorCode(errorCode)
                .amount(null)
                .paymentMethod(null)
                .amountCategory("LOW")
                .highValue(false)
                .build();
    }
}