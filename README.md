# Event-Driven Data Platform (AWS)

This project implements event-driven data pipeline using AWS and Java.
It demonstrates how to ingest, process, transform, and store event data in an analytics-ready format using a scalable and modular architecture.

---

## Overview

The system follows a layered, event-driven architecture:

Ingestion Service (Spring Boot API)
        ↓
S3 (Raw Data Zone)
        ↓
Lambda (Transformation Layer)
        ↓
Parquet + Partitioned Storage
        ↓
S3 (Processed Data Zone)

The pipeline converts raw JSON events into structured, partitioned Parquet files optimized for analytical workloads.

---

## Repository Structure

event-driven-data-platform-aws/
│
├── event-ingestion-service
├── event-transform-lambda
├── processing-service 
└── README.md

---

## Modules

### 1. event-ingestion-service

A Spring Boot service responsible for accepting events via REST APIs.

Responsibilities:
- Validate incoming events
- Support batch ingestion
- send event to SQS
- Maintain ingestion contract

---

### 2. event-transform-lambda

An AWS Lambda function triggered by S3 events.

Responsibilities:
- Read raw event files from S3
- Apply transformation logic using Strategy pattern
- Normalize event structure
- Convert data into Parquet format
- Store data in partitioned layout

---

### 3. processing-service 

- Consume events from ingestion service (via SQS)
- Perform basic validation and optional enrichment
- Batch events for efficient storage
- Write events to S3 in JSON format (raw data layer)
---

## Partition Strategy

processed/
  event_type=order-placed/
    year=2026/
      month=04/
        day=25/
          file.parquet

---

## Technology Stack

- Java 17
- Spring Boot
- AWS S3
- AWS Lambda
- Apache Parquet
- Apache Avro
- Maven

---

## How to Run

cd event-ingestion-service
mvn spring-boot:run

cd event-transform-lambda
mvn clean package

Deploy the generated JAR to AWS Lambda and configure S3 trigger.

---

## Summary

This project demonstrates a complete event-driven data pipeline with clean architecture, extensible transformation logic, and analytics-ready output.
