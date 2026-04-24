package com.souvanik.eventplatform.storage;

import org.apache.avro.Schema;

import java.io.InputStream;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */
public class SchemaLoader {
    public static Schema load(String version) {

        String path = "/schema/processed-event-" + version + ".avsc";

        try (InputStream is = SchemaLoader.class.getResourceAsStream(path)) {

            if (is == null) {
                throw new RuntimeException("Schema not found: " + path);
            }

            return new Schema.Parser().parse(is);

        } catch (Exception e) {
            throw new RuntimeException("Failed to load schema", e);
        }
    }
}
