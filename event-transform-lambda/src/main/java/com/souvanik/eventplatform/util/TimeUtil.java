package com.souvanik.eventplatform.util;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

/*
 * Copyright (c) 2026 Souvanik Saha
 *
 * Licensed under the MIT License.
 * https://opensource.org/licenses/MIT
 */



public class TimeUtil {

        private static final ZoneId IST = ZoneId.of("Asia/Kolkata");


        private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ISO_OFFSET_DATE_TIME;

        private static final DateTimeFormatter SIMPLE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ssXXX");


        private static ZonedDateTime parse(String ts) {
            return ZonedDateTime.parse(ts);
        }

        public static String utc(String ts) {
            return parse(ts)
                    .withZoneSameInstant(ZoneOffset.UTC)
                    .format(FORMATTER);
        }

        public static String ist(String ts) {
            return parse(ts)
                    .withZoneSameInstant(IST)
                    .format(FORMATTER);
        }

        public static String date(String ts) {
            return parse(ts)
                    .withZoneSameInstant(IST)
                    .toLocalDate()
                    .toString();
        }

        public static int hour(String ts) {
            return parse(ts)
                    .withZoneSameInstant(IST)
                    .getHour();
        }

        public static String nowIst() {
            return ZonedDateTime.now(IST)
                    .withNano(0)
                    .format(SIMPLE_FORMATTER);
        }

}