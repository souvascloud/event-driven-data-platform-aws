package com.souvanik.eventplatform.event_ingestion_service.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.stream.Collectors;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex) {

        String errorMessage = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(FieldError::getDefaultMessage)
                .collect(Collectors.joining(", "));

        logger.warn("event=validation_failed message={}", errorMessage);

        ErrorResponse response = ErrorResponse.builder()
                .status("FAILURE")
                .errorCode("VALIDATION_ERROR")
                .message(errorMessage)
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(
            IllegalArgumentException ex) {

        logger.warn("event=invalid_argument message={}", ex.getMessage());

        ErrorResponse response = ErrorResponse.builder()
                .status("FAILURE")
                .errorCode("INVALID_REQUEST")
                .message(ex.getMessage())
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.badRequest().body(response);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {

        logger.error("event=internal_error message={}", ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.builder()
                .status("FAILURE")
                .errorCode("INTERNAL_ERROR")
                .message("Something went wrong. Please try again later.")
                .timestamp(System.currentTimeMillis())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }

    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Void> handleNotFound(NoResourceFoundException ex) {

        if (ex.getMessage().contains("favicon.ico")) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.notFound().build();
    }
}