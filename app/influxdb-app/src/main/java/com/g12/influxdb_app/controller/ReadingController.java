package com.g12.influxdb_app.controller;

import com.g12.influxdb_app.dto.ReadingRequest;
import com.g12.influxdb_app.dto.ReadingResponse;
import com.g12.influxdb_app.service.ReadingService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/readings")
public class ReadingController {

    private final ReadingService readingService;

    public ReadingController(ReadingService readingService) {
        this.readingService = readingService;
    }

    @PostMapping
    public String create(@RequestBody @Valid ReadingRequest request) {
        return readingService.create(request);
    }

    @GetMapping
    public List<ReadingResponse> findAll() {
        return readingService.findAll();
    }

    @GetMapping("/{sensorId}")
    public List<ReadingResponse> findBySensorId(@PathVariable String sensorId) {
        return readingService.findBySensorId(sensorId);
    }

    @PutMapping("/{sensorId}")
    public String updateBySensorId(
            @PathVariable String sensorId,
            @RequestBody @Valid ReadingRequest request
    ) {
        return readingService.updateBySensorId(sensorId, request);
    }

    @DeleteMapping("/{sensorId}")
    public String deleteBySensorId(@PathVariable String sensorId) {
        return readingService.deleteBySensorId(sensorId);
    }
}