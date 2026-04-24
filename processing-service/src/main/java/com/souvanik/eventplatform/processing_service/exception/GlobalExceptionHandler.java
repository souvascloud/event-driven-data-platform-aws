package com.souvanik.eventplatform.processing_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle all unexpected exceptions
     */
    @ExceptionHandler(Exception.class)
    public void handleGenericException(Exception ex) {
        logger.error("event=internal_error message={}", ex.getMessage(), ex);
    }
}