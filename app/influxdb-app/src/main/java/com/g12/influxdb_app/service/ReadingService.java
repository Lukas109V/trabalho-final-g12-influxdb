package com.g12.influxdb_app.service;

import com.g12.influxdb_app.dto.ReadingRequest;
import com.g12.influxdb_app.dto.ReadingResponse;
import com.influxdb.client.DeleteApi;
import com.influxdb.client.InfluxDBClient;
import com.influxdb.client.QueryApi;
import com.influxdb.client.WriteApiBlocking;
import com.influxdb.client.domain.WritePrecision;
import com.influxdb.client.write.Point;
import com.influxdb.query.FluxRecord;
import com.influxdb.query.FluxTable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Service
public class ReadingService {

    private final InfluxDBClient influxDBClient;

    @Value("${influx.org}")
    private String org;

    @Value("${influx.bucket}")
    private String bucket;

    public ReadingService(InfluxDBClient influxDBClient) {
        this.influxDBClient = influxDBClient;
    }

    public String create(ReadingRequest request) {
        WriteApiBlocking writeApi = influxDBClient.getWriteApiBlocking();

        Point point = Point.measurement("sensor_reading")
                .addTag("sensorId", request.sensorId())
                .addTag("room", request.room())
                .addTag("type", request.type())
                .addTag("status", request.status())
                .addField("temperature", request.temperature())
                .addField("humidity", request.humidity())
                .addField("co2", request.co2())
                .addField("battery", request.battery())
                .time(Instant.now(), WritePrecision.NS);

        writeApi.writePoint(bucket, org, point);

        return "Leitura inserida com sucesso.";
    }

    public List<ReadingResponse> findAll() {
        String flux = """
                from(bucket: "%s")
                  |> range(start: 2024-06-17T00:00:00Z)
                  |> filter(fn: (r) => r._measurement == "sensor_reading")
                  |> keep(columns: ["_time", "sensorId", "room", "status", "_field", "_value"])
                """.formatted(bucket);

        return executeQuery(flux);
    }

    public List<ReadingResponse> findBySensorId(String sensorId) {
        String flux = """
                from(bucket: "%s")
                  |> range(start: 2024-06-17T00:00:00Z)
                  |> filter(fn: (r) => r._measurement == "sensor_reading")
                  |> filter(fn: (r) => r.sensorId == "%s")
                  |> keep(columns: ["_time", "sensorId", "room", "status", "_field", "_value"])
                """.formatted(bucket, sensorId);

        return executeQuery(flux);
    }

    public String updateBySensorId(String sensorId, ReadingRequest request) {
        deleteBySensorId(sensorId);
        create(request);

        return "Leitura atualizada com sucesso.";
    }

    public String deleteBySensorId(String sensorId) {
        DeleteApi deleteApi = influxDBClient.getDeleteApi();

        OffsetDateTime start = OffsetDateTime.of(2024, 6, 17, 0, 0, 0, 0, ZoneOffset.UTC);
        OffsetDateTime stop = OffsetDateTime.now(ZoneOffset.UTC).plusDays(1);

        String predicate = String.format("_measurement=\"sensor_reading\" AND sensorId=\"%s\"", sensorId);

        deleteApi.delete(start, stop, predicate, bucket, org);

        return "Leituras do sensor " + sensorId + " removidas com sucesso.";
    }

    private List<ReadingResponse> executeQuery(String flux) {
        QueryApi queryApi = influxDBClient.getQueryApi();
        List<FluxTable> tables = queryApi.query(flux, org);

        List<ReadingResponse> responses = new ArrayList<>();

        for (FluxTable table : tables) {
            for (FluxRecord record : table.getRecords()) {
                responses.add(new ReadingResponse(
                        record.getTime(),
                        String.valueOf(record.getValueByKey("sensorId")),
                        String.valueOf(record.getValueByKey("room")),
                        String.valueOf(record.getValueByKey("status")),
                        String.valueOf(record.getValueByKey("_field")),
                        record.getValue()
                ));
            }
        }

        return responses;
    }
}