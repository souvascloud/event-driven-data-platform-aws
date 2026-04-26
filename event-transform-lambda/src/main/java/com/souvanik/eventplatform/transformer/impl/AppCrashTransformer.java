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


            Object errorObj = payload.get("error");
            if (errorObj instanceof Map) {
                Map<String, Object> error = (Map<String, Object>) errorObj;
                if (error.get("code") != null) {
                    errorCode = error.get("code").toString();
                }
            }

            if (errorCode == null && payload.get("errorCode") != null) {
                errorCode = payload.get("errorCode").toString();
            }


            if (errorCode == null && payload.get("code") != null) {
                errorCode = payload.get("code").toString();
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