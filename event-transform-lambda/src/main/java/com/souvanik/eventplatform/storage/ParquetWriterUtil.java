package com.souvanik.eventplatform.storage;

import com.souvanik.eventplatform.model.ProcessedEvent;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.parquet.avro.AvroParquetWriter;
import org.apache.parquet.hadoop.ParquetWriter;
import org.apache.parquet.hadoop.metadata.CompressionCodecName;
import org.apache.parquet.io.OutputFile;

import java.io.ByteArrayOutputStream;
import java.util.List;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */


public class ParquetWriterUtil {

    public static byte[] writeToParquet(List<ProcessedEvent> events, String version) {

        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            OutputFile outputFile = new InMemoryOutputFile(baos);

            Schema schema = SchemaLoader.load(version);

            ParquetWriter<GenericRecord> writer =
                    AvroParquetWriter.<GenericRecord>builder(outputFile)
                            .withSchema(schema)
                            .withCompressionCodec(CompressionCodecName.SNAPPY)
                            .build();

            for (ProcessedEvent event : events) {
                writer.write(convert(event, schema));
            }

            writer.close();

            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Parquet write failed", e);
        }
    }

    private static GenericRecord convert(ProcessedEvent e, Schema schema) {

        GenericRecord r = new GenericData.Record(schema);

        for (Schema.Field field : schema.getFields()) {

            String name = field.name();

            switch (name) {

                case "eventId":
                    r.put("eventId", e.getEventId());
                    break;

                case "eventType":
                    r.put("eventType", e.getEventType());
                    break;

                case "userId":
                    r.put("userId", e.getUserId());
                    break;

                case "eventTimeUtc":
                    r.put("eventTimeUtc", e.getEventTimeUtc());
                    break;

                case "eventTimeIst":
                    r.put("eventTimeIst", e.getEventTimeIst());
                    break;

                case "eventDate":
                    r.put("eventDate", e.getEventDate());
                    break;

                case "eventHour":
                    r.put("eventHour", e.getEventHour());
                    break;

                case "amount":
                    r.put("amount", e.getAmount());
                    break;

                case "paymentMethod":
                    r.put("paymentMethod", e.getPaymentMethod());
                    break;

                case "amountCategory":
                    r.put("amountCategory", e.getAmountCategory());
                    break;

                case "highValue":
                    r.put("highValue", e.getHighValue());
                    break;

                case "deviceType":
                    r.put("deviceType", e.getDeviceType());
                    break;

                case "city":
                    r.put("city", e.getCity());
                    break;

                case "errorCode":
                    r.put("errorCode", e.getErrorCode());
                    break;

                case "source":
                    r.put("source", e.getSource());
                    break;

                case "version":
                    r.put("version", e.getVersion());
                    break;

                case "processedAt":
                    r.put("processedAt", e.getProcessedAt());
                    break;

                case "schemaVersion":
                    r.put("schemaVersion", "v1");
                    break;

                default:
                    // ignore unknown fields (future-proof)
                    break;
            }
        }

        return r;
    }
}