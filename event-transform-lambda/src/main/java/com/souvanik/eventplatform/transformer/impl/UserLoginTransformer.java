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
public class UserLoginTransformer extends AbstractBaseTransformer {

    @Override
    public String getEventType() {
        return "user-login";
    }

    @Override
    public ProcessedEvent transform(Map<String, Object> e) {

        Map<String, Object> payload = (Map<String, Object>) e.get("payload");

        String deviceType = null;
        String city = null;

        if (payload != null) {

            Map<String, Object> device = (Map<String, Object>) payload.get("device");
            if (device != null) {
                deviceType = (String) device.get("type");
            }

            Map<String, Object> location = (Map<String, Object>) payload.get("location");
            if (location != null) {
                city = (String) location.get("city");
            }
        }

        return baseBuilder(e)
                .deviceType(deviceType)
                .city(city)
                .amount(null)
                .paymentMethod(null)
                .amountCategory("LOW")
                .highValue(false)
                .build();
    }
}