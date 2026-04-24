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
public class OrderPlacedTransformer extends AbstractBaseTransformer {

    @Override
    public String getEventType() {
        return "order-placed";
    }

    @Override
    public ProcessedEvent transform(Map<String, Object> e) {

        Map<String, Object> payload = (Map<String, Object>) e.get("payload");

        Double amount = payload != null && payload.get("amount") != null
                ? Double.valueOf(payload.get("amount").toString())
                : null;

        String paymentMethod = null;

        if (payload != null) {
            Map<String, Object> payment = (Map<String, Object>) payload.get("payment");
            if (payment != null) {
                paymentMethod = (String) payment.get("method");
            }
        }

        return baseBuilder(e)
                .amount(amount)
                .paymentMethod(paymentMethod)
                .amountCategory(amount != null && amount > 2000 ? "HIGH" : "LOW")
                .highValue(amount != null && amount > 2000)
                .build();
    }

}