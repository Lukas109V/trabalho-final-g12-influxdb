package com.g12.influxdb_app.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReadingRequest(
        @NotBlank String sensorId,
        @NotBlank String room,
        @NotBlank String type,
        @NotBlank String status,
        @NotNull Double temperature,
        @NotNull Double humidity,
        @NotNull Double co2,
        @NotNull Integer battery
) {
}