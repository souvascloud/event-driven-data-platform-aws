# Event-Driven Data Platform (AWS)

This project implements event-driven data pipeline using AWS and Java.  
It demonstrates how raw events are ingested, processed, transformed, and stored in an analytics-ready format using a scalable and decoupled architecture.

---

## System Overview

The platform follows an event-driven, asynchronous architecture:

Ingestion Service (Spring Boot API)  
→ SQS (Message Queue for decoupling)  
→ Processing Service (Batching + Raw Storage)  
→ S3 (Raw Data Zone - JSON)  
→ Lambda (Transformation Layer)  
→ S3 (Processed Data Zone - Parquet, Partitioned)

---

## Data Flow

1. Clients send batch events to the ingestion API  
2. Ingestion service validates and publishes events to SQS  
3. Processing service consumes events from SQS  
4. Events are batched and stored in S3 as raw JSON  
5. S3 triggers Lambda for transformation  
6. Lambda:
   - Reads raw data  
   - Applies event-specific transformation (Strategy pattern)  
   - Normalizes event structure  
   - Converts data to Parquet  
7. Transformed data is stored in partitioned format for analytics  

---

## Repository Structure

event-driven-data-platform-aws/
├── event-ingestion-service
├── processing-service
├── event-transform-lambda
└── README.md

---

## Modules

### 1. event-ingestion-service

Handles external interaction via REST APIs.

Responsibilities:
- Accept batch events
- Validate request payloads
- Publish events to SQS
- Maintain ingestion contract

Design Focus:
- API layer only (no business logic leakage)
- Loose coupling via messaging

---

### 2. processing-service

Acts as the raw data persistence layer.

Responsibilities:
- Consume events from SQS
- Perform lightweight validation/enrichment
- Batch events for efficient writes
- Store events in S3 as JSON

Why this layer exists:
- Decouples ingestion from storage
- Improves reliability through buffering
- Enables replay and debugging from raw data

---

### 3. event-transform-lambda

Responsible for transforming raw data into analytics-ready format.

Responsibilities:
- Triggered automatically on S3 upload
- Applies transformation using Strategy pattern
- Converts JSON → Parquet
- Writes partitioned data to S3

Design Focus:
- Serverless processing
- Schema-driven transformation (Avro)
- Extensibility for new event types

---

## Storage Strategy

### Raw Data (Immutable)

raw/
  user-events/
    year=2026/month=04/day=25/
      events_*.json

### Processed Data (Analytics Ready)

processed/
  event_type=order-placed/
    year=2026/month=04/day=25/
      file.parquet

---

## Key Design Decisions

### 1. Event-Driven Architecture
- Services communicate via SQS
- Enables scalability and fault tolerance

### 2. Separation of Concerns
- Ingestion → API only  
- Processing → storage only  
- Lambda → transformation only  

### 3. Raw Data Layer
- Always store original data before transformation
- Supports replay and debugging

### 4. Strategy Pattern for Transformation
- Each event type has its own transformer
- Easily extensible without modifying core logic

### 5. Schema-Driven Design
- Avro schema ensures consistency
- Supports backward-compatible evolution

### 6. Columnar Storage (Parquet)
- Optimized for analytics
- Reduces storage and query cost

### 7. Partitioning Strategy
- Partition by event_type + date
- Enables efficient query pruning (Athena/Presto)

---

## Technology Stack

- Java 17
- Spring Boot
- AWS S3
- AWS Lambda
- AWS SQS
- Apache Parquet
- Apache Avro
- Maven

---

## How to Run

### Ingestion Service

cd event-ingestion-service  
mvn spring-boot:run  

### Processing Service

cd processing-service  
mvn spring-boot:run  

### Lambda

cd event-transform-lambda  
mvn clean package  

Deploy the generated JAR to AWS Lambda and configure S3 trigger.

---

## Summary

This project demonstrates how to build a scalable event-driven data platform with:

- Decoupled microservices
- Asynchronous processing using SQS
- Durable raw data storage
- Schema-driven transformation
- Partitioned, analytics-ready data lake
