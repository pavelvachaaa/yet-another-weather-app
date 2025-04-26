package org.pavelvachacz.yetanotherweatherapp.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.pavelvachacz.yetanotherweatherapp.dtos.MeasurementAggregateDTO;
import org.pavelvachacz.yetanotherweatherapp.models.Measurement;
import org.pavelvachacz.yetanotherweatherapp.services.MeasurementService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/measurement")
@Tag(name = "Measurement Management", description = "APIs for managing measurements")
public class MeasurementController {

    private final MeasurementService measurementService;

    public MeasurementController(MeasurementService measurementService) {
        this.measurementService = measurementService;
    }

    @Operation(summary = "Get all measurements", description = "Returns a list of all measurements")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurements found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Measurement.class)))),
            @ApiResponse(responseCode = "204", description = "No measurements found")
    })
    @GetMapping
    public ResponseEntity<List<Measurement>> getAllMeasurements() {
        List<Measurement> measurements = measurementService.getMeasurements();
        return measurements.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(measurements);
    }

    @Operation(summary = "Get measurements by city", description = "Returns measurements for a specific city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurements found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = Measurement.class)))),
            @ApiResponse(responseCode = "204", description = "No measurements found")
    })
    @GetMapping("/city/{cityName}")
    public ResponseEntity<List<Measurement>> getMeasurementsByCity(
            @Parameter(description = "Name of the city") @PathVariable String cityName) {
        List<Measurement> measurements = measurementService.getMeasurementsByCityName(cityName);
        return measurements.isEmpty() ? ResponseEntity.noContent().build() : ResponseEntity.ok(measurements);
    }

    @Operation(summary = "Get latest measurement", description = "Returns the latest measurement for a specific city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Latest measurement found",
                    content = @Content(schema = @Schema(implementation = Measurement.class))),
            @ApiResponse(responseCode = "404", description = "Measurement not found")
    })
    @GetMapping("/latest/{cityName}")
    public ResponseEntity<Measurement> getLatestMeasurement(
            @Parameter(description = "Name of the city") @PathVariable String cityName) {
        Measurement measurement = measurementService.getLatestMeasurement(cityName);
        return ResponseEntity.ok(measurement);
    }

    @Operation(summary = "Get daily average measurement", description = "Returns the daily average of measurements for a city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Daily average calculated",
                    content = @Content(schema = @Schema(implementation = MeasurementAggregateDTO.class)))
    })
    @GetMapping("/stats/daily/{cityName}")
    public ResponseEntity<MeasurementAggregateDTO> getDailyAverage(
            @Parameter(description = "Name of the city") @PathVariable String cityName) {
        MeasurementAggregateDTO dailyAverage = measurementService.getDailyAverage(cityName);
        return ResponseEntity.ok(dailyAverage);
    }

    @Operation(summary = "Get weekly average measurement", description = "Returns the weekly average of measurements for a city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Weekly average calculated",
                    content = @Content(schema = @Schema(implementation = MeasurementAggregateDTO.class)))
    })
    @GetMapping("/stats/weekly/{cityName}")
    public ResponseEntity<MeasurementAggregateDTO> getWeeklyAverage(
            @Parameter(description = "Name of the city") @PathVariable String cityName) {
        MeasurementAggregateDTO weeklyAverage = measurementService.getWeeklyAverage(cityName);
        return ResponseEntity.ok(weeklyAverage);
    }

    @Operation(summary = "Create a new measurement", description = "Adds a new measurement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Measurement created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid measurement data")
    })
    @PostMapping
    public ResponseEntity<Void> createMeasurement(
            @Parameter(description = "Measurement to create", required = true)
            @RequestBody Measurement measurement) {
        measurementService.save(measurement);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Create multiple measurements", description = "Adds multiple measurements at once")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Measurements created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid measurement data")
    })
    @PostMapping("/batch")
    public ResponseEntity<Void> createMeasurements(
            @Parameter(description = "List of measurements to create", required = true)
            @RequestBody List<Measurement> measurements) {
        measurements.forEach(measurementService::save);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @Operation(summary = "Update a measurement", description = "Updates an existing measurement")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Measurement updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid measurement data")
    })
    @PutMapping
    public ResponseEntity<Void> updateMeasurement(
            @Parameter(description = "Measurement to update", required = true)
            @RequestBody Measurement measurement) {
        measurementService.save(measurement);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete a measurement", description = "Deletes a measurement by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Measurement deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Measurement not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeasurement(
            @Parameter(description = "ID of the measurement to delete") @PathVariable int id) {
        Measurement measurement = new Measurement();
        measurement.setId(id);
        measurementService.delete(measurement);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete measurements by city", description = "Deletes all measurements for a given city")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Measurements deleted successfully"),
            @ApiResponse(responseCode = "404", description = "City not found")
    })
    @DeleteMapping("/city/{cityName}")
    public ResponseEntity<Void> deleteMeasurementsByCity(
            @Parameter(description = "Name of the city") @PathVariable String cityName) {
        measurementService.deleteByCity(cityName);
        return ResponseEntity.noContent().build();
    }
}
