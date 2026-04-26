#  Event-Driven Data Platform (AWS)

**Event-driven data pipeline** built using AWS and Java that ingests, processes, transforms, and serves analytics ready data using a scalable data lake architecture.

---

#  Why this project?

Modern systems generate massive volumes of events:

* User actions (login, purchase)
* System events (errors, crashes)
* Business transactions (orders, payments)

These events need to be:

```plaintext
- Captured in real-time
- Stored reliably
- Transformed into structured format
- Queried efficiently for analytics
```

This project demonstrates **how real companies build such pipelines**.

---

# What this project solves

| Problem                         | Solution                       |
| ------------------------------- | ------------------------------ |
| High-throughput event ingestion | SQS decoupling                 |
| Raw data storage                | S3 Data Lake                   |
| Schema inconsistency            | Transformer (Strategy pattern) |
| Analytics performance           | Parquet + Partitioning         |
| Querying large datasets         | Athena                         |
| Partition management overhead   | Partition Projection           |

---

#  High-Level Architecture

```plaintext
Client → Ingestion Service → SQS → Processing Service → S3 (Raw)
                                                    ↓
                                             Lambda Transform
                                                    ↓
                                          S3 (Processed - Parquet)
                                                    ↓
                                                 Athena
```

---

#  Architecture Diagram (PlantUML)



---

#  System Evolution 

This project was built incrementally like a real system:

---

##  Phase 1: Basic Ingestion

```plaintext
API → S3
```

Problems:

* Tight coupling
* No scalability
* No fault tolerance

---

## Phase 2: Introduced SQS

```plaintext
API → SQS → Consumer
```

- Decoupled system
- Improved reliability

---

##  Phase 3: Raw Data Lake (S3)

```plaintext
Consumer → S3 (JSON)
```

-Immutable raw data
-Replay capability
-Cheap storage

---

## Phase 4: Transformation Layer

```plaintext
S3 → Lambda → Structured Output
```

- Strategy pattern for event types
- Schema normalization
- Derived fields

---

##  Phase 5: Parquet + Partitioning

```plaintext
Processed → Parquet + Partitioned S3
```

- Columnar storage
- Faster queries
- Reduced cost

---

##  Phase 6: Athena Integration

```plaintext
S3 → Athena
```

- Serverless querying
- SQL-based analytics
- No infrastructure needed

---

##  Phase 7: Partition Projection (Advanced)

```plaintext
Removed MSCK REPAIR
```

- No metadata sync needed
- Real-time query availability
- Production-grade optimization

---

#  Repository Structure

```plaintext
event-driven-data-platform-aws/
├── event-ingestion-service
├── processing-service
├── event-transform-lambda
└── README.md
```

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

Triggered on S3 object creation.

### Responsibilities

```plaintext
- Read raw JSON
- Transform using Strategy pattern
- Normalize schema
- Convert to Parquet
- Partition data
```

---

#  Transformation Design (Strategy Pattern)

Each event type has its own transformer:

```plaintext
EventTransformer (interface)
 ├── OrderPlacedTransformer
 ├── PaymentFailedTransformer
 ├── UserLoginTransformer
 └── AppCrashTransformer
```

- Extensible
- Clean separation
- Easy to add new event types

---

#  Data Lake Design

---

##  Raw Layer

```plaintext
raw/
  user-events/year=2026/month=04/day=26/
```

- Immutable
- Replayable
- Schema-free

---

##  Processed Layer

```plaintext
processed/
  event_type=order-placed/
    year=2026/month=04/day=26/
```

- Structured
- Optimized for analytics
- Partitioned

---

#  Partition Strategy

```plaintext
event_type + year + month + day
```

### Why?

```plaintext
- Reduces query scan size
- Improves performance
- Enables partition pruning
```

---

#  Why Parquet?

| JSON         | Parquet      |
| ------------ | ------------ |
| Row-based    | Columnar     |
| Large size   | Compressed   |
| Slow queries | Fast queries |

---

#  Why Athena?

Athena is a **serverless query engine** for S3.

### Without Athena

```plaintext
- Need Spark/EMR cluster
- Infrastructure overhead
```

### With Athena

```plaintext
- Query using SQL
- No servers
- Pay per query
- Works directly on S3
```

---

#  Cost Optimization

```plaintext
- Parquet → less data scanned
- Partitioning → selective reads
- Projection → no metadata overhead
```

---

#  Partition Projection 

Instead of:

```plaintext
S3 → MSCK → Metastore
```

We use:

```plaintext
S3 → Direct Query
```

### Benefits

```plaintext
- No MSCK REPAIR
- Real-time partitions
- Faster queries
- Scalable metadata
```

---

#  Example Query

```sql
SELECT *
FROM processed_events
WHERE event_type='order-placed'
AND year='2026'
AND month='04'
AND day='26';
```

---

#  Tech Stack

```plaintext
Java 17
Spring Boot
AWS SQS
AWS S3
AWS Lambda
Apache Parquet
Apache Avro
AWS Athena
```

---

#  How to Run

---

## 1. Start ingestion service

```bash
cd event-ingestion-service
mvn spring-boot:run
```

---

## 2. Deploy Lambda

```bash
cd event-transform-lambda
mvn clean package
```

Upload JAR to AWS Lambda and configure S3 trigger.

---

## 3. Start processing service

```bash
cd processing-service
mvn spring-boot:run
```

---


#  Final Summary

This project demonstrates:

```plaintext
- Real-time ingestion pipeline
- Scalable data lake architecture
- Efficient transformation layer
- Analytics-ready storage
- Serverless querying
```

---
## License

Internal / Educational / Enterprise Architecture Reference

---

## Author

Senior Software Engineer

Passionate about backend systems, design, and clean code.

LinkedIn: https://www.linkedin.com/in/souvanik-saha
