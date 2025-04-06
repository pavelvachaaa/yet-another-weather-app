package org.pavelvachacz.yetanotherweatherapp.controllers;

import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.pavelvachacz.yetanotherweatherapp.services.MeasurementService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/measurement")
public class MeasurementController {

    private final MeasurementService measurementService;

    @Autowired
    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    // Get all measurements
    @GetMapping
    public ResponseEntity<List<Measurement>> getAllMeasurements() {
        List<Measurement> measurements = measurementService.getAllMeasurements();
        return ResponseEntity.ok(measurements);
    }

    // Get measurements by city ID
    @GetMapping("/city/{cityId}")
    public ResponseEntity<List<Measurement>> getMeasurementsByCityId(@PathVariable int cityId) {
        List<Measurement> measurements = measurementService.getMeasurementsByCityId(cityId);
        return ResponseEntity.ok(measurements);
    }

    // Get a specific measurement by ID
    @GetMapping("/{id}")
    public ResponseEntity<Measurement> getMeasurementById(@PathVariable int id) {
        Optional<Measurement> measurement = measurementService.getMeasurementById(id);
        return measurement.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Create a new measurement
    @PostMapping
    public ResponseEntity<Void> createMeasurement(@RequestBody Measurement measurement) {
        if (measurementService.createMeasurement(measurement)) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.badRequest().build();
    }

    // Create a batch of measurements
    @PostMapping("/batch")
    public ResponseEntity<Void> createMeasurements(@RequestBody List<Measurement> measurements) {
        int[] results = measurementService.createMeasurements(measurements);
        return results.length > 0 ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.badRequest().build();
    }

    // Update a measurement
    @PutMapping
    public ResponseEntity<Void> updateMeasurement(@RequestBody Measurement measurement) {
        if (measurementService.updateMeasurement(measurement)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    // Delete a measurement by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeasurement(@PathVariable int id) {
        if (measurementService.deleteMeasurement(id)) {
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
