#  Athena Setup & Querying Guide

This section explains how to configure AWS Athena to query the processed data stored in S3.

---

##  Why Athena in this project?

Athena enables:

```plaintext
- Serverless SQL querying on S3
- No infrastructure management
- Pay-per-query model
- Direct integration with partitioned Parquet data
```

In this project, Athena is used to:

```plaintext
- Query processed event data
- Validate transformation logic
- Perform analytics (counts, aggregations, filtering)
```

---

##  Step-by-Step Athena Setup

---

###  Step 1: Open Athena Console

* Go to AWS Console → Athena
* Select or create a **Workgroup** (default is fine)

---

###  Step 2: Configure Query Result Location

Athena requires a result bucket.

* Go to **Settings**
* Set:

```plaintext
s3://<your-bucket>/athena-results/
```

 This is where query outputs are stored.

---

###  Step 3: Create Database

```sql
CREATE DATABASE event_platform;
```

---

###  Step 4: Create Table (Partition Projection Enabled)

```sql
CREATE EXTERNAL TABLE event_platform.processed_events (
  eventid string,
  eventtype string,
  userid string,
  eventtimeutc string,
  eventtimeist string,
  eventdate string,
  eventhour int,
  amount double,
  paymentmethod string,
  devicetype string,
  city string,
  errorcode string,
  source string,
  version string,
  processedat string
)
PARTITIONED BY (
  event_type string,
  year string,
  month string,
  day string
)
STORED AS PARQUET
LOCATION 's3://<your-bucket>/processed/'
TBLPROPERTIES (
  'projection.enabled'='true',

  'projection.event_type.type'='enum',
  'projection.event_type.values'='order-placed,app-crash,payment-failed,user-login',

  'projection.year.type'='integer',
  'projection.year.range'='2024,2030',

  'projection.month.type'='integer',
  'projection.month.range'='1,12',
  'projection.month.format'='00',

  'projection.day.type'='integer',
  'projection.day.range'='1,31',
  'projection.day.format'='00',

  'storage.location.template'=
  's3://<your-bucket>/processed/event_type=${event_type}/year=${year}/month=${month}/day=${day}/'
);
```

---

##  Step 5: Query Data (No MSCK Required)

Example:

```sql
SELECT *
FROM event_platform.processed_events
WHERE event_type='order-placed'
AND year='2026'
AND month='04'
AND day='26';
```

---

##  Partition Projection (Important)

Unlike traditional Athena setups:

```plaintext
- No MSCK REPAIR TABLE needed
- No Glue crawler required
```

Instead:

```plaintext
- Athena dynamically calculates partitions
- New data is instantly queryable
- No metadata sync overhead
```

---

##  Example Analytical Queries

---

### Count events by type

```sql
SELECT event_type, COUNT(*)
FROM event_platform.processed_events
GROUP BY event_type;
```

---

### High-value orders

```sql
SELECT *
FROM event_platform.processed_events
WHERE event_type='order-placed'
AND amount > 2000;
```

---

### Crash analysis

```sql
SELECT errorcode, COUNT(*)
FROM event_platform.processed_events
WHERE event_type='app-crash'
GROUP BY errorcode;
```

---
