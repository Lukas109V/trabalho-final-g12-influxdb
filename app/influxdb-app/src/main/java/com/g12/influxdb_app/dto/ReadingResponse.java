package com.g12.influxdb_app.dto;

import java.time.Instant;

public record ReadingResponse(
        Instant time,
        String sensorId,
        String room,
        String status,
        String field,
        Object value
) {
}